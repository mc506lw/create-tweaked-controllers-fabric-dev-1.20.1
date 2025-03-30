package com.llyycci.create_tweaked_controllers.input;

import com.llyycci.create_tweaked_controllers.Create_Tweaked_Controllers;
import com.llyycci.create_tweaked_controllers.gui.InputConfig.GenericInputScreen;
import com.llyycci.create_tweaked_controllers.gui.InputConfig.KeyboardInputScreen;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.AllKeys;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import org.lwjgl.glfw.GLFW;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KeyboardInput implements GenericInput {
    public int key = GLFW.GLFW_KEY_UNKNOWN;
    public boolean invertValue = false;

    public KeyboardInput(int key)
    {
        this.key = key;
    }

    public KeyboardInput() {}

    @Override
    public boolean GetButtonValue() {
        if (!IsInputValid()) return invertValue;
        return invertValue ? !AllKeys.isKeyDown(key): AllKeys.isKeyDown(key);
    }

    @Override
    public float GetAxisValue()
    {
        return GetButtonValue() ? 1.0f : 0.0f;
    }

    @Override
    public MutableComponent GetDisplayName() {
        return key >= 0 ? InputConstants.getKey(key, 0).getDisplayName().plainCopy() : Create_Tweaked_Controllers.translateDirectRaw("key.keyboard.unknown");
    }

    @Override
    public boolean IsInputValid()
    {
        return key != GLFW.GLFW_KEY_UNKNOWN;
    }

    @Override
    public void Serialize(DataOutputStream buf) throws IOException {
        buf.writeBoolean(invertValue);
        buf.writeInt(key);
    }

    @Override
    public void Deserialize(DataInputStream buf) throws IOException {
        invertValue = buf.readBoolean();
        key = buf.readInt();
    }

    @Override
    public InputType GetType()
    {
        return InputType.KEYBOARD_KEY;
    }

    @Override
    public int GetValue()
    {
        return key;
    }

    @Override
    public GenericInputScreen OpenConfigScreen(Screen previous, Component comp) {
        return new KeyboardInputScreen(previous, comp, this);
    }

}
