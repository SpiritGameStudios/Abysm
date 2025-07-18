package dev.spiritstudios.abysm.mixin.pressure;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = EntityAttributes.class, priority = 500)
public abstract class EntityAttributesMixin {

	// this was a great idea with zero total drawbacks
	@ModifyVariable(method = "register", at = @At("HEAD"), index = 1, argsOnly = true)
	private static EntityAttribute modifyMaxValues(EntityAttribute value, String id) {
		if (!(value instanceof ClampedEntityAttribute attribute)) {
			return value;
		}
		if ("max_health".equals(id)) {
			((ClampedEntityAttributeAccessor) attribute).abysm$setMaxValue(Math.max(attribute.getMaxValue(), 1048576)); // 2^20
		} else if ("attack_damage".equals(id)) {
			((ClampedEntityAttributeAccessor) attribute).abysm$setMaxValue(Math.max(attribute.getMaxValue(), Long.MAX_VALUE));
		}
		return attribute;
	}
}
