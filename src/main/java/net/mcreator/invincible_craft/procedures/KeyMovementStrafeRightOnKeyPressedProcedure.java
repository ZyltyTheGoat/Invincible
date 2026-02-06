package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

public class KeyMovementStrafeRightOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			InvincibleCraftModVariables.PlayerVariables _vars = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
			_vars.movementStrafeRight = true;
			_vars.markSyncDirty();
		}
	}
}