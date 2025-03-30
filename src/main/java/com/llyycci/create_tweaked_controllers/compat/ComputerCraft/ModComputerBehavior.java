package com.llyycci.create_tweaked_controllers.compat.ComputerCraft;

import com.llyycci.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.simibubi.create.compat.computercraft.AbstractComputerBehaviour;
import com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import dan200.computercraft.api.peripheral.IPeripheral;

import org.jetbrains.annotations.Nullable;

public class ModComputerBehavior extends AbstractComputerBehaviour {
	@Nullable
	public static IPeripheral peripheralProvider(Level level, BlockPos blockPos) {
		AbstractComputerBehaviour behavior = BlockEntityBehaviour.get(level, blockPos, AbstractComputerBehaviour.TYPE);
		if (behavior instanceof ComputerBehaviour real)
			return real.getPeripheral();
		return null;
	}

	IPeripheral peripheral;

	public ModComputerBehavior(SmartBlockEntity te) {
		super(te);
		this.peripheral = getPeripheralFor(te);
	}
	public static IPeripheral getPeripheralFor(SmartBlockEntity be) {
		if (be instanceof TweakedLecternControllerBlockEntity tlcbe)
			return new TweakedLecternPeripheral(tlcbe);

		throw new IllegalArgumentException("No peripheral available for " + be.getType()
				.getClass().getName());
	}
	@Override
	public <T> T getPeripheral() {
		//noinspection unchecked
		return (T) peripheral;
	}
}
