package net.mcreator.invincible_craft.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SpeedRampProcedureProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double rampUpTicks = 0;
		double rampDownTicks = 0;
		double maxSpeedAmplifier = 0;
		double currentSpeedAmplifier = 0;
		rampUpTicks = 40;
		rampDownTicks = 0;
		if (!world.isClientSide()) {
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 1000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 10000) {
				maxSpeedAmplifier = 3;
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 10000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 100000) {
				maxSpeedAmplifier = 6;
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 100000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 500000) {
				maxSpeedAmplifier = 9;
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 500000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 1500000) {
				maxSpeedAmplifier = 11;
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 1500000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame < 3000000) {
				maxSpeedAmplifier = 13;
			}
			if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame >= 3000000 && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).fame <= 5000000) {
				maxSpeedAmplifier = 15;
			}
			if (entity.isSprinting()) {
				if (entity.getPersistentData().getDouble("currentSpeedRampTicks ") < rampUpTicks) {
					entity.getPersistentData().putDouble("currentSpeedRampTicks ", (entity.getPersistentData().getDouble("currentSpeedRampTicks ") + 1));
				}
			} else {
				if (entity.getPersistentData().getDouble("currentSpeedRampTicks ") > 0) {
					entity.getPersistentData().putDouble("currentSpeedRampTicks ", (entity.getPersistentData().getDouble("currentSpeedRampTicks ") - 2));
					if (entity.getPersistentData().getDouble("currentSpeedRampTicks ") < 0) {
						entity.getPersistentData().putDouble("currentSpeedRampTicks ", 0);
					}
				}
			}
			currentSpeedAmplifier = (entity.getPersistentData().getDouble("currentSpeedRampTicks ") / rampUpTicks) * maxSpeedAmplifier;
			if (currentSpeedAmplifier > 0) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 5, (int) (currentSpeedAmplifier - 1), true, false));
			}
		}
	}
}