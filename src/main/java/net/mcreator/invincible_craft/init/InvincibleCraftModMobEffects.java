/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.invincible_craft.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.invincible_craft.potion.StunMobEffect;
import net.mcreator.invincible_craft.potion.FollowUpMobEffect;
import net.mcreator.invincible_craft.potion.ComboMobEffect;
import net.mcreator.invincible_craft.potion.CombatCooldownMobEffect;
import net.mcreator.invincible_craft.InvincibleCraftMod;

public class InvincibleCraftModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, InvincibleCraftMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> STUN = REGISTRY.register("stun", () -> new StunMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> COMBAT_COOLDOWN = REGISTRY.register("combat_cooldown", () -> new CombatCooldownMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> COMBO = REGISTRY.register("combo", () -> new ComboMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> FOLLOW_UP = REGISTRY.register("follow_up", () -> new FollowUpMobEffect());
}