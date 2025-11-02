package dev.spiritstudios.abysm.mixin.skeleshark;

import dev.spiritstudios.specter.api.entity.EntityPart;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPart.class)
public interface EntityPartAccessor {

	@Accessor(value = "dimensions", remap = false)
	EntityDimensions abysm$getDimensions();

	@Mutable
	@Accessor(value = "dimensions", remap = false)
	void abysm$setDimensions(EntityDimensions updated);
}
