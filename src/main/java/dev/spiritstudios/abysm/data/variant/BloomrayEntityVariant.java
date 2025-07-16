package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.block.Block;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public class BloomrayEntityVariant extends AbstractEntityVariant {
	public static final Codec<BloomrayEntityVariant> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			getNameCodec(),
			getTextureCodec(),
			HideableCrownType.CODEC.fieldOf("crown").forGetter(variant -> variant.hideableCrownType),
			ParticleTypes.TYPE_CODEC.fieldOf("glimmer").forGetter(variant -> variant.glimmerParticle),
			ParticleTypes.TYPE_CODEC.fieldOf("thorns").forGetter(variant -> variant.thornsParticle)
		).apply(instance, BloomrayEntityVariant::new)
	);

	public static final Codec<RegistryEntry<BloomrayEntityVariant>> ENTRY_CODEC = RegistryFixedCodec.of(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT);

	public static final PacketCodec<RegistryByteBuf, RegistryEntry<BloomrayEntityVariant>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT);

	public static RegistryEntry<BloomrayEntityVariant> getDefaultEntry(DynamicRegistryManager registryManager) {
		return getDefaultEntry(registryManager.getOrThrow(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT));
	}

	public static RegistryEntry<BloomrayEntityVariant> getDefaultEntry(RegistryEntryLookup<BloomrayEntityVariant> lookup) {
		return lookup.getOrThrow(AbysmEntityVariants.ROSY_BLOOMRAY);
	}

	public final HideableCrownType hideableCrownType;
	public final ParticleEffect glimmerParticle;
	public final ParticleEffect thornsParticle;

	public BloomrayEntityVariant(Text name, Identifier texture, HideableCrownType hideableCrownType, ParticleEffect glimmerParticle, ParticleEffect thornsParticle) {
		super(name, texture);
		this.hideableCrownType = hideableCrownType;
		this.glimmerParticle = glimmerParticle;
		this.thornsParticle = thornsParticle;
	}

	// Getter method(s)
	public HideableCrownType getHideableCrownType() {
		return hideableCrownType;
	}

	// Hiding crown type
	// Uses this hard-coded enum instead of a block codec to hopefully reduce lag with the block finding AI
	public enum HideableCrownType implements StringIdentifiable{
		SODALITE_CROWN(AbysmBlocks.BLOOMING_SODALITE_CROWN, "sodalite"),
		ANYOLITE_CROWN(AbysmBlocks.BLOOMING_ANYOLITE_CROWN, "anyolite"),
		MELILITE_CROWN(AbysmBlocks.BLOOMING_MELILITE_CROWN, "melilite");

		public static final Codec<BloomrayEntityVariant.HideableCrownType> CODEC = StringIdentifiable.createCodec(BloomrayEntityVariant.HideableCrownType::values);
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
		public String asString() {
			return this.getId();
		}
	}
}
