package com.llyycci.create_tweaked_controllers;

import com.llyycci.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;

import com.llyycci.create_tweaked_controllers.gui.ModMainConfigButton;
import com.llyycci.create_tweaked_controllers.input.MouseCursorHandler;

import com.simibubi.create.infrastructure.gui.OpenCreateMenuButton;

import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ModClientEvents {

	public static void onTickStart(Minecraft client){
		TweakedLinkedControllerClientHandler.tick();
	}
	public static void onRenderTick() {
		if (!isGameActive())
			return;
		MouseCursorHandler.CancelPlayerTurn();
	}
	protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}
	public static void register() {
		ClientTickEvents.START_CLIENT_TICK.register(ModClientEvents::onTickStart);
		RenderTickStartCallback.EVENT.register(ModClientEvents::onRenderTick);
		// we need to add our config button after mod menu, so we register our event with a phase that comes later
		//从ClientEvents抄的，可能有bug↓
		ResourceLocation latePhase = Create_Tweaked_Controllers.asResource("late");
		ScreenEvents.AFTER_INIT.addPhaseOrdering(Event.DEFAULT_PHASE, latePhase);
		ScreenEvents.AFTER_INIT.register(latePhase, ModMainConfigButton.OpenConfigButtonHandler::onGuiInit);
	}


}
