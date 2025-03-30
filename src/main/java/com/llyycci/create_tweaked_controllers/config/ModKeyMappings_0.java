package com.llyycci.create_tweaked_controllers.config;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.util.Tuple;

public class ModKeyMappings_0 implements ClientModInitializer {
	// KeyMapping 实例声明（Parchment 映射规范）
	public static KeyMapping KEY_MOUSE_FOCUS;
	public static KeyMapping KEY_MOUSE_RESET;
	public static KeyMapping KEY_CONTROLLER_EXIT;

	@Override
	public void onInitializeClient() {
		// 使用 Tuple 存储键位配置（键名，默认按键）
		Tuple[] bindings = {
				new Tuple<>("mouse_focus", GLFW.GLFW_KEY_LEFT_ALT),
				new Tuple<>("mouse_reset", GLFW.GLFW_KEY_R),
				new Tuple<>("controller_exit", GLFW.GLFW_KEY_TAB)
		};

		// 批量注册键位
		for (Tuple<String, Integer> binding : bindings) {
			registerKey(binding.getA(), binding.getB());
		}
	}

	private static void registerKey(String name, int glfwKey) {
		KeyMapping key = new KeyMapping(
				"key.create_tweaked_controllers." + name, // 本地化键
				InputConstants.Type.KEYSYM,                // 输入类型（键盘符号）
				glfwKey,                                   // GLFW 键值
				"category.create_tweaked_controllers"      // 分类名称
		);

		KeyBindingHelper.registerKeyBinding(key); // Fabric 注册方法

		// 根据名称分配实例
		switch (name) {
			case "mouse_focus" -> KEY_MOUSE_FOCUS = key;
			case "mouse_reset" -> KEY_MOUSE_RESET = key;
			case "controller_exit" -> KEY_CONTROLLER_EXIT = key;
		}
	}
}
