package net.mcreator.invincible_craft.procedures;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;

import net.mcreator.invincible_craft.network.PlayPlayerAnimationMessage;
import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

import javax.annotation.Nullable;

/* imports omitted */
@EventBusSubscriber
public class FlightModeHandlerProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		double trueRampUpValue = 0;
		double currentSpeed = 0;
		Vec3 forward = Vec3.ZERO;
		Vec3 right = Vec3.ZERO;
		Vec3 movement = Vec3.ZERO;
		boolean isMoving = false;
		boolean useForward = false;
		boolean useBackwards = false;
		boolean useLeft = false;
		boolean useRight = false;
		boolean useSprint = false;
		String targetAnimation = "";
		if (entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).flightMode) {
			if (entity.onGround()) {
				{
					InvincibleCraftModVariables.PlayerVariables _vars = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES);
					_vars.flightMode = false;
					_vars.markSyncDirty();
				}
				entity.setNoGravity(false);
				if (entity instanceof Player player) {
					player.stopFallFlying();
				}
				entity.getPersistentData().putDouble("currentSpeedRampTicks ", 0);
				entity.getPersistentData().remove("lastFlightAnimation");
				entity.getPersistentData().putLong("animationChangeTime", 0);
				if (entity instanceof Player) {
					if (entity.level().isClientSide()) {
						CompoundTag data = entity.getPersistentData();
						data.remove("PlayerCurrentAnimation");
						data.remove("PlayerAnimationProgress");
						data.putBoolean("ResetPlayerAnimation", true);
						data.putBoolean("FirstPersonAnimation", false);
					} else {
						PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "", false, false));
					}
				}
			} else {
				isMoving = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementStrafeLeft || entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementStrafeRight
						|| entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementWalkBackwards || entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementWalkForwards;
				useSprint = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementSprint;
				useForward = entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementWalkForwards;
				useBackwards = !useSprint && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementWalkBackwards;
				useLeft = !useSprint && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementStrafeLeft;
				useRight = !useSprint && entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).movementStrafeRight;
				entity.setNoGravity(true);
				entity.fallDistance = 0;
				if (isMoving) {
					if (entity.getPersistentData().getDouble("currentSpeedRampTicks ") < 15) {
						entity.getPersistentData().putDouble("currentSpeedRampTicks ", (entity.getPersistentData().getDouble("currentSpeedRampTicks ") + 1));
					}
					trueRampUpValue = Math.min(entity.getPersistentData().getDouble("currentSpeedRampTicks ") / 15, 1);
					currentSpeed = (useSprint ? entity.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).flightSpeed : 0.5) * trueRampUpValue;
					forward = entity.getLookAngle();
					right = (new Vec3((forward.z() * (-1)), 0, (forward.x()))).normalize();
					movement = Vec3.ZERO;
					// Determine target animation with debouncing
					if (useSprint) {
						targetAnimation = "invincible_craft:flight_sprint";
					} else if (useForward && !useBackwards && !useLeft && !useRight) {
						targetAnimation = "invincible_craft:flight_forward";
					} else if (useBackwards && !useForward && !useLeft && !useRight) {
						targetAnimation = "invincible_craft:flight_backwards";
					} else if (useRight && !useLeft && !useForward && !useBackwards) {
						targetAnimation = "invincible_craft:flight_right";
					} else if (useLeft && !useRight && !useForward && !useBackwards) {
						targetAnimation = "invincible_craft:flight_left";
					} else {
						// Mixed movement (diagonal, etc.)
						targetAnimation = "invincible_craft:flight_standing";
					}
					if (useForward) {
						movement = movement.add(forward);
					}
					if (useBackwards) {
						movement = movement.subtract(forward);
					}
					if (useRight) {
						movement = movement.add(right);
					}
					if (useLeft) {
						movement = movement.subtract(right);
					}
					if (movement.length() > 0) {
						movement = (movement.normalize()).scale(currentSpeed);
						entity.setDeltaMovement(new Vec3((movement.x()), (movement.y()), (movement.z())));
						if (useSprint) {
							if (entity instanceof Player player && !player.isFallFlying()) {
								player.startFallFlying();
							}
						}
					}
				} else {
					targetAnimation = "invincible_craft:flight_standing";
					entity.getPersistentData().putDouble("currentSpeedRampTicks ", 0);
					entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() * 0.6), 0, (entity.getDeltaMovement().z() * 0.6)));
					if (entity instanceof Player player) {
						player.stopFallFlying();
					}
				}
				// Animation debouncing - only change animation if it's been different for 3+ ticks
				String lastAnimation = entity.getPersistentData().getString("lastFlightAnimation");
				long currentTime = entity.level().getGameTime();
				if (!targetAnimation.equals(lastAnimation)) {
					// Animation wants to change
					long changeStartTime = entity.getPersistentData().getLong("animationChangeTime");
					if (changeStartTime == 0) {
						// First tick of wanting to change
						entity.getPersistentData().putLong("animationChangeTime", currentTime);
					} else if (currentTime - changeStartTime >= 3) {
						// Has wanted to change for 3+ ticks, actually change it
						entity.getPersistentData().putString("lastFlightAnimation", targetAnimation);
						entity.getPersistentData().putLong("animationChangeTime", 0);
						if (entity instanceof Player) {
							if (entity.level().isClientSide()) {
								CompoundTag data = entity.getPersistentData();
								data.putString("PlayerCurrentAnimation", targetAnimation);
								data.putBoolean("OverrideCurrentAnimation", true);
								data.putBoolean("FirstPersonAnimation", false);
							} else {
								PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), targetAnimation, true, false));
							}
						}
					}
				} else {
					// Animation is stable, reset change timer
					entity.getPersistentData().putLong("animationChangeTime", 0);
				}
				// Initialize animation on first tick
				if (lastAnimation.isEmpty()) {
					entity.getPersistentData().putString("lastFlightAnimation", targetAnimation);
					if (entity instanceof Player) {
						if (entity.level().isClientSide()) {
							CompoundTag data = entity.getPersistentData();
							data.putString("PlayerCurrentAnimation", targetAnimation);
							data.putBoolean("OverrideCurrentAnimation", true);
							data.putBoolean("FirstPersonAnimation", false);
						} else {
							PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), targetAnimation, true, false));
						}
					}
				}
			}
		}
	}
}