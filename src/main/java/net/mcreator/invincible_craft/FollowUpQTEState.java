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

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
public class FollowUpQTEState {
	public static final FollowUpQTEState INSTANCE = new FollowUpQTEState();
	private boolean active = false;
	private long startTime = 0;
	private float successZoneStart = 0;
	private float successZoneSize = 90.0f;
	private float rotationSpeed = 670.0f;
	private boolean hasTimedOut = false;

	private FollowUpQTEState() {
	}

	// Start QTE with random success zone
	public void startQTE() {
		this.active = true;
		this.startTime = System.currentTimeMillis();
		this.hasTimedOut = false;
		// Random zone between 90° (3 o'clock) and 360° (12 o'clock)
		this.successZoneStart = 90.0f + (float) (Math.random() * 270.0f);
	}

	public boolean isActive() {
		return active;
	}

	public float getCurrentRotation() {
		if (!active)
			return 0;
		long elapsed = System.currentTimeMillis() - startTime;
		float seconds = elapsed / 1000.0f;
		return (seconds * rotationSpeed) % 360.0f;
	}

	public float getSuccessZoneStart() {
		return successZoneStart;
	}

	public float getSuccessZoneEnd() {
		return (successZoneStart + successZoneSize) % 360.0f;
	}

	public boolean isInSuccessZone() {
		if (!active)
			return false;
		float current = getCurrentRotation();
		float start = successZoneStart;
		float end = getSuccessZoneEnd();
		if (end < start) {
			return current >= start || current <= end;
		}
		return current >= start && current <= end;
	}

	public boolean hasTimedOut() {
		if (!active)
			return false;
		long elapsed = System.currentTimeMillis() - startTime;
		return elapsed > 1500; 
	}

	public boolean endQTE() {
		boolean success = isInSuccessZone() && !hasTimedOut();
		this.active = false;
		return success;
	}

	public void cancelQTE() {
		this.active = false;
		this.hasTimedOut = true;
	}
}