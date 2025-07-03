package dev.spiritstudios.abysm.data.fishenchantment;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FishEnchantment(List<Entry> modifiers) {

	public static final Codec<FishEnchantment> CODEC = Entry.CODEC.listOf().xmap(FishEnchantment::new, enchantment -> enchantment.modifiers);
	public static final PacketCodec<RegistryByteBuf, FishEnchantment> PACKET_CODEC = PacketCodecs.registryValue(AbysmRegistries.FISH_ENCHANTMENT);

	public static final FishEnchantment DEFAULT = builder().build();

	public static Registry<FishEnchantment> getRegistry(World world) {
		return getRegistry(world.getRegistryManager());
	}

	public static Registry<FishEnchantment> getRegistry(DynamicRegistryManager drm) {
		return drm.getOrThrow(AbysmRegistries.FISH_ENCHANTMENT);
	}

	@SuppressWarnings("unused")
	@Nullable
	public Identifier getId(World world) {
		return this.getId(world.getRegistryManager());
	}

	@Nullable
	public Identifier getId(DynamicRegistryManager drm) {
		return drm.getOptional(AbysmRegistries.FISH_ENCHANTMENT)
			.map(registry ->
				registry.getId(this))
			.orElse(null);
	}

	public static Builder builder() {
		return new Builder();
	}

	@SuppressWarnings("unused")
	public Builder copy() {
		return this.copyWithExtraSize(0);
	}

	public Builder copyWithExtraSize(int extra) {
		Builder builder = new Builder(this.modifiers.size() + extra);
		for (Entry entry : this.modifiers) {
			builder.add(entry.attribute, entry.modifier);
		}
		return builder;
	}

	public static class Builder {
		private final ImmutableList.Builder<Entry> entries;

		Builder() {
			entries = ImmutableList.builder();
		}

		Builder(int expectedSize) {
			entries = ImmutableList.builderWithExpectedSize(expectedSize);
		}

		public Builder add(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
			this.entries.add(new Entry(attribute, modifier));
			return this;
		}

		public FishEnchantment build() {
			return new FishEnchantment(this.entries.build());
		}
	}

	@SuppressWarnings("unused")
	public record Entry(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
		public static final Codec<Entry> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					EntityAttribute.CODEC.fieldOf("type").forGetter(entry -> entry.attribute),
					EntityAttributeModifier.MAP_CODEC.forGetter(entry -> entry.modifier)
				)
				.apply(instance, Entry::new)
		);

		public static final PacketCodec<RegistryByteBuf, Entry> PACKET_CODEC = PacketCodec.tuple(
			EntityAttribute.PACKET_CODEC, entry -> entry.attribute,
			EntityAttributeModifier.PACKET_CODEC, entry -> entry.modifier,
			Entry::new
		);

		public boolean matches(RegistryEntry<EntityAttribute> attribute, Identifier modifierId) {
			return attribute.equals(this.attribute) && this.modifier.idMatches(modifierId);
		}
	}
}
