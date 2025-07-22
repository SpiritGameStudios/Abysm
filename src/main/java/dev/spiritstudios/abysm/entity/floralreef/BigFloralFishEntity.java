package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.pattern.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.DyeColor;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class BigFloralFishEntity extends AbstractFloralFishEntity implements EcologicalEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.floral_fish_big.idle");

	public EcosystemLogic ecosystemLogic;

	public BigFloralFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = this.createEcosystemLogic(this);
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
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.BIG_FLORAL_FISH;
	}

	@Override
	public EntityPattern getDefaultPattern(RegistryEntryLookup<EntityPatternVariant> lookup) {
		return new EntityPattern(
			lookup.getOrThrow(AbysmEntityPatternVariants.FLORAL_FISH_BIG_TERRA),
			DyeColor.LIGHT_BLUE.getEntityColor(), DyeColor.PINK.getEntityColor()
		);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		AnimationController<ElectricOoglyBooglyEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> {
			return event.setAndContinue(IDLE_ANIM);
		});

		controllers.add(animController);
	}

}
