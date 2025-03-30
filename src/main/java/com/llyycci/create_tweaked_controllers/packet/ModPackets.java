package com.llyycci.create_tweaked_controllers.packet;

import static com.simibubi.create.foundation.networking.SimplePacketBase.NetworkDirection.PLAY_TO_SERVER;

import com.llyycci.create_tweaked_controllers.packet.TweakedLinkedControllerButtonPacket;
import com.llyycci.create_tweaked_controllers.packet.TweakedLinkedControllerAxisPacket;
import com.llyycci.create_tweaked_controllers.packet.TweakedLinkedControllerBindPacket;
import com.llyycci.create_tweaked_controllers.packet.TweakedLinkedControllerStopLecternPacket;
import com.llyycci.create_tweaked_controllers.Create_Tweaked_Controllers;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public enum ModPackets {
	TWEAKED_LINKED_CONTROLLER_INPUT(TweakedLinkedControllerButtonPacket.class, TweakedLinkedControllerButtonPacket::new, PLAY_TO_SERVER),
	TWEAKED_LINKED_CONTROLLER_INPUT_AXIS(TweakedLinkedControllerAxisPacket.class, TweakedLinkedControllerAxisPacket::new, PLAY_TO_SERVER),
	TWEAKED_LINKED_CONTROLLER_BIND(TweakedLinkedControllerBindPacket.class, TweakedLinkedControllerBindPacket::new, PLAY_TO_SERVER),
	TWEAKED_LINKED_CONTROLLER_USE_LECTERN(TweakedLinkedControllerStopLecternPacket.class, TweakedLinkedControllerStopLecternPacket::new, PLAY_TO_SERVER),
	;
	public static final ResourceLocation CHANNEL_NAME = Create_Tweaked_Controllers.asResource("main");
	public static final int NETWORK_VERSION = 3;
	public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
	private static SimpleChannel channel;

	private ModPackets.PacketType<?> packetType;

	<T extends SimplePacketBase> ModPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
											SimplePacketBase.NetworkDirection direction) {
		packetType = new ModPackets.PacketType<>(type, factory, direction);
	}

	public static void registerPackets() {
		channel = new SimpleChannel(CHANNEL_NAME);
		for (ModPackets packet : values())
			packet.packetType.register();
	}

	public static SimpleChannel getChannel() {
		return channel;
	}

	public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
		getChannel().sendToClientsAround((S2CPacket) message, (ServerLevel) world, pos, range);
	}

	private static class PacketType<T extends SimplePacketBase> {
		private static int index = 0;

		private Function<FriendlyByteBuf, T> decoder;
		private Class<T> type;
		private SimplePacketBase.NetworkDirection direction;

		private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, SimplePacketBase.NetworkDirection direction) {
			decoder = factory;
			this.type = type;
			this.direction = direction;
		}

		private void register() {
			switch (direction) {
				case PLAY_TO_CLIENT -> getChannel().registerS2CPacket(type, index++, decoder);
				case PLAY_TO_SERVER -> getChannel().registerC2SPacket(type, index++, decoder);
			}
		}
	}

}
