/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.invincible_craft.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.invincible_craft.InvincibleCraftMod;

public class InvincibleCraftModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, InvincibleCraftMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> WOOSH = REGISTRY.register("woosh", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("invincible_craft", "woosh")));
	public static final DeferredHolder<SoundEvent, SoundEvent> HIT_STRONG = REGISTRY.register("hit_strong", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("invincible_craft", "hit_strong")));
}