package com.llyycci.create_tweaked_controllers.input;

import com.llyycci.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;
import com.simibubi.create.foundation.events.InputEvents;

import io.github.fabricators_of_create.porting_lib.event.client.InteractEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.HitResult;

public class ModInputEvents{

	public static InteractionResult onUse(Minecraft mc, HitResult hit, InteractionHand hand){
		TweakedLinkedControllerClientHandler.deactivateInLectern();
		return InteractionResult.SUCCESS;
	}

	public static void register(){
		InteractEvents.USE.register(InputEvents::onUse);
	}
}
