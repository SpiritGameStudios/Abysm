package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureManager.class)
public interface StructureManagerAccessor {
	// amazing name, i love mixins - Phana
	// Can we please call this StructureAccessor^2 lol - Sky
	// this use to be called StructureAccessorAccessor when we used yarn. RIP StructureAccessorAccessor, 2025 - 2025 - echo

	@Accessor("level")
	LevelAccessor getLevel();
}
