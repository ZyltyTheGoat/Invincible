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
					if (entity instanceof Player) {
						if (entity.level().isClientSide()) {
							CompoundTag data = entity.getPersistentData();
							data.putString("PlayerCurrentAnimation", "invincible_craft:deltaTest");
							data.putBoolean("OverrideCurrentAnimation", true);
							data.putBoolean("FirstPersonAnimation", false);
						} else {
							PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "invincible_craft:deltaTest", true, false));
						}
					}
					if (movement.length() > 0) {
						movement = (movement.normalize()).scale(currentSpeed);
						entity.setDeltaMovement(new Vec3((movement.x()), (movement.y()), (movement.z())));
						if (useSprint) {
							if (entity instanceof Player) {
								if (entity.level().isClientSide()) {
									CompoundTag data = entity.getPersistentData();
									data.putString("PlayerCurrentAnimation", "invincible_craft:flight_sprint");
									data.putBoolean("OverrideCurrentAnimation", true);
									data.putBoolean("FirstPersonAnimation", false);
								} else {
									PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "invincible_craft:flight_sprint", true, false));
								}
							}
							if (entity instanceof Player player && !player.isFallFlying()) {
								player.startFallFlying();
							}
						}
					}
				} else {
					if (entity instanceof Player) {
						if (entity.level().isClientSide()) {
							CompoundTag data = entity.getPersistentData();
							data.putString("PlayerCurrentAnimation", "invincible_craft:deltaTest");
							data.putBoolean("OverrideCurrentAnimation", true);
							data.putBoolean("FirstPersonAnimation", false);
						} else {
							PacketDistributor.sendToPlayersInDimension((ServerLevel) entity.level(), new PlayPlayerAnimationMessage(entity.getId(), "invincible_craft:deltaTest", true, false));
						}
					}
					entity.getPersistentData().putDouble("currentSpeedRampTicks ", 0);
					entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() * 0.6), 0, (entity.getDeltaMovement().z() * 0.6)));
					if (entity instanceof Player player) {
						player.stopFallFlying();
					}
				}
			}
		}
	}
}