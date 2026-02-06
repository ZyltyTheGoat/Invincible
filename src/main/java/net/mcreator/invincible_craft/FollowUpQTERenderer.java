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

import org.joml.Matrix4f;

import org.checkerframework.checker.units.qual.t;
import org.checkerframework.checker.units.qual.g;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;

@EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class FollowUpQTERenderer {
	@SubscribeEvent
	public static void onRenderOverlay(RenderGuiEvent.Post event) {
		if (!FollowUpQTEState.INSTANCE.isActive())
			return;
		Minecraft mc = Minecraft.getInstance();
		GuiGraphics guiGraphics = event.getGuiGraphics();
		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();
		// Center position
		float centerX = screenWidth / 2.0f;
		float centerY = screenHeight / 2.0f;
		float radius = 60.0f; // Circle radius
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		// 1. Render thin white base ring
		/*
		renderCircle(poseStack, centerX, centerY, 57, 6.0f, 0.49f, 0.49f, 0.49f, 1f);
		renderCircle(poseStack, centerX, centerY, 56, 1f, 0f, 0f, 0f, 1f);
		renderCircle(poseStack, centerX, centerY, 59, 1f, 0f, 0f, 0f, 1f);
		*/
		renderCircle(poseStack, centerX, centerY, 55, 18f, 0.42353f, 0.42353f, 0.42353f, 1f);
		renderCircle(poseStack, centerX, centerY, 54, 1f, 0f, 0f, 0f, 1f);
		renderCircle(poseStack, centerX, centerY, 59, 1f, 0f, 0f, 0f, 1f);
		renderCircle(poseStack, centerX, centerY, 68, 1f, 0f, 0f, 0f, 1f);
		renderCircle(poseStack, centerX, centerY, 73, 1f, 0f, 0f, 0f, 1f);
		renderCircle(poseStack, centerX, centerY, 60, 8f, 0.18824f, 0.18824f, 0.18824f, 1f);
		// 2. Render thick green success zone (centered on the base ring - half inside, half outside)
		float zoneStart = FollowUpQTEState.INSTANCE.getSuccessZoneStart();
		float zoneEnd = FollowUpQTEState.INSTANCE.getSuccessZoneEnd();
		float greenThickness = 8.0f;
		renderArcCentered(poseStack, centerX, centerY, 64, zoneStart - 1, zoneEnd + 1, greenThickness, 0f, 0f, 0f, 0.8f);
		renderArcCentered(poseStack, centerX, centerY, 64, zoneStart, zoneEnd, greenThickness, 0.6f, 1f, 0.6f, 0.8f);
		// 3. Render red indicator as a small wedge on the ring (half inside, half outside)
		float currentRotation = FollowUpQTEState.INSTANCE.getCurrentRotation();
		renderIndicatorWedge(poseStack, centerX, centerY, 64, currentRotation, 3f, 1f, 0f, 0f, 1f);
		poseStack.popPose();
	}

	private static void renderIndicatorWedge(PoseStack poseStack, float centerX, float centerY, float radius, float angle, float width, float r, float g, float b, float a) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();
		Matrix4f matrix = poseStack.last().pose();
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		// Create a small wedge that spans from slightly inside to slightly outside the ring
		float startAngle = angle - width / 2;
		float endAngle = angle + width / 2;
		float innerRadius = radius - 4; // Goes inside the ring
		float outerRadius = radius + 4; // Goes outside the ring
		float startRad = (float) Math.toRadians(startAngle - 90);
		float endRad = (float) Math.toRadians(endAngle - 90);
		int segments = 10;
		for (int i = 0; i <= segments; i++) {
			float t = (float) i / segments;
			float currentAngle = startRad + (endRad - startRad) * t;
			float cos = (float) Math.cos(currentAngle);
			float sin = (float) Math.sin(currentAngle);
			float outerX = centerX + cos * outerRadius;
			float outerY = centerY + sin * outerRadius;
			buffer.addVertex(matrix, outerX, outerY, 100).setColor(r, g, b, a);
			float innerX = centerX + cos * innerRadius;
			float innerY = centerY + sin * innerRadius;
			buffer.addVertex(matrix, innerX, innerY, 100).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
	}

	private static void renderCircle(PoseStack poseStack, float centerX, float centerY, float radius, float thickness, float r, float g, float b, float a) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();
		Matrix4f matrix = poseStack.last().pose();
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		int segments = 60;
		for (int i = 0; i <= segments; i++) {
			float angle = (float) (2 * Math.PI * i / segments);
			float cos = (float) Math.cos(angle);
			float sin = (float) Math.sin(angle);
			float outerX = centerX + cos * (radius + thickness);
			float outerY = centerY + sin * (radius + thickness);
			buffer.addVertex(matrix, outerX, outerY, 0).setColor(r, g, b, a);
			float innerX = centerX + cos * radius;
			float innerY = centerY + sin * radius;
			buffer.addVertex(matrix, innerX, innerY, 0).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
	}

	private static void renderArcCentered(PoseStack poseStack, float centerX, float centerY, float radius, float startAngle, float endAngle, float thickness, float r, float g, float b, float a) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();
		Matrix4f matrix = poseStack.last().pose();
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
		// Center the arc on the base ring - half thickness inside, half outside
		float innerRadius = radius - thickness / 2;
		float outerRadius = radius + thickness / 2;
		float startRad = (float) Math.toRadians(startAngle - 90);
		float endRad = (float) Math.toRadians(endAngle - 90);
		if (endRad < startRad) {
			endRad += 2 * Math.PI;
		}
		int segments = 30;
		for (int i = 0; i <= segments; i++) {
			float t = (float) i / segments;
			float angle = startRad + (endRad - startRad) * t;
			float cos = (float) Math.cos(angle);
			float sin = (float) Math.sin(angle);
			float outerX = centerX + cos * outerRadius;
			float outerY = centerY + sin * outerRadius;
			buffer.addVertex(matrix, outerX, outerY, 0).setColor(r, g, b, a);
			float innerX = centerX + cos * innerRadius;
			float innerY = centerY + sin * innerRadius;
			buffer.addVertex(matrix, innerX, innerY, 0).setColor(r, g, b, a);
		}
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
	}
}