package dev.spiritstudios.abysm.world.ecosystem.registry;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record EcosystemData(
	Set<EntityType<?>> predators,
	Set<EntityType<?>> prey,
	Set<Block> plants,
	int targetPopulation,
	IntProvider huntDuration,
	float favorChance,
	FloatProvider favoredHuntSpeed, FloatProvider unfavoredHuntSpeed,
	IntProvider breedingCooldown,
	IntProvider litterSize
) {
	public static final Codec<EcosystemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		EntityType.CODEC.listOf().<Set<EntityType<?>>>xmap(
			HashSet::new,
			ArrayList::new
		).optionalFieldOf("predators", Collections.emptySet()).forGetter(EcosystemData::predators),
		EntityType.CODEC.listOf().<Set<EntityType<?>>>xmap(
			HashSet::new,
			ArrayList::new
		).optionalFieldOf("prey", Collections.emptySet()).forGetter(EcosystemData::prey),
		BuiltInRegistries.BLOCK.byNameCodec().listOf().<Set<Block>>xmap(
			HashSet::new,
			ArrayList::new
		).optionalFieldOf("plants", Collections.emptySet()).forGetter(EcosystemData::plants),
		ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("target_population", 7).forGetter(EcosystemData::targetPopulation),
		IntProvider.NON_NEGATIVE_CODEC.optionalFieldOf(
			"hunt_duration",
			UniformInt.of(
				60 * SharedConstants.TICKS_PER_SECOND,
				80 * SharedConstants.TICKS_PER_SECOND
			)
		).forGetter(EcosystemData::huntDuration),
		Codec.FLOAT.optionalFieldOf("favor_chance", 0.85F).forGetter(EcosystemData::favorChance),
		FloatProvider.CODEC.fieldOf("favored_speed").forGetter(EcosystemData::favoredHuntSpeed),
		FloatProvider.CODEC.fieldOf("unfavored_speed").forGetter(EcosystemData::unfavoredHuntSpeed),
		IntProvider.NON_NEGATIVE_CODEC.fieldOf("breeding_cooldown").forGetter(EcosystemData::breedingCooldown),
		IntProvider.POSITIVE_CODEC.fieldOf("litter_size").forGetter(EcosystemData::litterSize)
	).apply(instance, EcosystemData::new));

	public static class Builder {
		private ImmutableSet<EntityType<?>> predators = ImmutableSet.of();
		private ImmutableSet<EntityType<?>> prey = ImmutableSet.of();
		private ImmutableSet<Block> plants = ImmutableSet.of();

		private int targetPopulation = 7;

		private IntProvider huntDuration = UniformInt.of(60 * SharedConstants.TICKS_PER_SECOND, 80 * SharedConstants.TICKS_PER_SECOND);

		private float favorChance = 0.85f;
		private FloatProvider favoredHuntSpeed = ConstantFloat.of(0.5f);
		private FloatProvider unfavoredHuntSpeed = ConstantFloat.of(-0.25f);

		private IntProvider breedCooldown = ConstantInt.of(15 * SharedConstants.TICKS_PER_SECOND);
		private IntProvider litterSize = ConstantInt.of(1);

		@SafeVarargs
		public final Builder predators(EntityType<? extends LivingEntity>... predators) {
			this.predators = ImmutableSet.copyOf(predators);
			return this;
		}

		@SafeVarargs
		public final Builder prey(EntityType<? extends LivingEntity>... prey) {
			this.prey = ImmutableSet.copyOf(prey);
			return this;
		}

		public Builder plants(Block... plants) {
			this.plants = ImmutableSet.copyOf(plants);
			return this;
		}

		public Builder targetPopulation(int targetPopulation) {
			this.targetPopulation = targetPopulation;
			return this;
		}

		public Builder huntDuration(IntProvider huntDuration) {
			this.huntDuration = huntDuration;
			return this;
		}

		public Builder favorChance(@Range(from = 0, to = 1) float favorChance) {
			this.favorChance = favorChance;
			return this;
		}

		public Builder favoredHuntSpeed(FloatProvider favoredHuntSpeed) {
			this.favoredHuntSpeed = favoredHuntSpeed;
			return this;
		}

		public Builder unfavoredHuntSpeed(FloatProvider unfavoredHuntSpeed) {
			this.unfavoredHuntSpeed = unfavoredHuntSpeed;
			return this;
		}

		public Builder breedCooldown(IntProvider breedCooldown) {
			this.breedCooldown = breedCooldown;
			return this;
		}

		public Builder litterSize(IntProvider litterSize) {
			this.litterSize = litterSize;
			return this;
		}

		public EcosystemData build() {
			return new EcosystemData(
				this.predators, this.prey, this.plants,
				this.targetPopulation,
				this.huntDuration,
				this.favorChance,
				this.favoredHuntSpeed, this.unfavoredHuntSpeed,
				this.breedCooldown,
				this.litterSize
			);
		}
	}
}
