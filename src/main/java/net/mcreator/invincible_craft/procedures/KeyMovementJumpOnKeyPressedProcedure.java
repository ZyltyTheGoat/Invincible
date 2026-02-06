package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.Minecraft;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

public class KeyMovementJumpOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String power = "";
		{
			InvincibleCraftModVariables.PlayerVariables _vars = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
			_vars.movementJump = true;
			_vars.markSyncDirty();
		}
		if (getEntityGameType(entity) == GameType.SURVIVAL || getEntityGameType(entity) == GameType.ADVENTURE) {
			if (!entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).flightMode) {
				if (entity.getXRot() >= -90 && entity.getXRot() <= -40 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementSprint) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("invincible_craft:boost")), SoundSource.NEUTRAL, 2, 1);
						} else {
							_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("invincible_craft:boost")), SoundSource.NEUTRAL, 2, 1, false);
						}
					}
					entity.setDeltaMovement(new Vec3((entity.getLookAngle().x * 1.5), (entity.getLookAngle().y * 1.5), (entity.getLookAngle().z * 1.5)));
					{
						InvincibleCraftModVariables.PlayerVariables _vars = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
						_vars.flightMode = true;
						_vars.markSyncDirty();
					}
				} else if (!entity.onGround()) {
					{
						InvincibleCraftModVariables.PlayerVariables _vars = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
						_vars.flightMode = true;
						_vars.markSyncDirty();
					}
				}
			}
		}
	}

	private static GameType getEntityGameType(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer();
		} else if (entity instanceof Player player && player.level().isClientSide()) {
			PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
			if (playerInfo != null)
				return playerInfo.getGameMode();
		}
		return null;
	}
}