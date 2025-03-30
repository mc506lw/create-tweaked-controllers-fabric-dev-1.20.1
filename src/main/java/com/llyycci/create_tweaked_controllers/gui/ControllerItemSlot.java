package com.llyycci.create_tweaked_controllers.gui;

import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ControllerItemSlot extends SlotItemHandler {
	private boolean active = true;

	public ControllerItemSlot(SlottedStackStorage storage, int index,
							  int x, int y) {
		super(storage, index, x, y);
	}

	public ControllerItemSlot(SlottedStackStorage storage, int index,
							  int x, int y, boolean active) {
		super(storage, index, x, y);
		this.active = active;
	}
	/*
	// Fabric 需要覆盖可用性检查
	@Override
	public boolean canInsert(ItemStack stack) {
		return active && super.canInsert(stack);
	}*/

	@Override
	public boolean isActive() {
		return active;
	}

	public void SetActive(boolean active) {
		this.active = active;
	}
}
