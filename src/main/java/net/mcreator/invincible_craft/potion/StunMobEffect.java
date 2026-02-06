package net.mcreator.invincible_craft.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class StunMobEffect extends MobEffect {
	public StunMobEffect() {
		super(MobEffectCategory.NEUTRAL, -256);
	}
}