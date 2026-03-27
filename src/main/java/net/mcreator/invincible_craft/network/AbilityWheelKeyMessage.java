package net.mcreator.invincible_craft.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.invincible_craft.InvincibleCraftMod;

@EventBusSubscriber
public record AbilityWheelKeyMessage(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<AbilityWheelKeyMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(InvincibleCraftMod.MODID, "key_ability_wheel_key"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AbilityWheelKeyMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, AbilityWheelKeyMessage message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new AbilityWheelKeyMessage(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<AbilityWheelKeyMessage> type() {
		return TYPE;
	}

	public static void handleData(final AbilityWheelKeyMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		InvincibleCraftMod.addNetworkMessage(AbilityWheelKeyMessage.TYPE, AbilityWheelKeyMessage.STREAM_CODEC, AbilityWheelKeyMessage::handleData);
	}
}