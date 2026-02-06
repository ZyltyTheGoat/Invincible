package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

public class KeyMovementSprintOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			InvincibleCraftModVariables.PlayerVariables _vars = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
			_vars.movementSprint = true;
			_vars.markSyncDirty();
		}
		if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).flightMode && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementWalkForwards) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("invincible_craft:boost")), SoundSource.NEUTRAL, 2, 1);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("invincible_craft:boost")), SoundSource.NEUTRAL, 2, 1, false);
				}
			}
		}
	}
}