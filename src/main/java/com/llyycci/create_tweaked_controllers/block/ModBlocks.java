package com.llyycci.create_tweaked_controllers.block;

import com.llyycci.create_tweaked_controllers.Create_Tweaked_Controllers;
import com.llyycci.create_tweaked_controllers.tabs.CTC_CreativeTabs;
import com.simibubi.create.content.redstone.link.controller.LecternControllerBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;

import net.minecraft.world.level.block.Blocks;


import static com.simibubi.create.foundation.data.TagGen.axeOnly;

public class ModBlocks {
	private static final CreateRegistrate REGISTRATE = Create_Tweaked_Controllers.registrate();

	static {
		REGISTRATE.setCreativeTab(CTC_CreativeTabs.BASE.key());
	}

	public static final BlockEntry<TweakedLecternControllerBlock> TWEAKED_LECTERN_CONTROLLER =
			REGISTRATE.block("tweaked_lectern_controller", TweakedLecternControllerBlock::new)
					.initialProperties(() -> Blocks.LECTERN)
					.transform(axeOnly())
					.blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
							.getExistingFile(p.mcLoc("block/lectern"))))
					.loot((lt, block) -> lt.dropOther(block, Blocks.LECTERN))
					.register();
	public static void register() {}
}
