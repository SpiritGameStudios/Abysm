package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

public class AbysmEntityAttributeModifiers {

	public static final Identifier FAVORED_HUNT_SPEED_MODIFIER_ID = Abysm.id("favored_hunt_speed");
	public static final Identifier UNFAVORED_HUNT_SPEED_MODIFIER_ID = Abysm.id("unfavored_hunt_speed");

	// I honestly don't even think this is needed
	public static void init() {
		// NO-OP
	}

	/**
	 * @param speed Value is assumed to be positive to actually be a speed increase.
	 */
	public static EntityAttributeModifier ofFavoredSpeed(float speed) {
		return new EntityAttributeModifier(FAVORED_HUNT_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE);
	}

	/**
	 * @param speed Value is assumed to be negative to actually be a speed decrease.
	 */
	public static EntityAttributeModifier ofUnfavoredSpeed(float speed) {
		return new EntityAttributeModifier(UNFAVORED_HUNT_SPEED_MODIFIER_ID, speed, EntityAttributeModifier.Operation.ADD_VALUE);
	}

}
