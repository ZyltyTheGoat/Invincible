package net.mcreator.invincible_craft;

import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.player.Player;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.invincible_craft.network.InvincibleCraftModVariables;

@EventBusSubscriber(modid = "invincible_craft", value = Dist.CLIENT)
public class HotbarOffsetHandler {
	@SubscribeEvent
	public static void onRenderGuiPre(RenderGuiLayerEvent.Pre event) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;
		boolean battleMode = player.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).battleMode;
		if (!battleMode)
			return;
		if (event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH) || event.getName().equals(VanillaGuiLayers.ARMOR_LEVEL) || event.getName().equals(VanillaGuiLayers.FOOD_LEVEL) || event.getName().equals(VanillaGuiLayers.AIR_LEVEL)
				|| event.getName().equals(VanillaGuiLayers.EXPERIENCE_BAR) || event.getName().equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) { // ADDED THIS
			GuiGraphics guiGraphics = event.getGuiGraphics();
			guiGraphics.pose().pushPose();
			guiGraphics.pose().translate(0, -13, 0);
		}
	}

	@SubscribeEvent
	public static void onRenderGuiPost(RenderGuiLayerEvent.Post event) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;
		boolean battleMode = player.getData(InvincibleCraftModVariables.PLAYER_VARIABLES).battleMode;
		if (!battleMode)
			return;
		if (event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH) || event.getName().equals(VanillaGuiLayers.ARMOR_LEVEL) || event.getName().equals(VanillaGuiLayers.FOOD_LEVEL) || event.getName().equals(VanillaGuiLayers.AIR_LEVEL)
				|| event.getName().equals(VanillaGuiLayers.EXPERIENCE_BAR) || event.getName().equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) { // ADDED THIS
			GuiGraphics guiGraphics = event.getGuiGraphics();
			guiGraphics.pose().popPose();
		}
	}
}