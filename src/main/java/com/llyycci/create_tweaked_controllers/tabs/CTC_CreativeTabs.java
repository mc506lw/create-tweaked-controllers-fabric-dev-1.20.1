package com.llyycci.create_tweaked_controllers.tabs;

import com.llyycci.create_tweaked_controllers.Create_Tweaked_Controllers;
import com.simibubi.create.AllCreativeModeTabs;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class CTC_CreativeTabs {
	public static final AllCreativeModeTabs.TabInfo BASE = register("create_tweaked_controllers",
			() -> FabricItemGroup.builder()
					.title(Component.translatable("itemGroup.create_tweaked_controllers.base"))
					.icon(() -> AllCreativeModeTabs.BASE_CREATIVE_TAB.tab().getIconItem())//这一行还要修改
					.build());
	private static AllCreativeModeTabs.TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
		ResourceLocation id = Create_Tweaked_Controllers.asResource(name);
		ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
		CreativeModeTab tab = supplier.get();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
		return new AllCreativeModeTabs.TabInfo(key, tab);
	}
	public static void register() {}
}
