package com.llyycci.create_tweaked_controllers.gui;

import com.llyycci.create_tweaked_controllers.Create_Tweaked_Controllers;
import com.llyycci.create_tweaked_controllers.controller.TweakedLinkedControllerMenu;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ModMenuTypes {
	private static final CreateRegistrate REGISTRATE = Create_Tweaked_Controllers.registrate();

	public static final MenuEntry<TweakedLinkedControllerMenu> TWEAKED_LINKED_CONTROLLER =
			register("tweaked_linked_controller", TweakedLinkedControllerMenu::new, () -> TweakedLinkedControllerScreen::new);

	private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
			String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
		return REGISTRATE
				.menu(name, factory, screenFactory)
				.register();
	}

	public static void register() {}
}
