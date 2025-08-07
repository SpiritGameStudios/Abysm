package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import org.jetbrains.annotations.Nullable;

public interface HarpoonOwner {
	@Nullable HarpoonEntity abysm$getHarpoon();
	void abysm$setHarpoon(@Nullable HarpoonEntity harpoon);
}
