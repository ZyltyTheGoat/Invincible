package net.mcreator.invincible_craft.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import java.util.Comparator;

public class KnockbackProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity sourceEntity, boolean ignoreKnockbackResistance, double knockbackX, double knockbackY, double knockbackZ, double power, double range, String type) {
		if (sourceEntity == null || type == null)
			return;
		double knockback_y = 0;
		double knockback_x = 0;
		double knockback_z = 0;
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(range / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (!(entityiterator == sourceEntity)) {
					if ((type).equals("center")) {
						knockback_x = entityiterator.getX() - x;
						knockback_y = Math.min(entityiterator.getY() - y, 2);
						knockback_z = entityiterator.getZ() - z;
					} else if ((type).equals("sourceLook")) {
						knockback_x = sourceEntity.getLookAngle().x;
						knockback_y = sourceEntity.getLookAngle().y;
						knockback_z = sourceEntity.getLookAngle().z;
					} else if ((type).equals("zero")) {
						entityiterator.setDeltaMovement(new Vec3(0, 0, 0));
					} else if ((type).equals("custom")) {
						knockback_x = knockbackX;
						knockback_y = knockbackY;
						knockback_z = knockbackZ;
					}
					if (entityiterator instanceof LivingEntity && !ignoreKnockbackResistance) {
						knockback_x = knockback_x * (1.2 - (entityiterator instanceof LivingEntity _livingEntity9 && _livingEntity9.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
								? _livingEntity9.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue()
								: 0));
						knockback_y = knockback_y * (1.2 - (entityiterator instanceof LivingEntity _livingEntity10 && _livingEntity10.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
								? _livingEntity10.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue()
								: 0));
						knockback_z = knockback_z * (1.2 - (entityiterator instanceof LivingEntity _livingEntity11 && _livingEntity11.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE)
								? _livingEntity11.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getBaseValue()
								: 0));
					}
					entityiterator.setDeltaMovement(new Vec3((knockback_x * power), (knockback_y * power), (knockback_z * power)));
					entityiterator.hurtMarked = true;
				}
			}
		}
	}
}