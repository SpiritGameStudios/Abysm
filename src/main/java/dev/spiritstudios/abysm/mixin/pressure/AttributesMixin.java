package dev.spiritstudios.abysm.mixin.pressure;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Attributes.class, priority = 500)
public abstract class AttributesMixin {

	// this was a great idea with zero total drawbacks
	@ModifyVariable(method = "register", at = @At("HEAD"), index = 1, argsOnly = true)
	private static Attribute modifyMaxValues(Attribute value, String id) {
		if (!(value instanceof RangedAttribute attribute)) {
			return value;
		}
		if ("max_health".equals(id)) {
			((RangedAttributeAccessor) attribute).setMaxValue(Math.max(attribute.getMaxValue(), 1048576)); // 2^20
		} else if ("attack_damage".equals(id)) {
			((RangedAttributeAccessor) attribute).setMaxValue(Math.max(attribute.getMaxValue(), Long.MAX_VALUE));
		}
		return attribute;
	}
}
