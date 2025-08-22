package dev.spiritstudios.abysm.entity.leviathan.pseudo;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.entity.leviathan.GeoChainLeviathan;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;

import java.util.List;

public class SkeletonSharkEntity extends GeoChainLeviathan implements EcologicalEntity {

	public final List<LeviathanPart> parts;
	public final EcosystemLogic ecosystemLogic = this.createEcosystemLogic(this);

	public SkeletonSharkEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		ImmutableList.Builder<LeviathanPart> builder = ImmutableList.builder();
		float width = entityType.getWidth();
		float height = entityType.getHeight();
		for (int i = 0; i < 4; i++) {
			LeviathanPart part = new LeviathanPart(this, "tail" + i, width, height);
			part.setRelativePos(new Vec3d(0, 0, i + 1));
			builder.add(part);
		}
		this.parts = builder.build();
	}

	@Override
	public List<? extends LeviathanPart> getSpecterEntityParts() {
		return this.parts;
	}

	@Override
	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return super.isValidNonPlayerTarget(living) && this.getEcosystemType().prey().contains(living.getType());
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
		AnimationController<SkeletonSharkEntity> animController = new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM));

		registrar.add(animController);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.alertEcosystemOfSpawn();
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SKELETON_SHARK;
	}

	@Override
	public void onRemove(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemove(reason);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	public static DefaultAttributeContainer.Builder createSansAttributes() {
		return MobEntity.createMobAttributes()
			.add();
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(4, new SwimAroundGoal(this, 1.0, 10));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.targetSelector.add(1, new HuntPreyGoal(this, false));
	}
}
