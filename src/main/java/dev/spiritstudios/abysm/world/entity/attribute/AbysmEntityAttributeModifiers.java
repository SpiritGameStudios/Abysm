package dev.spiritstudios.abysm.world.entity.attribute;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AbysmEntityAttributeModifiers {

	public static final Identifier FAVORED_HUNT_SPEED_MODIFIER_ID = Abysm.id("favored_hunt_speed");
	public static final Identifier UNFAVORED_HUNT_SPEED_MODIFIER_ID = Abysm.id("unfavored_hunt_speed");

	// I honestly don't even think this is needed - Kat
	// It isn't because nothing is registered here - Sky
	public static void init() {
		// NO-OP
	}

	/**
	 * @param speed Value is assumed to be positive to actually be a speed increase.
	 */
	public static AttributeModifier ofFavoredSpeed(float speed) {
		assert speed >= 0; // yes I meant to do this
		return new AttributeModifier(FAVORED_HUNT_SPEED_MODIFIER_ID, speed, AttributeModifier.Operation.ADD_VALUE);
	}

	/**
	 * @param speed Value is assumed to be negative to actually be a speed decrease.
	 */
	public static AttributeModifier ofUnfavoredSpeed(float speed) {
		assert speed <= 0;
		return new AttributeModifier(UNFAVORED_HUNT_SPEED_MODIFIER_ID, speed, AttributeModifier.Operation.ADD_VALUE);
	}

}
