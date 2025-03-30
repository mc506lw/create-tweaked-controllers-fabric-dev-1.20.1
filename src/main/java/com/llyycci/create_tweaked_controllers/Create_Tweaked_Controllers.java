package com.llyycci.create_tweaked_controllers;

import com.llyycci.create_tweaked_controllers.block.ModBlocks;
import com.llyycci.create_tweaked_controllers.compat.ComputerCraft.ModComputerCraftProxy;
import com.llyycci.create_tweaked_controllers.config.ModConfigs;
import com.llyycci.create_tweaked_controllers.input.ModInputEvents;
import com.llyycci.create_tweaked_controllers.item.ModItems;
import com.llyycci.create_tweaked_controllers.tabs.CTC_CreativeTabs;
import com.simibubi.create.Create;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Create_Tweaked_Controllers implements ModInitializer {
	public static final String ID = "create_tweaked_controllers";
	public static final String NAME = "Create_Tweaked_Controllers";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);
	@Override
	public void onInitialize() {
		LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", NAME, Create.VERSION);
		LOGGER.info(EnvExecutor.unsafeRunForDist(
				() -> () -> "{} is accessing Porting Lib from the client!",
				() -> () -> "{} is accessing Porting Lib from the server!"
		), NAME);

		CTC_CreativeTabs.register();
		ModItems.register();
		ModBlocks.register();
		ModBlockEntityTypes.register();

		REGISTRATE.register();
		ModConfigs.register();
		ModComputerCraftProxy.register();
		ModCommonEvents.register();

	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(ID, path);
	}
	public static CreateRegistrate registrate()
	{
		return REGISTRATE;
	}
	public static MutableComponent translateDirect(String key, Object... args) {
		return Components.translatable(Create_Tweaked_Controllers.ID + "." + key, Lang.resolveBuilders(args));
	}

	public static MutableComponent translateDirectRaw(String key, Object... args) {
		return Components.translatable(key, Lang.resolveBuilders(args));
	}

	public static LangBuilder builder()
	{
		return new LangBuilder(Create_Tweaked_Controllers.ID);
	}
	public static LangBuilder translate(String langKey, Object... args)
	{
		return builder().translate(langKey, args);
	}
	public static void log(String message)
	{
		Create.LOGGER.info(message);
	}
	public static void error(String message)
	{
		Create.LOGGER.error(message);
	}
}
