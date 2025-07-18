package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.component.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.entity.pattern.Patternable;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.DyeColor;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;

import java.util.List;

// TODO - Dropped fish item & bucket item
public abstract class AbstractFloralFishEntity extends AbstractSchoolingFishEntity implements GeoEntity, Patternable {
	public static final List<Integer> PATTERN_COLORS = List.of(
		DyeColor.WHITE.getEntityColor(), DyeColor.BLACK.getEntityColor(),
		DyeColor.BLUE.getEntityColor(), DyeColor.LIGHT_BLUE.getEntityColor(), DyeColor.CYAN.getEntityColor(),
		DyeColor.PINK.getEntityColor(), DyeColor.PURPLE.getEntityColor(), DyeColor.MAGENTA.getEntityColor(),
		DyeColor.RED.getEntityColor(), DyeColor.YELLOW.getEntityColor(), DyeColor.LIME.getEntityColor()
	);

	public static final TrackedData<EntityPattern> ENTITY_PATTERN = DataTracker.registerData(AbstractFloralFishEntity.class, AbysmTrackedDataHandlers.ENTITY_PATTERN);

	public AbstractFloralFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(ENTITY_PATTERN, EntityPattern.EMPTY_PATTERN);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		AnimationController<AbstractFloralFishEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> PlayState.STOP);

		controllerRegistrar.add(animController);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		entityData = super.initialize(world, difficulty, spawnReason, entityData);
		EntityPattern pattern = this.getPatternForInitialize(world, this, entityData);
		this.setEntityPattern(pattern);
		return entityData;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		RegistryOps<NbtElement> ops = getRegistryManager().getOps(NbtOps.INSTANCE);

		this.writeEntityPatternNbt(ops, nbt);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		RegistryOps<NbtElement> ops = getRegistryManager().getOps(NbtOps.INSTANCE);

		this.readEntityPatternNbt(this, ops, nbt);
	}

	@Nullable
	@Override
	public <T> T get(ComponentType<? extends T> type) {
		return type == AbysmDataComponentTypes.ENTITY_PATTERN ?
			castComponentValue(type, this.getEntityPattern()) :
			super.get(type);
	}

	@Override
	protected void copyComponentsFrom(ComponentsAccess from) {
		this.copyComponentFrom(from, AbysmDataComponentTypes.ENTITY_PATTERN);
		super.copyComponentsFrom(from);
	}

	@Override
	protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
		if (type == AbysmDataComponentTypes.ENTITY_PATTERN) {
			this.setEntityPattern(castComponentValue(AbysmDataComponentTypes.ENTITY_PATTERN, value));
			return true;
		} else {
			return super.setApplicableComponent(type, value);
		}
	}

	@Override
	public void copyDataToStack(ItemStack stack) {
		super.copyDataToStack(stack);
		stack.copy(AbysmDataComponentTypes.ENTITY_PATTERN, this);
	}

	@Override
	public EntityPattern getEntityPattern() {
		return this.dataTracker.get(ENTITY_PATTERN);
	}

	@Override
	public void setEntityPattern(EntityPattern pattern) {
		this.dataTracker.set(ENTITY_PATTERN, pattern);
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
