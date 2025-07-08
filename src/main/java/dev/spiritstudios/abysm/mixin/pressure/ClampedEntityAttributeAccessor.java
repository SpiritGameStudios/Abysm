package dev.spiritstudios.abysm.mixin.pressure;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClampedEntityAttribute.class)
public interface ClampedEntityAttributeAccessor {

	@Mutable
	@Accessor("maxValue")
	void abysm$setMaxValue(double maxValue);
}
