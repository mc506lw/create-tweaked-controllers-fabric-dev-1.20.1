package com.llyycci.create_tweaked_controllers.block;

import com.llyycci.create_tweaked_controllers.item.ModItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;

import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.llyycci.create_tweaked_controllers.ModBlockEntityTypes;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TweakedLecternControllerBlock extends LecternBlock
	implements IBE<TweakedLecternControllerBlockEntity>, ISpecialBlockItemRequirement, BlockPickInteractionAware {
	//注意，0.5.1版本所用的ISpecialBlockItemRequirement在6.0.0版本中变更为SpecialBlockItemRequirement
	public TweakedLecternControllerBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(HAS_BOOK, true));
	}

	@Override//重写获取方块实体类
	public Class<TweakedLecternControllerBlockEntity> getBlockEntityClass() {
		return TweakedLecternControllerBlockEntity.class;
	}

	@Override//重写获取方块实体类型
	public BlockEntityType<? extends TweakedLecternControllerBlockEntity> getBlockEntityType() {
		return ModBlockEntityTypes.TWEAKED_LECTERN_CONTROLLER.get();
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153573_, BlockState p_153574_) {
		return IBE.super.newBlockEntity(p_153573_, p_153574_);
	}

	@Override//重写方块交互:右键使用讲台上的遥控器/按shift+右键取下遥控器
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
								 BlockHitResult hit) {
		if (!player.isShiftKeyDown() && TweakedLecternControllerBlockEntity.playerInRange(player, world, pos)) {
			if (!world.isClientSide)
				withBlockEntityDo(world, pos, be -> be.tryStartUsing(player));
			return InteractionResult.SUCCESS;
		}

		if (player.isShiftKeyDown()) {
			if (!world.isClientSide)
				replaceWithLectern(state, world, pos);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (!world.isClientSide)
				withBlockEntityDo(world, pos, be -> be.dropController(state));

			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return 15;
	}

	public void replaceLectern(BlockState lecternState, Level world, BlockPos pos, ItemStack controller) {
		world.setBlockAndUpdate(pos, defaultBlockState().setValue(FACING, lecternState.getValue(FACING))
				.setValue(POWERED, lecternState.getValue(POWERED)));
		withBlockEntityDo(world, pos, be -> be.setController(controller));
	}

	public void replaceWithLectern(BlockState state, Level world, BlockPos pos) {
		AllSoundEvents.CONTROLLER_TAKE.playOnServer(world, pos);
		world.setBlockAndUpdate(pos, Blocks.LECTERN.defaultBlockState()
				.setValue(FACING, state.getValue(FACING))
				.setValue(POWERED, state.getValue(POWERED)));
	}

	@Override
	public ItemStack getPickedStack(BlockState state, BlockGetter view, BlockPos pos, @Nullable Player player, @Nullable HitResult result) {
		return Blocks.LECTERN.getCloneItemStack(view, pos, state);
	}

	@Override
	public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
		ArrayList<ItemStack> requiredItems = new ArrayList<>();
		requiredItems.add(new ItemStack(Blocks.LECTERN));
		requiredItems.add(new ItemStack(ModItems.TWEAKED_LINKED_CONTROLLER.get()));
		return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, requiredItems);
	}
}
