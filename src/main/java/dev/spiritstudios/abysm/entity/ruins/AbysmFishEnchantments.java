package dev.spiritstudios.abysm.entity.ruins;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;

public class AbysmFishEnchantments {
	public static final RegistryKey<FishEnchantment> NONE = of("none");
	public static final RegistryKey<FishEnchantment> JAW = of("jaw");
	public static final RegistryKey<FishEnchantment> SHELL = of("shell");
	public static final RegistryKey<FishEnchantment> JET = of("jet");
	public static final RegistryKey<FishEnchantment> OBFUSCATED = of("obfuscated");

	private static RegistryKey<FishEnchantment> of(String path) {
		return RegistryKey.of(AbysmRegistries.FISH_ENCHANTMENT, Abysm.id(path));
	}

	public static ImmutableMap<RegistryKey<FishEnchantment>, FishEnchantment> asEnchantments(Registerable<FishEnchantment> registerable) {
		ImmutableMap.Builder<RegistryKey<FishEnchantment>, FishEnchantment> builder = ImmutableMap.builder();
		builder.put(NONE, FishEnchantment.DEFAULT);
		create(builder, JAW, EntityAttributes.ATTACK_DAMAGE, 2);
		create(builder, SHELL, EntityAttributes.ARMOR, 10);
		create(builder, JET, EntityAttributes.MOVEMENT_SPEED, 0.7);
		create(builder, OBFUSCATED, EntityAttributes.LUCK, 1);
		return builder.build();
	}

	private static void create(ImmutableMap.Builder<RegistryKey<FishEnchantment>, FishEnchantment> builder, RegistryKey<FishEnchantment> registryKey, RegistryEntry<EntityAttribute> attribute, double value) {
		builder.put(registryKey, FishEnchantment.builder()
			.add(attribute,
				new EntityAttributeModifier(registryKey.getValue(),
					value,
					EntityAttributeModifier.Operation.ADD_VALUE))
			.build());
	}

	public static void bootstrap(Registerable<FishEnchantment> registerable) {
		for (Map.Entry<RegistryKey<FishEnchantment>, FishEnchantment> entry : asEnchantments(registerable).entrySet()) {
			registerable.register(entry.getKey(), entry.getValue());
		}
	}
}
