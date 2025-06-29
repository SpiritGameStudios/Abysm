package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.registry.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

public class BigFloralFishEntity extends AbstractFloralFishEntity implements EcologicalEntity {
	public static final EntityPatternVariant DEFAULT_PATTERN_VARIANT = new EntityPatternVariant(
		AbysmEntityTypes.BIG_FLORAL_FISH, "Terra", Abysm.id("textures/entity/pattern/floral_fish_big/terra.png")
	);
	public static final EntityPattern DEFAULT_PATTERN = new EntityPattern(DEFAULT_PATTERN_VARIANT, DyeColor.LIGHT_BLUE.getEntityColor(), DyeColor.PINK.getEntityColor());
	public EcosystemLogic ecosystemLogic;

	public BigFloralFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = this.createEcosystemLogic(this);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.BIG_FLORAL_FISH;
	}

	@Override
	public EntityPattern getDefaultPattern() {
		return DEFAULT_PATTERN;
	}

}
