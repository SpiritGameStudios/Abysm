package dev.spiritstudios.abysm.entity;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.Map;

public class AbysmDamageTypes {

	public static final RegistryKey<DamageType> HARPOON = of("harpoon");
	public static final RegistryKey<DamageType> CNIDOCYTE_STING = of("cnidocyte_sting");
	public static final RegistryKey<DamageType> PRESSURE = of("pressure");

	private static RegistryKey<DamageType> of(String path) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Abysm.id(path));
	}

	public static RegistryEntry.Reference<DamageType> getOrThrow(World world, RegistryKey<DamageType> registryKey) {
		return getOrThrow(world.getRegistryManager(), registryKey);
	}

	public static RegistryEntry.Reference<DamageType> getOrThrow(DynamicRegistryManager drm, RegistryKey<DamageType> registryKey) {
		return drm.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(registryKey);
	}

	public static ImmutableMap<RegistryKey<DamageType>, DamageType> asDamageTypes() {
		ImmutableMap.Builder<RegistryKey<DamageType>, DamageType> damageTypes = ImmutableMap.builder();
		damageTypes.put(HARPOON, new DamageType("abysm.harpoon", 0.1F));
		damageTypes.put(CNIDOCYTE_STING, new DamageType("abysm.cnidocyte_sting", 0.2F));
		damageTypes.put(PRESSURE, new DamageType("abysm.pressure", 0.1F));
		return damageTypes.build();
	}

	public static void bootstrap(Registerable<DamageType> damageTypeRegisterable) {
		for (Map.Entry<RegistryKey<DamageType>, DamageType> entry : asDamageTypes().entrySet()) {
			damageTypeRegisterable.register(entry.getKey(), entry.getValue());
		}
	}
}
