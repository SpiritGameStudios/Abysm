package dev.spiritstudios.abysm.entity.variant;

import dev.spiritstudios.abysm.data.variant.AbstractEntityVariant;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * @param <T> Child of {@link AbstractEntityVariant} type for your entity
 * @see AbstractEntityVariant
 */
public interface Variantable<T extends AbstractEntityVariant> {
	// TODO - possible DRY by accepting datatracker in get/set variant int ids?
	// TODO - DRY via random variant upon spawn

	/**
	 * @return This Entity's EntityVariant, as the actual variant.
	 */
	T getVariant();

	/**
	 * Set this Entity's EntityVariant using the actual variant.
	 */
	void setVariant(RegistryEntry<T> variant);
}
