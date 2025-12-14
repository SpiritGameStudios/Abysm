package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.world.entity.harpoon.HarpoonEntity;
import org.jetbrains.annotations.Nullable;

public interface HarpoonOwner {
	void abysm$setHarpoon(@Nullable HarpoonEntity harpoon);
}
