package net.mcreator.invincible_craft.procedures;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.invincible_craft.network.PlayPlayerAnimationMessage;
import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

@EventBusSubscriber
public class FlightModeHandlerProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Entity entity = event.getEntity();
		if (entity == null)
			return;
		InvincibleCraftModVariables.PlayerVariables playerVars = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
		// 1. EXIT FLIGHT IF ON GROUND
		if (playerVars.flightMode && entity.onGround()) {
			exitFlight(entity, playerVars);
			return;
		}
		// 2. MAIN FLIGHT LOGIC
		if (playerVars.flightMode) {
			handleFlightLogic(entity, playerVars);
		}
	}

	private static void handleFlightLogic(Entity entity, InvincibleCraftModVariables.PlayerVariables playerVars) {
		entity.setNoGravity(true);
		entity.fallDistance = 0;
		// Input States
		boolean isMoving = playerVars.movementStrafeLeft || playerVars.movementStrafeRight || playerVars.movementWalkBackwards || playerVars.movementWalkForwards;
		boolean isSprinting = playerVars.movementSprint;
		CompoundTag persistentData = entity.getPersistentData();
		// FIX: Declare and initialize rampTicks from NBT
		double rampTicks = persistentData.getDouble("currentSpeedRampTicks");
		if (isMoving) {
			// ACCELERATION RAMP
			if (rampTicks < 20)
				rampTicks++;
			persistentData.putDouble("currentSpeedRampTicks", rampTicks);
			double speedMult = (isSprinting ? playerVars.flightSpeed : 0.6) * Math.min(rampTicks / 20.0, 1.0);
			// DIRECTIONAL VECTORS
			Vec3 look = entity.getLookAngle();
			Vec3 right = new Vec3(-look.z, 0, look.x).normalize();
			Vec3 targetMove = Vec3.ZERO;
			if (playerVars.movementWalkForwards)
				targetMove = targetMove.add(look);
			if (playerVars.movementWalkBackwards)
				targetMove = targetMove.subtract(look);
			if (playerVars.movementStrafeRight)
				targetMove = targetMove.add(right);
			if (playerVars.movementStrafeLeft)
				targetMove = targetMove.subtract(right);
			if (targetMove.lengthSqr() > 0) {
				targetMove = targetMove.normalize().scale(speedMult);
				// LERP VELOCITY: Smooth direction changes
				Vec3 currentVel = entity.getDeltaMovement();
				entity.setDeltaMovement(currentVel.add(targetMove.subtract(currentVel).scale(0.2)));
			}
			handleAnimations(entity, isSprinting);
		} else {
			// --- SMOOTH STOPPING (DRIFT) ---
			if (rampTicks > 0) {
				rampTicks--;
				persistentData.putDouble("currentSpeedRampTicks", rampTicks);
			}
			Vec3 drift = entity.getDeltaMovement().scale(0.92);
			entity.setDeltaMovement(drift.lengthSqr() < 0.001 ? Vec3.ZERO : drift);
			resetAnimations(entity);
			if (entity instanceof Player player)
				player.stopFallFlying();
		}
		playerVars.markSyncDirty();
	}

	private static void exitFlight(Entity entity, InvincibleCraftModVariables.PlayerVariables playerVars) {
		playerVars.flightMode = false;
		playerVars.flightRoll = 0; // Reset roll just in case
		playerVars.markSyncDirty();
		entity.setNoGravity(false);
		entity.getPersistentData().putDouble("currentSpeedRampTicks", 0);
		if (entity instanceof Player player)
			player.stopFallFlying();
		resetAnimations(entity);
	}

	private static void handleAnimations(Entity entity, boolean isSprinting) {
		if (!(entity instanceof Player player))
			return;
		if (isSprinting) {
			if (entity.level().isClientSide()) {
				CompoundTag data = entity.getPersistentData();
				data.putString("PlayerCurrentAnimation", "invincible_craft:flight_sprint");
				data.putBoolean("OverrideCurrentAnimation", true);
			} else {
				PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "invincible_craft:flight_sprint", true, false));
			}
			if (!player.isFallFlying())
				player.startFallFlying();
		} else {
			resetAnimations(entity);
			player.stopFallFlying();
		}
	}

	private static void resetAnimations(Entity entity) {
		if (entity instanceof Player) {
			if (entity.level().isClientSide()) {
				CompoundTag data = entity.getPersistentData();
				data.remove("PlayerCurrentAnimation");
				data.putBoolean("ResetPlayerAnimation", true);
			} else {
				PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "", false, false));
			}
		}
	}
}