package com.llyycci.create_tweaked_controllers.gui;

import com.llyycci.create_tweaked_controllers.config.ModClientConfig;
import com.llyycci.create_tweaked_controllers.item.ModItems;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Components;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;

import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModMainConfigButton extends Button {

	public static final ItemStack ICON = ModItems.TWEAKED_LINKED_CONTROLLER.asStack(); // TODO maybe put an icon

	public ModMainConfigButton(int x, int y) {
		super(x, y, 20, 20, Components.immutableEmpty(), ModMainConfigButton::click, DEFAULT_NARRATION);
	}

	@Override
	public void renderString(GuiGraphics graphics, Font pFont, int pColor) {
		graphics.renderItem(ICON, getX() + 2, getY() + 2);
	}

	public static void click(Button b)
	{
		ScreenOpener.open(new ModConfigScreen(Minecraft.getInstance().screen));
	}

	public static class SingleMenuRow {
		public final String left, right;
		public SingleMenuRow(String left, String right) {
			this.left = I18n.get(left);
			this.right = I18n.get(right);
		}
		public SingleMenuRow(String center) {
			this(center, center);
		}
	}

	public static class MenuRows {
		protected final List<String> leftButtons, rightButtons;

		public MenuRows(List<SingleMenuRow> variants) {
			leftButtons = variants.stream().map(r -> r.left).collect(Collectors.toList());
			rightButtons = variants.stream().map(r -> r.right).collect(Collectors.toList());
		}

		public static MenuRows CreateMainMenuRows() {
			return new MenuRows(Arrays.asList(
					new SingleMenuRow("menu.singleplayer"),
					new SingleMenuRow("menu.multiplayer"),
					new SingleMenuRow("fml.menu.mods", "menu.online"),
					new SingleMenuRow("narrator.button.language", "narrator.button.accessibility")
			));
		}

		public static MenuRows CreateIngameMenuRows() {
			return new MenuRows(Arrays.asList(
					new SingleMenuRow("menu.returnToGame"),
					new SingleMenuRow("gui.advancements", "gui.stats"),
					new SingleMenuRow("menu.sendFeedback", "menu.reportBugs"),
					new SingleMenuRow("menu.options", "menu.shareToLan"),
					new SingleMenuRow("menu.returnToMenu")
			));
		}
	}

	public static class OpenConfigButtonHandler {

		public static void onGuiInit(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
			MenuRows menu = null;
			int rowIdx = 0;
			int offsetX = 0;

			if (screen instanceof TitleScreen) {
				menu = MenuRows.CreateMainMenuRows();
				rowIdx = ModClientConfig.CONFIG_BUTTON_MAIN_MENU_ROW.get();
				offsetX = ModClientConfig.CONFIG_BUTTON_MAIN_MENU_OFFSET.get();
			} else if (screen instanceof PauseScreen) {
				menu = MenuRows.CreateIngameMenuRows();
				rowIdx = ModClientConfig.CONFIG_BUTTON_INGAME_MENU_ROW.get();
				offsetX = ModClientConfig.CONFIG_BUTTON_INGAME_MENU_OFFSET.get();
			}

			if (rowIdx != 0 && menu != null) {
				boolean onLeft = offsetX < 0;
				String target = (onLeft ? menu.leftButtons : menu.rightButtons).get(rowIdx - 1);

				int offsetX_ = offsetX;
				MutableObject<ModMainConfigButton> toAdd = new MutableObject<>(null);
				Screens.getButtons(screen).stream()
						.filter(w -> w instanceof AbstractWidget)
						.map(w -> (AbstractWidget) w)
						.filter(w -> w.getMessage()
								.getString()
								.equals(target))
						.findFirst()
						.ifPresent(w -> toAdd
								.setValue(new ModMainConfigButton(w.getX() + offsetX_ + (onLeft ? -20 : w.getWidth()), w.getY())));
				if (toAdd.getValue() != null)
					Screens.getButtons(screen).add(toAdd.getValue());//原为screen.addRenderableWidget(toAdd.getValue());可能有bug
			}
		}
	}
}
