package dev.spiritstudios.abysm.entity.variant;

import dev.spiritstudios.abysm.data.variant.AbstractEntityVariant;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.MobEntity;

/**
 * @see AbstractEntityVariant
 * @param <T> Child of {@link AbstractEntityVariant} type for your entity
 */
public interface Variantable<T extends AbstractEntityVariant> {
	// TODO - possible DRY by accepting datatracker in get/set variant int ids?

	/**
	 * @return This Entity's EntityVariant, as the actual variant. This will likely use {@link Variantable#getVariantIntId()}.
	 */
	T getVariant();

	/**
	 * Set this Entity's EntityVariant using the actual variant. This will likely use {@link Variantable#setVariantIntId(int)}.
	 */
	void setVariant(T variant);

	/**
	 * @return This Entity's EntityVariant int id. This is expected to use this Entity's {@link MobEntity#getDataTracker()} {@link net.minecraft.entity.data.DataTracker#get(TrackedData)} method.
	 */
	int getVariantIntId();

	/**
	 * Set this Entity's EntityVariant int id. This is expected to use this Entity's {@link MobEntity#getDataTracker()} {@link net.minecraft.entity.data.DataTracker#set(TrackedData, Object)} method.
	 */
	void setVariantIntId(int variantId);

}
