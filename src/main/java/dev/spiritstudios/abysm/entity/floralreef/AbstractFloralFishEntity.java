package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.component.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.entity.pattern.Patternable;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;

import java.util.List;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class AbstractFloralFishEntity extends SimpleEcoSchoolingFishEntity implements GeoEntity, Patternable {
	public static final List<Integer> PATTERN_COLORS = List.of(
		DyeColor.WHITE.getTextureDiffuseColor(), DyeColor.BLACK.getTextureDiffuseColor(),
		DyeColor.BLUE.getTextureDiffuseColor(), DyeColor.LIGHT_BLUE.getTextureDiffuseColor(), DyeColor.CYAN.getTextureDiffuseColor(),
		DyeColor.PINK.getTextureDiffuseColor(), DyeColor.PURPLE.getTextureDiffuseColor(), DyeColor.MAGENTA.getTextureDiffuseColor(),
		DyeColor.RED.getTextureDiffuseColor(), DyeColor.YELLOW.getTextureDiffuseColor(), DyeColor.LIME.getTextureDiffuseColor()
	);

	public static final EntityDataAccessor<EntityPattern> ENTITY_PATTERN = SynchedEntityData.defineId(AbstractFloralFishEntity.class, AbysmTrackedDataHandlers.ENTITY_PATTERN);

	public AbstractFloralFishEntity(EntityType<? extends AbstractFloralFishEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ENTITY_PATTERN, EntityPattern.EMPTY_PATTERN);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new SwimToRandomPlaceGoal(this, 1.0F));
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		AnimationController<AbstractFloralFishEntity> animController = new AnimationController<>(5, event -> PlayState.STOP);

		controllerRegistrar.add(animController);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
		EntityPattern pattern = this.getPatternForInitialize(world, this, entityData);
		this.setEntityPattern(pattern);
		return entityData;
	}

	@Override
	public void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		this.writeEntityPattern(view);
	}

	@Override
	public void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		this.readEntityPattern(this, view);
	}

	@Nullable
	@Override
	public <T> T get(DataComponentType<? extends T> type) {
		return type == AbysmDataComponentTypes.ENTITY_PATTERN ?
			castComponentValue(type, this.getEntityPattern()) :
			super.get(type);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter from) {
		this.applyImplicitComponentIfPresent(from, AbysmDataComponentTypes.ENTITY_PATTERN);
		super.applyImplicitComponents(from);
	}

	@Override
	protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
		if (type == AbysmDataComponentTypes.ENTITY_PATTERN) {
			this.setEntityPattern(castComponentValue(AbysmDataComponentTypes.ENTITY_PATTERN, value));
			return true;
		} else {
			return super.applyImplicitComponent(type, value);
		}
	}

	@Override
	public void saveToBucketTag(ItemStack stack) {
		super.saveToBucketTag(stack);
		stack.copyFrom(AbysmDataComponentTypes.ENTITY_PATTERN, this);
	}

	@Override
	public EntityPattern getEntityPattern() {
		return this.entityData.get(ENTITY_PATTERN);
	}

	@Override
	public void setEntityPattern(EntityPattern pattern) {
		this.entityData.set(ENTITY_PATTERN, pattern);
	}

	@Override
	public List<Integer> getBaseColors() {
		return PATTERN_COLORS;
	}

	@Override
	public List<Integer> getPatternColors() {
		return PATTERN_COLORS;
	}
}
