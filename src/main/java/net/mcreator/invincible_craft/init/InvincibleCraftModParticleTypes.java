/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.invincible_craft.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.invincible_craft.InvincibleCraftMod;

public class InvincibleCraftModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, InvincibleCraftMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHOCKWAVE = REGISTRY.register("shockwave", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> IMPACT = REGISTRY.register("impact", () -> new SimpleParticleType(true));
}