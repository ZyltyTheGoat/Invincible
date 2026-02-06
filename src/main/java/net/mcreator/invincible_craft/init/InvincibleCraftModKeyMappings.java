/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.invincible_craft.init;

import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.invincible_craft.network.FollowUpKeyMessage;
import net.mcreator.invincible_craft.network.BattleModeMessage;

@EventBusSubscriber(Dist.CLIENT)
public class InvincibleCraftModKeyMappings {
	public static final KeyMapping BATTLE_MODE = new KeyMapping("key.invincible_craft.battle_mode", GLFW.GLFW_KEY_V, "key.categories.invincible_craft") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new BattleModeMessage(0, 0));
				BattleModeMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping FOLLOW_UP_KEY = new KeyMapping("key.invincible_craft.follow_up_key", GLFW.GLFW_KEY_G, "key.categories.invincible_craft") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new FollowUpKeyMessage(0, 0));
				FollowUpKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				FOLLOW_UP_KEY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - FOLLOW_UP_KEY_LASTPRESS);
				PacketDistributor.sendToServer(new FollowUpKeyMessage(1, dt));
				FollowUpKeyMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long FOLLOW_UP_KEY_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(BATTLE_MODE);
		event.register(FOLLOW_UP_KEY);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				BATTLE_MODE.consumeClick();
				FOLLOW_UP_KEY.consumeClick();
			}
		}
	}
}