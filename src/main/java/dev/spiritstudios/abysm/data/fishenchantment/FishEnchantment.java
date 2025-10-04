package dev.spiritstudios.abysm.data.fishenchantment;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.AbysmAttribute;
import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public record FishEnchantment(List<AbysmAttribute> modifiers, Identifier rendererId) {
	public static final Codec<FishEnchantment> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				AbysmAttribute.CODEC.listOf().fieldOf("modifiers").forGetter(component -> component.modifiers),
				Identifier.CODEC.fieldOf("renderer_id").forGetter(component -> component.rendererId)
			)
			.apply(instance, FishEnchantment::new)
	);

	public static final PacketCodec<RegistryByteBuf, RegistryEntry<FishEnchantment>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(AbysmRegistryKeys.FISH_ENCHANTMENT);

	public static final Codec<RegistryEntry<FishEnchantment>> ENTRY_CODEC = RegistryFixedCodec.of(AbysmRegistryKeys.FISH_ENCHANTMENT);

	public static RegistryEntry<FishEnchantment> getDefaultEntry(DynamicRegistryManager registryManager) {
		return getDefaultEntry(registryManager.getOrThrow(AbysmRegistryKeys.FISH_ENCHANTMENT));
	}

	public static RegistryEntry<FishEnchantment> getDefaultEntry(RegistryEntryLookup<FishEnchantment> lookup) {
		return lookup.getOrThrow(AbysmFishEnchantments.NONE);
	}

	@SuppressWarnings("unused")
	@Nullable
	public Identifier getId(World world) {
		return this.getId(world.getRegistryManager());
	}

	@Nullable
	public Identifier getId(DynamicRegistryManager drm) {
		return drm.getOptional(AbysmRegistryKeys.FISH_ENCHANTMENT)
			.map(registry ->
				registry.getId(this))
			.orElse(null);
	}

	public void applyModifiers(BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> attributeConsumer) {
		for (AbysmAttribute entry : this.modifiers) {
			attributeConsumer.accept(entry.attribute(), entry.modifier());
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final ImmutableList.Builder<AbysmAttribute> entries;
		private Identifier rendererId = null;

		Builder() {
			entries = ImmutableList.builder();
		}

		public Builder id(Identifier rendererId) {
			this.rendererId = rendererId;
			return this;
		}

		public Builder add(AbysmAttribute entry) {
			this.entries.add(entry);
			return this;
		}

		public Builder add(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
			this.entries.add(new AbysmAttribute(attribute, modifier));
			return this;
		}

		public FishEnchantment build() {
			if (this.rendererId == null) {
				throw new IllegalStateException("rendererId must not be null!");
			}
			return new FishEnchantment(this.entries.build(), rendererId);
		}
	}
}
