package net.mcreator.invincible_craft.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.invincible_craft.procedures.ReturnComboCountProcedure;
import net.mcreator.invincible_craft.procedures.ReturnCombatModeActiveProcedure;

@EventBusSubscriber(Dist.CLIENT)
public class CombooverlayOverlay {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		if (ReturnCombatModeActiveProcedure.execute(entity)) {

			event.getGuiGraphics().blit(ResourceLocation.parse("invincible_craft:textures/screens/combo_bar_spritesheet.png"), w / 2 + -91, h - 36, Mth.clamp((int) ReturnComboCountProcedure.execute(entity) * 182, 0, 1092), 0, 182, 12, 1274, 12);

		}
	}
}