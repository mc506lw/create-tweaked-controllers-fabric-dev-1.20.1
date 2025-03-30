package com.llyycci.create_tweaked_controllers;

import com.llyycci.create_tweaked_controllers.controller.TweakedLinkedControllerServerHandler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ModCommonEvents {
	public static void onServerWorldTick(Level world) {
		if (!world.isClientSide()) {
			TweakedLinkedControllerServerHandler.tick(world);
		}
	}
	public static void onEntityAdded(Entity entity, Level world) {
		if (entity instanceof Player){
			Player player = (Player)entity;
			if (player.getCustomData().contains("IsUsingLecternController"))
			{
				player.getCustomData().remove("IsUsingLecternController");
			}
		}
	}
	public static void register() {
		ServerTickEvents.END_WORLD_TICK.register(ModCommonEvents::onServerWorldTick);
		ServerEntityEvents.ENTITY_LOAD.register(ModCommonEvents::onEntityAdded);
	}
}
