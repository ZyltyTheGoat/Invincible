/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.invincible_craft.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.invincible_craft.client.particle.ShockwaveParticle;
import net.mcreator.invincible_craft.client.particle.ImpactParticle;

@EventBusSubscriber(Dist.CLIENT)
public class InvincibleCraftModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(InvincibleCraftModParticleTypes.SHOCKWAVE.get(), ShockwaveParticle::provider);
		event.registerSpriteSet(InvincibleCraftModParticleTypes.IMPACT.get(), ImpactParticle::provider);
	}
}