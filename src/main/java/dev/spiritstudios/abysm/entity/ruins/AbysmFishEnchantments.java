package dev.spiritstudios.abysm.entity.ruins;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AbysmFishEnchantments {
	public static final ResourceKey<FishEnchantment> NONE = ofKey("none");
	public static final ResourceKey<FishEnchantment> JAW = ofKey("jaw");
	public static final ResourceKey<FishEnchantment> SHELL = ofKey("shell");
	public static final ResourceKey<FishEnchantment> JET = ofKey("jet");
	public static final ResourceKey<FishEnchantment> OBFUSCATED = ofKey("obfuscated");

	private static ResourceKey<FishEnchantment> ofKey(String key) {
		return ResourceKey.create(AbysmRegistryKeys.FISH_ENCHANTMENT, Abysm.id(key));
	}

	private static void register(BootstrapContext<FishEnchantment> registerable, ResourceKey<FishEnchantment> key,Holder<Attribute> attribute, double value) {
		registerable.register(key, FishEnchantment.builder()
			.id(key.location())
			.add(
				attribute,
				new AttributeModifier(
					key.location(),
					value,
					AttributeModifier.Operation.ADD_VALUE
				)
			).build());
	}

	public static void bootstrap(BootstrapContext<FishEnchantment> registerable) {
		registerable.register(NONE, FishEnchantment.builder().id(NONE.location()).build());

		register(registerable, JAW, Attributes.ATTACK_DAMAGE, 2);
		register(registerable, SHELL, Attributes.ARMOR, 10);
		register(registerable, JET, Attributes.MOVEMENT_SPEED, 0.4);
		register(registerable, OBFUSCATED, Attributes.LUCK, 1);
	}
}
