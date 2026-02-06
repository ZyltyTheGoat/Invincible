package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.invincible_craft.init.InvincibleCraftModMobEffects;

import java.util.Comparator;

public class AttackFollowUpProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(128 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if ((entity.getPersistentData().getString("followUpUUID")).equals(entityiterator.getStringUUID()) && entityiterator.isAlive()) {
					{
						Entity _ent = entity;
						_ent.teleportTo((entityiterator.getX() + (-1.5) * entity.getLookAngle().x), (entityiterator.getY() + entityiterator.getBbHeight() / 2d + (-1.5) * entity.getLookAngle().y),
								(entityiterator.getZ() + (-1.5) * entity.getLookAngle().z));
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport((entityiterator.getX() + (-1.5) * entity.getLookAngle().x), (entityiterator.getY() + entityiterator.getBbHeight() / 2d + (-1.5) * entity.getLookAngle().y),
									(entityiterator.getZ() + (-1.5) * entity.getLookAngle().z), _ent.getYRot(), _ent.getXRot());
					}
					if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(InvincibleCraftModMobEffects.STUN, 10, 1, false, false));
					entity.getPersistentData().putString("followUpUUID", "");
					if (entity instanceof LivingEntity _entity)
						_entity.removeEffect(InvincibleCraftModMobEffects.FOLLOW_UP);
					return;
				}
			}
		}
	}
}