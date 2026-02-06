package net.mcreator.invincible_craft.network;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import net.mcreator.invincible_craft.InvincibleCraftMod;

import java.util.function.Supplier;

@EventBusSubscriber
public class InvincibleCraftModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, InvincibleCraftMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		InvincibleCraftMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
	}

	@SubscribeEvent
	public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
	}

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player)
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
	}

	@SubscribeEvent
	public static void onPlayerTickUpdateSyncPlayerVariables(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player && player.getData(PLAYER_VARIABLES)._syncDirty) {
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
			player.getData(PLAYER_VARIABLES)._syncDirty = false;
		}
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {
		PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
		PlayerVariables clone = new PlayerVariables();
		clone.power = original.power;
		clone.rank = original.rank;
		clone.fame = original.fame;
		clone.flightSpeed = original.flightSpeed;
		if (!event.isWasDeath()) {
			clone.battleMode = original.battleMode;
			clone.flightMode = original.flightMode;
			clone.flightJumpCounter = original.flightJumpCounter;
			clone.movementStrafeLeft = original.movementStrafeLeft;
			clone.movementStrafeRight = original.movementStrafeRight;
			clone.movementWalkBackwards = original.movementWalkBackwards;
			clone.movementWalkForwards = original.movementWalkForwards;
			clone.movementJump = original.movementJump;
			clone.movementSprint = original.movementSprint;
		}
		event.getEntity().setData(PLAYER_VARIABLES, clone);
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		boolean _syncDirty = false;
		public String power = "\"\"";
		public String rank = "\"\"";
		public double fame = 0;
		public boolean battleMode = false;
		public boolean flightMode = false;
		public double flightJumpCounter = 0;
		public boolean movementStrafeLeft = false;
		public boolean movementStrafeRight = false;
		public boolean movementWalkBackwards = false;
		public boolean movementWalkForwards = false;
		public boolean movementJump = false;
		public double flightSpeed = 0;
		public boolean movementSprint = false;

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putString("power", power);
			nbt.putString("rank", rank);
			nbt.putDouble("fame", fame);
			nbt.putBoolean("battleMode", battleMode);
			nbt.putBoolean("flightMode", flightMode);
			nbt.putDouble("flightJumpCounter", flightJumpCounter);
			nbt.putBoolean("movementStrafeLeft", movementStrafeLeft);
			nbt.putBoolean("movementStrafeRight", movementStrafeRight);
			nbt.putBoolean("movementWalkBackwards", movementWalkBackwards);
			nbt.putBoolean("movementWalkForwards", movementWalkForwards);
			nbt.putBoolean("movementJump", movementJump);
			nbt.putDouble("flightSpeed", flightSpeed);
			nbt.putBoolean("movementSprint", movementSprint);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			power = nbt.getString("power");
			rank = nbt.getString("rank");
			fame = nbt.getDouble("fame");
			battleMode = nbt.getBoolean("battleMode");
			flightMode = nbt.getBoolean("flightMode");
			flightJumpCounter = nbt.getDouble("flightJumpCounter");
			movementStrafeLeft = nbt.getBoolean("movementStrafeLeft");
			movementStrafeRight = nbt.getBoolean("movementStrafeRight");
			movementWalkBackwards = nbt.getBoolean("movementWalkBackwards");
			movementWalkForwards = nbt.getBoolean("movementWalkForwards");
			movementJump = nbt.getBoolean("movementJump");
			flightSpeed = nbt.getDouble("flightSpeed");
			movementSprint = nbt.getBoolean("movementSprint");
		}

		public void markSyncDirty() {
			_syncDirty = true;
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data, int player) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(InvincibleCraftMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> {
			buffer.writeInt(message.player());
			buffer.writeNbt(message.data().serializeNBT(buffer.registryAccess()));
		}, (RegistryFriendlyByteBuf buffer) -> {
			PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables(), buffer.readInt());
			message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
			return message;
		});

		@Override
		public Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					Entity player = context.player().level().getEntity(message.player);
					if (player == null)
						return;
					player.getData(PLAYER_VARIABLES).deserializeNBT(context.player().registryAccess(), message.data.serializeNBT(context.player().registryAccess()));
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}