package com.llyycci.create_tweaked_controllers.block;

import com.llyycci.create_tweaked_controllers.compat.ComputerCraft.ModComputerCraftProxy;
import com.llyycci.create_tweaked_controllers.compat.ComputerCraft.TweakedLecternPeripheral;
import com.llyycci.create_tweaked_controllers.controller.ControllerRedstoneOutput;
import com.llyycci.create_tweaked_controllers.controller.TweakedLinkedControllerClientHandler;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.utility.fabric.ReachUtil;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class TweakedLecternControllerBlockEntity extends SmartBlockEntity {
	//private CompoundTag controllerNbt = new CompoundTag();
	private ItemStack controller;
	private UUID user;
	private UUID prevUser;    // used only on client
	private boolean deactivatedThisTick;    // used only on server
	private boolean useFullPrecision = false;
	private ControllerRedstoneOutput output;
	private float[] axis;
	private TweakedLecternPeripheral peripheral = null;

	public AbstractComputerBehaviour computerBehaviour;

	public TweakedLecternControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		output = new ControllerRedstoneOutput();
		output.DecodeAxis(0);
		output.DecodeButtons((short)0);
		axis = new float[6];
		for (byte i = 0; i < 6; i++) {
			axis[i] = 0;
		}
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(computerBehaviour = ModComputerCraftProxy.behaviour(this));
	}

	@Override//数据持久化保存：方块实体状态写入nbt
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putBoolean("UseFullPrecision", useFullPrecision);//存储是否使用高精度
		if (controller == null) {
			controller = new ItemStack(Blocks.AIR, 0);
		}
		compound.put("Controller", controller.save(new CompoundTag()));
		if (user != null)
			compound.putUUID("User", user);
	}

	@Override
	public void writeSafe(CompoundTag compound) {
		super.writeSafe(compound);
		compound.putBoolean("UseFullPrecision", useFullPrecision);
		compound.put("Controller", controller.save(new CompoundTag()));
	}

	@Override//读取nbt获取方块实体状态
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		useFullPrecision = compound.getBoolean("UseFullPrecision");
		controller = ItemStack.of(compound.getCompound("Controller"));
		user = compound.hasUUID("User") ? compound.getUUID("User") : null;
	}

	public void AssignPeripheral(TweakedLecternPeripheral p) {
		peripheral = p;
	}//注册CC电脑外设

	public void ReceiveButtonStates(short value)
	{
		output.DecodeButtons(value);
	}

	public boolean GetButton(int index)
	{
		return output.buttons[index];
	}

	public void ReceiveAxisStates(int value)
	{
		output.DecodeAxis(value);
	}

	public void ReceiveFullStates(float[] value) {
		for (byte i = 0; i < 6; i++) {
			axis[i] = value[i];
		}
	}

	public float GetAxis(int index) {
		if (useFullPrecision) {
			return axis[index];
		} else {
			Byte input = output.axis[index];
			float result;
			if ((input & 0x10) != 0) {
				result = -(input & 0x0f) / 15.0f;
			} else {
				result = input / 15.0f;
			}
			return result;
		}
	}

	public ItemStack getController()
	{
		return controller;
	}

	public boolean hasUser()
	{
		return user != null;
	}

	public UUID getUserUUID()
	{
		return user;
	}

	public void SetFullPrecision(boolean value) {
		if (useFullPrecision == value) return;
		useFullPrecision = value;
		sendData();
	}

	public boolean shouldUseFullPrecision()
	{
		return useFullPrecision;
	}

	public boolean isUsedBy(Player player)
	{
		return hasUser() && user.equals(player.getUUID());
	}

	public void tryStartUsing(Player player) {
		if (!deactivatedThisTick && !hasUser() && !playerIsUsingLectern(player) && playerInRange(player, level, worldPosition))
			startUsing(player);
	}

	public void tryStopUsing(Player player) {
		if (isUsedBy(player))
			stopUsing(player);
	}

	private void startUsing(Player player) {
		if (peripheral != null) {
			peripheral.NotifyUseEvent(true, player);
		}
		user = player.getUUID();
		player.getCustomData().putBoolean("IsUsingLecternController", true);
		sendData();
	}

	private void stopUsing(Player player) {
		if (peripheral != null) {
			peripheral.NotifyUseEvent(false, player);
		}
		user = null;
		if (player != null)
			player.getCustomData().remove("IsUsingLecternController");
		deactivatedThisTick = true;
		sendData();
	}

	public static boolean playerIsUsingLectern(Player player) {
		return player.getCustomData().contains("IsUsingLecternController");
	}

	@Override
	public void tick() {
		super.tick();

		if (level.isClientSide) {
			EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::tryToggleActive);
			prevUser = user;
		}

		if (!level.isClientSide) {
			deactivatedThisTick = false;

			if (!(level instanceof ServerLevel))
				return;
			if (user == null)
				return;

			Entity entity = ((ServerLevel) level).getEntity(user);
			if (!(entity instanceof Player player)) {
				stopUsing(null);
				return;
			}

			if (!playerInRange(player, level, worldPosition) || !playerIsUsingLectern(player))
				stopUsing(player);
		}
	}

	@Environment(EnvType.CLIENT)
	private void tryToggleActive() {
		if (user == null && Minecraft.getInstance().player.getUUID().equals(prevUser)) {
			TweakedLinkedControllerClientHandler.deactivateInLectern();
		} else if (prevUser == null && Minecraft.getInstance().player.getUUID().equals(user)) {
			TweakedLinkedControllerClientHandler.activateInLectern(worldPosition);
		}
	}
	//放置遥控器
	public void setController(ItemStack newController) {
		controller = newController;
		if (newController != null) {
			AllSoundEvents.CONTROLLER_PUT.playOnServer(level, worldPosition);
		}
	}

	//交换遥控器
	public void swapControllers(ItemStack stack, Player player, InteractionHand hand, BlockState state) {
		ItemStack newController = stack.copy();
		stack.setCount(0);
		if (player.getItemInHand(hand).isEmpty()) {
			player.setItemInHand(hand, controller);
		} else {
			dropController(state);
		}
		setController(newController);
	}

	//丢弃遥控器
	public void dropController(BlockState state) {
		Entity playerEntity = ((ServerLevel) level).getEntity(user);
		if (playerEntity instanceof Player)
			stopUsing((Player) playerEntity);

		Direction dir = state.getValue(TweakedLecternControllerBlock.FACING);
		double x = worldPosition.getX() + 0.5 + 0.25*dir.getStepX();
		double y = worldPosition.getY() + 1;
		double z = worldPosition.getZ() + 0.5 + 0.25*dir.getStepZ();
		ItemEntity itementity = new ItemEntity(level, x, y, z, controller.copy());
		itementity.setDefaultPickUpDelay();
		level.addFreshEntity(itementity);
		controller = null;
	}

	//判断玩家是否在范围内
	public static boolean playerInRange(Player player, Level world, BlockPos pos) {
		//double modifier = world.isRemote ? 0 : 1.0;
		double reach = 0.4 * ReachUtil.reach(player);// + modifier;
		return player.distanceToSqr(Vec3.atCenterOf(pos)) < reach * reach;
	}

	//getCapability 暂未实现
}
