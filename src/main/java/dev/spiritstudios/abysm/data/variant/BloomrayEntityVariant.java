package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.block.Block;

public class BloomrayEntityVariant extends AbstractEntityVariant {
	public static final Codec<BloomrayEntityVariant> CODEC = RecordCodecBuilder.create(
		instance -> fillFields(instance).and(instance.group(
				HideableCrownType.CODEC.fieldOf("crown").forGetter(variant -> variant.hideableCrownType),
				ParticleTypes.CODEC.fieldOf("glimmer").forGetter(variant -> variant.glimmerParticle),
				ParticleTypes.CODEC.fieldOf("thorns").forGetter(variant -> variant.thornsParticle)
			)
		).apply(instance, BloomrayEntityVariant::new)
	);

	public static final Codec<Holder<BloomrayEntityVariant>> ENTRY_CODEC = RegistryFixedCodec.create(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT);

	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BloomrayEntityVariant>> ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT);

	public static Holder<BloomrayEntityVariant> getDefaultEntry(RegistryAccess registryManager) {
		return getDefaultEntry(registryManager.lookupOrThrow(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT));
	}

	public static Holder<BloomrayEntityVariant> getDefaultEntry(HolderGetter<BloomrayEntityVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.ROSY_BLOOMRAY);
	}

	public final HideableCrownType hideableCrownType;
	public final ParticleOptions glimmerParticle;
	public final ParticleOptions thornsParticle;

	public BloomrayEntityVariant(Component name, Identifier texture, SpawnPrioritySelectors spawnConditions, HideableCrownType hideableCrownType, ParticleOptions glimmerParticle, ParticleOptions thornsParticle) {
		super(name, texture, spawnConditions);
		this.hideableCrownType = hideableCrownType;
		this.glimmerParticle = glimmerParticle;
		this.thornsParticle = thornsParticle;
	}

	// Hiding crown type
	// Uses this hard-coded enum instead of a block codec to hopefully reduce lag with the block finding AI
	public enum HideableCrownType implements StringRepresentable {
		SODALITE_CROWN(AbysmBlocks.BLOOMING_SODALITE_CROWN, "sodalite"),
		ANYOLITE_CROWN(AbysmBlocks.BLOOMING_ANYOLITE_CROWN, "anyolite"),
		MELILITE_CROWN(AbysmBlocks.BLOOMING_MELILITE_CROWN, "melilite");

		public static final Codec<BloomrayEntityVariant.HideableCrownType> CODEC = StringRepresentable.fromEnum(BloomrayEntityVariant.HideableCrownType::values);
		private final Block crown;
		private final String id;

		HideableCrownType(Block crown, String id) {
			this.crown = crown;
			this.id = id;
		}

		public Block getCrown() {
			return crown;
		}

		public String getId() {
			return id;
		}

		@Override
		public String getSerializedName() {
			return this.getId();
		}
	}
}
