package net.mcreator.invincible_craft.procedures;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;
import net.mcreator.invincible_craft.init.InvincibleCraftModMobEffects;
import net.mcreator.invincible_craft.InvincibleCraftMod;

import javax.annotation.Nullable;

@EventBusSubscriber(Dist.CLIENT)
public class WhenEntitySwingsHandProcedure {
	@SubscribeEvent
	public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
		PacketDistributor.sendToServer(new WhenEntitySwingsHandMessage());
		execute(event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getEntity());
	}

	@EventBusSubscriber
	public record WhenEntitySwingsHandMessage() implements CustomPacketPayload {
		public static final Type<WhenEntitySwingsHandMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(InvincibleCraftMod.MODID, "procedure_when_entity_swings_hand"));
		public static final StreamCodec<RegistryFriendlyByteBuf, WhenEntitySwingsHandMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, WhenEntitySwingsHandMessage message) -> {
		}, (RegistryFriendlyByteBuf buffer) -> new WhenEntitySwingsHandMessage());

		@Override
		public Type<WhenEntitySwingsHandMessage> type() {
			return TYPE;
		}

		public static void handleData(final WhenEntitySwingsHandMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.SERVERBOUND) {
				context.enqueueWork(() -> {
					if (!context.player().level().hasChunkAt(context.player().blockPosition()))
						return;
					execute(context.player().level(), context.player().getX(), context.player().getY(), context.player().getZ(), context.player());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}

		@SubscribeEvent
		public static void registerMessage(FMLCommonSetupEvent event) {
			InvincibleCraftMod.addNetworkMessage(WhenEntitySwingsHandMessage.TYPE, WhenEntitySwingsHandMessage.STREAM_CODEC, WhenEntitySwingsHandMessage::handleData);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).battleMode && !(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(InvincibleCraftModMobEffects.STUN))
				&& !(entity instanceof LivingEntity _livEnt1 && _livEnt1.hasEffect(InvincibleCraftModMobEffects.COMBAT_COOLDOWN))) {
			AttackPunchProcedure.execute(world, x, y, z, entity);
		}
	}
}