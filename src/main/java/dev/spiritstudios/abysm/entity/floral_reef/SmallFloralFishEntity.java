package dev.spiritstudios.abysm.entity.floral_reef;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

public class SmallFloralFishEntity extends AbstractFloralFishEntity {
	public static final EntityPatternVariant DEFAULT_PATTERN_VARIANT = new EntityPatternVariant(
		AbysmEntityTypes.SMALL_FLORAL_FISH, "Colorful", Abysm.id("textures/entity/pattern/floral_fish_small/colorful.png")
	);

	public static final EntityPattern DEFAULT_PATTERN = new EntityPattern(DEFAULT_PATTERN_VARIANT, DyeColor.PINK.getEntityColor(), DyeColor.LIGHT_BLUE.getEntityColor());

	public SmallFloralFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public EntityPattern getDefaultPattern() {
		return DEFAULT_PATTERN;
	}
}
