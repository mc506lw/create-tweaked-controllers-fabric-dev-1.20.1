package com.llyycci.create_tweaked_controllers.gui.InputConfig;

import com.llyycci.create_tweaked_controllers.Create_Tweaked_Controllers;
import com.llyycci.create_tweaked_controllers.controller.TweakedControlsUtil;
import com.llyycci.create_tweaked_controllers.gui.ModControllerConfigScreen;
import com.llyycci.create_tweaked_controllers.input.GamepadInputs;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.gui.ScreenOpener;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class InputList extends ContainerObjectSelectionList<InputList.Entry> {
    final ModControllerConfigScreen modControllerConfigScreen;
    int maxNameWidth;

    public InputList(ModControllerConfigScreen screen, Minecraft mc) {
        super(mc, screen.width + 45, screen.height, 120, screen.height - 32, 20);
        modControllerConfigScreen = screen;
        addEntry(new CategoryEntry(Create_Tweaked_Controllers.translateDirect("gui_gamepad_buttons")));
        for (int i = 0; i < 15; i++) {
            Component c = GamepadInputs.GetButtonName(i);
            int l = mc.font.width(c);
            if (l > maxNameWidth) {
                maxNameWidth = l;
            }
            addEntry(new InputEntry(i, c, screen));
        }
        addEntry(new CategoryEntry(Create_Tweaked_Controllers.translateDirect("gui_gamepad_axis")));
        for (int i = 0; i < 10; i++) {
            Component c = GamepadInputs.GetAxisName(i);
            int l = mc.font.width(c);
            if (l > maxNameWidth) {
                maxNameWidth = l;
            }
            addEntry(new InputEntry(i+15, c, screen));
        }
    }

    protected int getScrollbarPosition()
    {
        return super.getScrollbarPosition() + 15 + 120;
    }

    public int getRowWidth()
    {
        return super.getRowWidth() + 32;
    }

	@Environment(EnvType.CLIENT)
    public class CategoryEntry extends Entry {
        final Component name;
        private final int width;

        public CategoryEntry(Component comp) {
            name = comp;
            width = InputList.this.minecraft.font.width(name);
        }

        public void render(GuiGraphics graphics, int p_193889_, int p_193890_, int p_193891_, int p_193892_,
            int p_193893_, int p_193894_, int p_193895_, boolean p_193896_, float p_193897_) {
            graphics.drawString(InputList.this.minecraft.font, name,
                    (int) (InputList.this.minecraft.screen.width / 2 - width / 2),
                    (int) (p_193890_ + p_193893_ - 9 - 1), 16777215);
        }

        public boolean changeFocus(boolean value)
        {
            return false;
        }

        public List<? extends GuiEventListener> children()
        {
            return Collections.emptyList();
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                public NarrationPriority narrationPriority()
                {
                    return NarrationPriority.HOVERED;
                }

                public void updateNarration(NarrationElementOutput output) {
                    output.add(NarratedElementType.TITLE, CategoryEntry.this.name);
                }
            });
        }
    }

	@Environment(EnvType.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> { }

	@Environment(EnvType.CLIENT)
    public class InputEntry extends Entry {
        private final int key;
        private final Component name;
        private final Button changeButton;
        private final Button resetButton;
        private final Button configButton;

        InputEntry(final int input, Component text, Screen parent) {
            key = input;
            name = text;
            changeButton = Button.builder(name, (b) -> {
                InputList.this.modControllerConfigScreen.SetActiveInput(input);
            }).bounds(0, 0, 95, 20).createNarration(new Button.CreateNarration() {
                @Override
                public MutableComponent createNarrationMessage(Supplier<MutableComponent> p_253695_) {
                    return (TweakedControlsUtil.profile.layout[key] != null) ? Component.translatable("narrator.controls.unbound", name)
                            : Component.translatable("narrator.controls.bound", name);
                }
            }).build();
            resetButton = Button.builder(Create_Tweaked_Controllers.translateDirect("gui_config_reset"), (b) -> {
                TweakedControlsUtil.profile.layout[key] = null;
                TweakedControlsUtil.profile.UpdateProfileData();
            }).bounds(0, 0, 50, 20).createNarration(new Button.CreateNarration() {
                @Override
                public MutableComponent createNarrationMessage(Supplier<MutableComponent> p_253695_) {
                    return Component.translatable("narrator.controls.reset", name);
                }
            }).build();
            configButton = Button.builder(Create_Tweaked_Controllers.translateDirect("gui_config_config"), (b) -> {
                ScreenOpener.open(TweakedControlsUtil.profile.layout[key].OpenConfigScreen(parent, name));
            }).bounds(0, 0, 50, 20).createNarration(new Button.CreateNarration() {
                @Override
                public MutableComponent createNarrationMessage(Supplier<MutableComponent> p_253695_) {
                    return Component.translatable("narrator.controls.reset", name);
                }
            }).build();
        }

        public void render(GuiGraphics p_193923_, int p_193924_, int p_193925_, int p_193926_, int p_193927_,
                int p_193928_, int p_193929_, int p_193930_, boolean p_193931_, float p_193932_) {
            float f = (float) (p_193926_ + 40 - InputList.this.maxNameWidth);
            p_193923_.drawString(InputList.this.minecraft.font, name, (int)f, (int)(p_193925_ + p_193928_ / 2 - 9 / 2), 16777215);
                    boolean active = TweakedControlsUtil.profile.layout[key] != null;
            resetButton.setX(p_193926_ + 155);
            resetButton.setY(p_193925_);
            resetButton.active = active;
            resetButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
            configButton.setX(p_193926_ + 210);
            configButton.setY(p_193925_);
            configButton.active = active;
            configButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
            changeButton.setX(p_193926_ + 55);
            changeButton.setY(p_193925_);
            changeButton.setMessage((
                active ?
                TweakedControlsUtil.profile.layout[key].GetDisplayName() :
                Create_Tweaked_Controllers.translateDirect("gui_config_none")
                ));
            if (InputList.this.modControllerConfigScreen.GetActiveInput() == key) {
                changeButton.setMessage((Component.literal("> "))
                        .append(changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW)).append(" <")
                        .withStyle(ChatFormatting.YELLOW));
            } else if (!active || !TweakedControlsUtil.profile.layout[key].IsInputValid()) {
                changeButton.setMessage(changeButton.getMessage().copy()
                        .withStyle(active ? ChatFormatting.RED : ChatFormatting.DARK_AQUA));
            }

            changeButton.render(p_193923_, p_193929_, p_193930_, p_193932_);
        }

        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(changeButton, resetButton, configButton);
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(changeButton, resetButton, configButton);
        }

        public boolean mouseClicked(double p_193919_, double p_193920_, int p_193921_) {
            if (changeButton.mouseClicked(p_193919_, p_193920_, p_193921_)) {
                return true;
            } else {
                return resetButton.mouseClicked(p_193919_, p_193920_, p_193921_)
                    || configButton.mouseClicked(p_193919_, p_193920_, p_193921_);
            }
        }

        public boolean mouseReleased(double p_193941_, double p_193942_, int p_193943_) {
            return changeButton.mouseReleased(p_193941_, p_193942_, p_193943_)
                    || resetButton.mouseReleased(p_193941_, p_193942_, p_193943_)
                    || configButton.mouseReleased(p_193941_, p_193942_, p_193943_);
        }
    }
}
