package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.mcreator.invincible_craft.init.InvincibleCraftModParticleTypes;
import net.mcreator.invincible_craft.init.InvincibleCraftModMobEffects;

import java.util.Comparator;

public class AttackPunchProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double attackDamage = 0;
		double comboCounter = 0;
		boolean generatedParticles = false;
		boolean finisher = false;
		comboCounter = entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(InvincibleCraftModMobEffects.COMBO) ? _livEnt.getEffect(InvincibleCraftModMobEffects.COMBO).getAmplifier() : 0;
		if (comboCounter >= 5) {
			attackDamage = (entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity1.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0) * 1.5;
			finisher = true;
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(InvincibleCraftModMobEffects.COMBAT_COOLDOWN, 17, 0, true, false));
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(InvincibleCraftModMobEffects.COMBO);
		} else {
			attackDamage = entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity4.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0;
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(InvincibleCraftModMobEffects.COMBAT_COOLDOWN, 3, 0, true, false));
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(InvincibleCraftModMobEffects.COMBO, 30, (int) (comboCounter + 1), true, false));
		}
		if (entity instanceof LivingEntity _entity)
			_entity.swing(InteractionHand.MAIN_HAND, true);
		{
			final Vec3 _center = new Vec3((x + entity.getLookAngle().x * 2), (y + 1 + entity.getLookAngle().y * 2), (z + entity.getLookAngle().z * 2));
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator instanceof LivingEntity && !(entityiterator == entity) && !(entityiterator instanceof TamableAnimal _tamIsTamedBy && entity instanceof LivingEntity _livEnt ? _tamIsTamedBy.isOwnedBy(_livEnt) : false)) {
					entityiterator.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("invincible_craft:attack"))), entity), (float) attackDamage);
					entityiterator.invulnerableTime = 0;
					if (finisher) {
						KnockbackProcedure.execute(world, x + entity.getLookAngle().x * 2, y + 1 + entity.getLookAngle().y * 2, z + entity.getLookAngle().z * 2, entity, true, 0, 0, 0, 3, 4, "sourceLook");
					}
					if (!generatedParticles) {
						generatedParticles = true;
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("invincible_craft:hit_strong")), SoundSource.NEUTRAL, 4, (float) 0.7);
							} else {
								_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("invincible_craft:hit_strong")), SoundSource.NEUTRAL, 4, (float) 0.7, false);
							}
						}
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.POOF, (entityiterator.getX()), (entityiterator.getY() + entityiterator.getBbHeight() / 2d), (entityiterator.getZ()), (int) (finisher ? 7 : 2), 0.2, 0.3, 0.2, (finisher ? 0.5 : 0.2));
						if (finisher) {
							entity.getPersistentData().putString("followUpUUID", (entityiterator.getStringUUID()));
							if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
								_entity.addEffect(new MobEffectInstance(InvincibleCraftModMobEffects.FOLLOW_UP, 30, 0, true, false));
							if (world instanceof ServerLevel _level)
								_level.sendParticles(ParticleTypes.FLASH, (entityiterator.getX()), (entityiterator.getY() + entityiterator.getBbHeight() / 2d), (entityiterator.getZ()), 4, 0.75, 0.75, 0.75, 0);
							if (world instanceof ServerLevel _level)
								_level.sendParticles((SimpleParticleType) (InvincibleCraftModParticleTypes.SHOCKWAVE.get()), (entityiterator.getX()), (entityiterator.getY() + entityiterator.getBbHeight() / 2d), (entityiterator.getZ()), 1, 0, 0, 0,
										0);
						} else {
							if (world instanceof ServerLevel _level)
								_level.sendParticles((SimpleParticleType) (InvincibleCraftModParticleTypes.IMPACT.get()), (entityiterator.getX()), (entityiterator.getY() + entityiterator.getBbHeight() / 2d), (entityiterator.getZ()), 1, 0, 0, 0, 0);
						}
					}
				}
			}
		}
		if (!generatedParticles) {
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.POOF, (x + entity.getLookAngle().x * 2), (y + 1 + entity.getLookAngle().y * 2), (z + entity.getLookAngle().z * 2), (int) (finisher ? 5 : 2), 0.1, 0.1, 0.1, (finisher ? 0.5 : 0.2));
			if (finisher) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (InvincibleCraftModParticleTypes.SHOCKWAVE.get()), (x + entity.getLookAngle().x * 2), (y + 1 + entity.getLookAngle().y * 2), (z + entity.getLookAngle().z * 2), 1, 0, 0, 0, 0);
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.FLASH, (x + entity.getLookAngle().x * 2), (y + 1 + entity.getLookAngle().y * 2), (z + entity.getLookAngle().z * 2), 2, 0.5, 0.5, 0.5, 0);
			} else {
				if (world instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (InvincibleCraftModParticleTypes.IMPACT.get()), (x + entity.getLookAngle().x * 2), (y + 1 + entity.getLookAngle().y * 2), (z + entity.getLookAngle().z * 2), 1, 0, 0, 0, 0);
			}
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.player.attack.sweep")), SoundSource.NEUTRAL, (float) 1.5, (float) 0.7);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.player.attack.sweep")), SoundSource.NEUTRAL, (float) 1.5, (float) 0.7, false);
			}
		}
	}
}