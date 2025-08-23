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
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;

import java.util.List;

public class SkeletonSharkEntity extends GeoChainLeviathan implements EcologicalEntity {

	public final List<SkeletonSharkPart> parts;
	public final EcosystemLogic ecosystemLogic = this.createEcosystemLogic(this);

	public SkeletonSharkEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		ImmutableList.Builder<SkeletonSharkPart> builder = ImmutableList.builder();
		builder.add(new SkeletonSharkPart(this, "body", 2F, 1F, -0.1F));
		builder.add(new SkeletonSharkPart(this, "tail", 2F, 1F, 1F));
		this.parts = builder.build();
	}

	@Override
	protected @NotNull ServerBossBar createBossBar(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		ServerBossBar bar = super.createBossBar(entityType, world);
		bar.setColor(BossBar.Color.WHITE);
		return bar;
	}

	@Override
	public List<SkeletonSharkPart> getSpecterEntityParts() {
		return this.parts;
	}

	@Override
	public float getDistanceToMainBody(LeviathanPart leviathanPart) {
		return leviathanPart instanceof SkeletonSharkPart part ? part.originalDistance : super.getDistanceToMainBody(leviathanPart);
	}

	@Override
	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return super.isValidNonPlayerTarget(living) && this.getEcosystemType().prey().contains(living.getType());
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
		AnimationController<SkeletonSharkEntity> animController = new AnimationController<>(0, event -> PlayState.STOP);

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
		return Leviathan.createLeviathanAttributes()
			.add(EntityAttributes.ATTACK_DAMAGE, 15);
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

	@Override
	public float damageResist() {
		return 200F;
	}
}
