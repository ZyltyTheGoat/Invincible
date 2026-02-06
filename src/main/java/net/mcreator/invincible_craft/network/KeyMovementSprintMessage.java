package net.mcreator.invincible_craft.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.invincible_craft.procedures.KeyMovementSprintOnKeyReleasedProcedure;
import net.mcreator.invincible_craft.procedures.KeyMovementSprintOnKeyPressedProcedure;
import net.mcreator.invincible_craft.InvincibleCraftMod;

@EventBusSubscriber
public record KeyMovementSprintMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<KeyMovementSprintMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(InvincibleCraftMod.MODID, "key_key_movement_sprint"));
	public static final StreamCodec<RegistryFriendlyByteBuf, KeyMovementSprintMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, KeyMovementSprintMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new KeyMovementSprintMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<KeyMovementSprintMessage> type() {
		return TYPE;
	}

	public static void handleData(final KeyMovementSprintMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				pressAction(context.player(), message.eventType, message.pressedms);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {

			KeyMovementSprintOnKeyPressedProcedure.execute(world, x, y, z, entity);
		}
		if (type == 1) {

			KeyMovementSprintOnKeyReleasedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		InvincibleCraftMod.addNetworkMessage(KeyMovementSprintMessage.TYPE, KeyMovementSprintMessage.STREAM_CODEC, KeyMovementSprintMessage::handleData);
	}
}