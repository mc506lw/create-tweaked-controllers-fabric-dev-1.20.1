package com.llyycci.create_tweaked_controllers.packet;

import com.llyycci.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.llyycci.create_tweaked_controllers.item.TweakedLinkedControllerItem;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class TweakedLinkedControllerBindPacket extends TweakedLinkedControllerPacketBase {

    private int button;
    private BlockPos linkLocation;

    public TweakedLinkedControllerBindPacket(int button, BlockPos linkLocation) {
        super((BlockPos) null);
        this.button = button;
        this.linkLocation = linkLocation;
    }

    public TweakedLinkedControllerBindPacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.button = buffer.readVarInt();
        this.linkLocation = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        buffer.writeVarInt(button);
        buffer.writeBlockPos(linkLocation);
    }

    @Override
    protected void handleItem(ServerPlayer player, ItemStack heldItem) {
        if (player.isSpectator())
            return;

        ItemStackHandler frequencyItems = TweakedLinkedControllerItem.getFrequencyItems(heldItem);
        LinkBehaviour linkBehaviour = BlockEntityBehaviour.get(player.level(), linkLocation, LinkBehaviour.TYPE);
        if (linkBehaviour == null)
            return;

        linkBehaviour.getNetworkKey()
            .forEachWithContext((f, first) -> frequencyItems.setStackInSlot(button * 2 + (first ? 0 : 1), f.getStack()
                .copy()));

        heldItem.getTag()
            .put("Items", frequencyItems.serializeNBT());
    }

    @Override
    protected void handleLectern(ServerPlayer player, TweakedLecternControllerBlockEntity lectern) {}

}
