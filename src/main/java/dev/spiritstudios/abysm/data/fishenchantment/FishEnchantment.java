package dev.spiritstudios.abysm.data.fishenchantment;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.attribute.AbysmAttribute;
import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;

public record FishEnchantment(List<AbysmAttribute> modifiers, ResourceLocation rendererId) {
	public static final Codec<FishEnchantment> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				AbysmAttribute.CODEC.listOf().fieldOf("modifiers").forGetter(component -> component.modifiers),
				ResourceLocation.CODEC.fieldOf("renderer_id").forGetter(component -> component.rendererId)
			)
			.apply(instance, FishEnchantment::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<FishEnchantment>> ENTRY_PACKET_CODEC = ByteBufCodecs.holderRegistry(AbysmRegistryKeys.FISH_ENCHANTMENT);

	public static final Codec<Holder<FishEnchantment>> ENTRY_CODEC = RegistryFixedCodec.create(AbysmRegistryKeys.FISH_ENCHANTMENT);

	public static Holder<FishEnchantment> getDefaultEntry(RegistryAccess registryManager) {
		return getDefaultEntry(registryManager.lookupOrThrow(AbysmRegistryKeys.FISH_ENCHANTMENT));
	}

	public static Holder<FishEnchantment> getDefaultEntry(HolderGetter<FishEnchantment> lookup) {
		return lookup.getOrThrow(AbysmFishEnchantments.NONE);
	}

	@SuppressWarnings("unused")
	@Nullable
	public ResourceLocation getId(Level world) {
		return this.getId(world.registryAccess());
	}

	@Nullable
	public ResourceLocation getId(RegistryAccess drm) {
		return drm.lookup(AbysmRegistryKeys.FISH_ENCHANTMENT)
			.map(registry ->
				registry.getKey(this))
			.orElse(null);
	}

	public void applyModifiers(BiConsumer<Holder<Attribute>, AttributeModifier> attributeConsumer) {
		for (AbysmAttribute entry : this.modifiers) {
			attributeConsumer.accept(entry.attribute(), entry.modifier());
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final ImmutableList.Builder<AbysmAttribute> entries;
		private ResourceLocation rendererId = null;

		Builder() {
			entries = ImmutableList.builder();
		}

		public Builder id(ResourceLocation rendererId) {
			this.rendererId = rendererId;
			return this;
		}

		public Builder add(AbysmAttribute entry) {
			this.entries.add(entry);
			return this;
		}

		public Builder add(Holder<Attribute> attribute, AttributeModifier modifier) {
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
