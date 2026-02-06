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

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;

import net.mcreator.invincible_craft.init.InvincibleCraftModMobEffects;

@EventBusSubscriber
public class FollowUpQTETick {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event.getEntity());
	}

	public static void execute(Player player) {
		if (player == null)
			return;
		if (player.getPersistentData().getString("qte_mode").equals("followup_active")) {
			if (player.level().isClientSide()) {
				if (FollowUpQTEState.INSTANCE.hasTimedOut() || !player.hasEffect(InvincibleCraftModMobEffects.FOLLOW_UP)) {
					FollowUpQTEState.INSTANCE.cancelQTE();
					FollowUpQTEMaster.onKeyRelease(player);
				}
			}
		}
	}
}