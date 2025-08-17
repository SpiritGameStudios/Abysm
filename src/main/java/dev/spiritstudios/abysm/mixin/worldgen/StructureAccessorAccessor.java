package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureAccessor.class)
public interface StructureAccessorAccessor {
	// amazing name, i love mixins - Phana
	// Can we please call this StructureAccessor^2 lol - Sky

	@Accessor("world")
	WorldAccess abysm$getWorld();
}
