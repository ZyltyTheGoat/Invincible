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

import net.mcreator.invincible_craft.network.*;

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
	public static final KeyMapping KEY_MOVEMENT_JUMP = new KeyMapping("key.invincible_craft.key_movement_jump", GLFW.GLFW_KEY_SPACE, "key.categories.invincible_craft_movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new KeyMovementJumpMessage(0, 0));
				KeyMovementJumpMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				KEY_MOVEMENT_JUMP_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - KEY_MOVEMENT_JUMP_LASTPRESS);
				PacketDistributor.sendToServer(new KeyMovementJumpMessage(1, dt));
				KeyMovementJumpMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping KEY_MOVEMENT_STRAFE_LEFT = new KeyMapping("key.invincible_craft.key_movement_strafe_left", GLFW.GLFW_KEY_A, "key.categories.invincible_craft_movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new KeyMovementStrafeLeftMessage(0, 0));
				KeyMovementStrafeLeftMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				KEY_MOVEMENT_STRAFE_LEFT_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - KEY_MOVEMENT_STRAFE_LEFT_LASTPRESS);
				PacketDistributor.sendToServer(new KeyMovementStrafeLeftMessage(1, dt));
				KeyMovementStrafeLeftMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping KEY_MOVEMENT_STRAFE_RIGHT = new KeyMapping("key.invincible_craft.key_movement_strafe_right", GLFW.GLFW_KEY_D, "key.categories.invincible_craft_movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new KeyMovementStrafeRightMessage(0, 0));
				KeyMovementStrafeRightMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				KEY_MOVEMENT_STRAFE_RIGHT_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - KEY_MOVEMENT_STRAFE_RIGHT_LASTPRESS);
				PacketDistributor.sendToServer(new KeyMovementStrafeRightMessage(1, dt));
				KeyMovementStrafeRightMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping KEY_MOVEMENT_WALK_BACKWARDS = new KeyMapping("key.invincible_craft.key_movement_walk_backwards", GLFW.GLFW_KEY_S, "key.categories.invincible_craft_movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new KeyMovementWalkBackwardsMessage(0, 0));
				KeyMovementWalkBackwardsMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				KEY_MOVEMENT_WALK_BACKWARDS_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - KEY_MOVEMENT_WALK_BACKWARDS_LASTPRESS);
				PacketDistributor.sendToServer(new KeyMovementWalkBackwardsMessage(1, dt));
				KeyMovementWalkBackwardsMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping KEY_MOVEMENT_WALK_FORWARDS = new KeyMapping("key.invincible_craft.key_movement_walk_forwards", GLFW.GLFW_KEY_W, "key.categories.invincible_craft_movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new KeyMovementWalkForwardsMessage(0, 0));
				KeyMovementWalkForwardsMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				KEY_MOVEMENT_WALK_FORWARDS_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - KEY_MOVEMENT_WALK_FORWARDS_LASTPRESS);
				PacketDistributor.sendToServer(new KeyMovementWalkForwardsMessage(1, dt));
				KeyMovementWalkForwardsMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping KEY_MOVEMENT_SPRINT = new KeyMapping("key.invincible_craft.key_movement_sprint", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.invincible_craft_movement") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new KeyMovementSprintMessage(0, 0));
				KeyMovementSprintMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				KEY_MOVEMENT_SPRINT_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - KEY_MOVEMENT_SPRINT_LASTPRESS);
				PacketDistributor.sendToServer(new KeyMovementSprintMessage(1, dt));
				KeyMovementSprintMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long FOLLOW_UP_KEY_LASTPRESS = 0;
	private static long KEY_MOVEMENT_JUMP_LASTPRESS = 0;
	private static long KEY_MOVEMENT_STRAFE_LEFT_LASTPRESS = 0;
	private static long KEY_MOVEMENT_STRAFE_RIGHT_LASTPRESS = 0;
	private static long KEY_MOVEMENT_WALK_BACKWARDS_LASTPRESS = 0;
	private static long KEY_MOVEMENT_WALK_FORWARDS_LASTPRESS = 0;
	private static long KEY_MOVEMENT_SPRINT_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(BATTLE_MODE);
		event.register(FOLLOW_UP_KEY);
		event.register(KEY_MOVEMENT_JUMP);
		event.register(KEY_MOVEMENT_STRAFE_LEFT);
		event.register(KEY_MOVEMENT_STRAFE_RIGHT);
		event.register(KEY_MOVEMENT_WALK_BACKWARDS);
		event.register(KEY_MOVEMENT_WALK_FORWARDS);
		event.register(KEY_MOVEMENT_SPRINT);
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				BATTLE_MODE.consumeClick();
				FOLLOW_UP_KEY.consumeClick();
				KEY_MOVEMENT_JUMP.consumeClick();
				KEY_MOVEMENT_STRAFE_LEFT.consumeClick();
				KEY_MOVEMENT_STRAFE_RIGHT.consumeClick();
				KEY_MOVEMENT_WALK_BACKWARDS.consumeClick();
				KEY_MOVEMENT_WALK_FORWARDS.consumeClick();
				KEY_MOVEMENT_SPRINT.consumeClick();
			}
		}
	}
}