package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.invincible_craft.init.InvincibleCraftModMobEffects;

public class ReturnComboCountProcedure {
	public static double execute(Entity entity) {
		if (entity == null)
			return 0;
		return entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(InvincibleCraftModMobEffects.COMBO) ? _livEnt.getEffect(InvincibleCraftModMobEffects.COMBO).getAmplifier() : 0;
	}
}