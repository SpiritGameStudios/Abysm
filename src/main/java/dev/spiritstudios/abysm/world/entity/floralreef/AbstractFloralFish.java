package dev.spiritstudios.abysm.world.entity.floralreef;

import dev.spiritstudios.abysm.core.component.AbysmDataComponents;
import dev.spiritstudios.abysm.world.entity.AbysmEntityDataSerializers;
import dev.spiritstudios.abysm.world.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.world.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.world.entity.pattern.Patternable;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractFloralFish extends SimpleEcoSchoolingFishEntity implements Patternable {
	public static final List<Integer> PATTERN_COLORS = List.of(
		DyeColor.WHITE.getTextureDiffuseColor(), DyeColor.BLACK.getTextureDiffuseColor(),
		DyeColor.BLUE.getTextureDiffuseColor(), DyeColor.LIGHT_BLUE.getTextureDiffuseColor(), DyeColor.CYAN.getTextureDiffuseColor(),
		DyeColor.PINK.getTextureDiffuseColor(), DyeColor.PURPLE.getTextureDiffuseColor(), DyeColor.MAGENTA.getTextureDiffuseColor(),
		DyeColor.RED.getTextureDiffuseColor(), DyeColor.YELLOW.getTextureDiffuseColor(), DyeColor.LIME.getTextureDiffuseColor()
	);

	public static final EntityDataAccessor<EntityPattern> ENTITY_PATTERN = SynchedEntityData.defineId(AbstractFloralFish.class, AbysmEntityDataSerializers.ENTITY_PATTERN);

	public AbstractFloralFish(EntityType<? extends AbstractFloralFish> entityType, Level world) {
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
		return type == AbysmDataComponents.ENTITY_PATTERN ?
			castComponentValue(type, this.getEntityPattern()) :
			super.get(type);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter from) {
		this.applyImplicitComponentIfPresent(from, AbysmDataComponents.ENTITY_PATTERN);
		super.applyImplicitComponents(from);
	}

	@Override
	protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
		if (type == AbysmDataComponents.ENTITY_PATTERN) {
			this.setEntityPattern(castComponentValue(AbysmDataComponents.ENTITY_PATTERN, value));
			return true;
		} else {
			return super.applyImplicitComponent(type, value);
		}
	}

	@Override
	public void saveToBucketTag(ItemStack stack) {
		super.saveToBucketTag(stack);
		stack.copyFrom(AbysmDataComponents.ENTITY_PATTERN, this);
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
