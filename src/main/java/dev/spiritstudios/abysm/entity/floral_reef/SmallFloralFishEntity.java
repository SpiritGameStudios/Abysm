package dev.spiritstudios.abysm.entity.floral_reef;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.entity.ecosystem.EcologicalEntity;
import dev.spiritstudios.abysm.entity.ecosystem.EcosystemLogic;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

import java.util.List;

public class SmallFloralFishEntity extends AbstractFloralFishEntity implements EcologicalEntity {
	public static final EntityPatternVariant DEFAULT_PATTERN_VARIANT = new EntityPatternVariant(
		AbysmEntityTypes.SMALL_FLORAL_FISH, "Colorful", Abysm.id("textures/entity/pattern/floral_fish_small/colorful.png")
	);

	public static final EntityPattern DEFAULT_PATTERN = new EntityPattern(DEFAULT_PATTERN_VARIANT, DyeColor.PINK.getEntityColor(), DyeColor.LIGHT_BLUE.getEntityColor());

	public EcosystemLogic ecosystemLogic;

	public SmallFloralFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	@Override
	public EntityPattern getDefaultPattern() {
		return DEFAULT_PATTERN;
	}

	@Override
	public List<Class<? extends LivingEntity>> definePredators() {
		return List.of(BloomrayEntity.class);
	}

	@Override
	public List<Class<? extends LivingEntity>> definePrey() {
		return List.of(ElectricOoglyBooglyEntity.class);
	}

	@Override
	public List<Block> definePlants() {
		return List.of(AbysmBlocks.BLUE_SCABIOSA);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}
}
