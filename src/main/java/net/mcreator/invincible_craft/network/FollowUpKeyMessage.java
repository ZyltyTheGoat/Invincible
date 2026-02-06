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

import net.mcreator.invincible_craft.procedures.FollowUpKeyOnKeyReleasedProcedure;
import net.mcreator.invincible_craft.procedures.FollowUpKeyOnKeyPressedProcedure;
import net.mcreator.invincible_craft.InvincibleCraftMod;

@EventBusSubscriber
public record FollowUpKeyMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<FollowUpKeyMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(InvincibleCraftMod.MODID, "key_follow_up_key"));
	public static final StreamCodec<RegistryFriendlyByteBuf, FollowUpKeyMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, FollowUpKeyMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new FollowUpKeyMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<FollowUpKeyMessage> type() {
		return TYPE;
	}

	public static void handleData(final FollowUpKeyMessage message, final IPayloadContext context) {
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

			FollowUpKeyOnKeyPressedProcedure.execute(entity);
		}
		if (type == 1) {

			FollowUpKeyOnKeyReleasedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		InvincibleCraftMod.addNetworkMessage(FollowUpKeyMessage.TYPE, FollowUpKeyMessage.STREAM_CODEC, FollowUpKeyMessage::handleData);
	}
}