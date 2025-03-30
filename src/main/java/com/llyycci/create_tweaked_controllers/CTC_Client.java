package com.llyycci.create_tweaked_controllers;

import com.llyycci.create_tweaked_controllers.compat.Controllable.ControllerHandler;
import com.llyycci.create_tweaked_controllers.compat.Mods;
import com.llyycci.create_tweaked_controllers.config.ModKeyMappings;
import com.llyycci.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;
import com.llyycci.create_tweaked_controllers.input.ModInputEvents;
import com.llyycci.create_tweaked_controllers.input.MouseCursorHandler;
import com.mojang.blaze3d.platform.Window;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

public class CTC_Client implements ClientModInitializer {
	//初始化客户端
	@Override
	public void onInitializeClient() {

		registerOverlays();
		ModInputEvents.register();//注册ModInputEvents
		ModKeyMappings.register();//注册ModKeyMappings(可能有bug，需要多加关注)
		Mods.CONTROLLABLE.executeIfInstalled(() -> CTC_Client::ControllableInit);//如果Controllable已加载，则执行注册
		MouseCursorHandler.InitValues();//初始化鼠标位置
		ModClientEvents.register();//注册ModClientEvents
	}
	private static void registerOverlays() {
		HudRenderCallback.EVENT.register((graphics, partialTicks) -> {
			Window window = Minecraft.getInstance().getWindow();

			TweakedLinkedControllerClientHandler.renderOverlay(graphics, partialTicks, window);
		});
	}

	private static void ControllableInit() {
		ControllerHandler.Register();
	}
}
