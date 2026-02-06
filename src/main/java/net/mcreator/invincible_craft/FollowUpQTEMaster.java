/*
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.invincible_craft as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.invincible_craft;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.chat.Component;

import net.mcreator.invincible_craft.network.FollowUpPacketMessage;

public class FollowUpQTEMaster {
	// Static key state tracker to prevent double-calls
	private static final java.util.Map<java.util.UUID, Boolean> keyPressed = new java.util.concurrent.ConcurrentHashMap<>();

	// Called when key is PRESSED
	public static void onKeyPress(Entity entity) {
		if (!(entity instanceof Player player))
			return;
		java.util.UUID uuid = player.getUUID();
		// Prevent double-press
		if (keyPressed.getOrDefault(uuid, false)) {
			return;
		}
		keyPressed.put(uuid, true);
		// Set QTE mode
		player.getPersistentData().putString("qte_mode", "followup_active");
		// Apply slowness during QTE
		if (player instanceof LivingEntity livingEntity) {
			livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2, false, false));
		}
		// Start QTE on client
		if (player.level().isClientSide()) {
			FollowUpQTEState.INSTANCE.startQTE();
		}
	}

	// Called when key is RELEASED
	public static void onKeyRelease(Entity entity) {
		if (!(entity instanceof Player player))
			return;
		java.util.UUID uuid = player.getUUID();
		// Prevent double-release
		Boolean wasPressed = keyPressed.get(uuid);
		if (wasPressed == null || !wasPressed) {
			return;
		}
		// Clear key state
		keyPressed.put(uuid, false);
		String qteMode = player.getPersistentData().getString("qte_mode");
		// Only proceed if in QTE mode
		if (!qteMode.equals("followup_active")) {
			return;
		}
		// Reset QTE mode
		player.getPersistentData().putString("qte_mode", "");
		if (player.level().isClientSide()) {
			boolean success = FollowUpQTEState.INSTANCE.endQTE();
			if (success) {
				PacketDistributor.sendToServer(new FollowUpPacketMessage("success"));
			} else {
				player.displayClientMessage(Component.literal("§4§lMissed!"), true);
			}
		}
	}
}