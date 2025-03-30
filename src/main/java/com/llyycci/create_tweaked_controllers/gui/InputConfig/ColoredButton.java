package com.llyycci.create_tweaked_controllers.gui.InputConfig;

import org.joml.Vector3f;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ColoredButton extends Button
{
    public Vector3f color = new Vector3f(1, 1, 1);

    public ColoredButton(int x, int y, int width, int height, Component message, Button.OnPress onPress, Vector3f color)
    {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.color = color;
    }

    private int getTextureYReimplemented()
    {
        int i = 1;
        if (!this.active)
        {
            i = 0;
        }
        else if (this.isHoveredOrFocused())
        {
            i = 2;
        }
        return 46 + i * 20;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int x, int y, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        graphics.setColor(color.x, color.y, color.z, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureYReimplemented());
        graphics.setColor(color.x, color.y, color.z, 1.0F);
        int i = isActive() ? 16777215 : 10526880;//if active, white, else light_gray
        this.renderString(graphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

}
