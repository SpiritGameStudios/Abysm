package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.registry.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SmallFloralFishEntity extends AbstractFloralFishEntity implements EcologicalEntity {
	public static final EntityPatternVariant DEFAULT_PATTERN_VARIANT = new EntityPatternVariant(
		AbysmEntityTypes.SMALL_FLORAL_FISH, "Colorful", Abysm.id("textures/entity/pattern/floral_fish_small/colorful.png")
	);

	public static final EntityPattern DEFAULT_PATTERN = new EntityPattern(DEFAULT_PATTERN_VARIANT, DyeColor.PINK.getEntityColor(), DyeColor.LIGHT_BLUE.getEntityColor());

	protected EcosystemLogic ecosystemLogic;

	public SmallFloralFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.alertEcosystemOfSpawn();
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	@Override
	public void onRemove(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemove(reason);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
	}

	@Override
	public EntityPattern getDefaultPattern() {
		return DEFAULT_PATTERN;
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SMALL_FLORAL_FISH;
	}

}
