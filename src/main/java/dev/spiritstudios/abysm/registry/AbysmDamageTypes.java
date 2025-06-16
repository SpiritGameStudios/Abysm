package dev.spiritstudios.abysm.registry;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.Map;

public class AbysmDamageTypes {

	public static final RegistryKey<DamageType> HARPOON = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Abysm.id("harpoon"));
	public static final RegistryKey<DamageType> CNIDOCYTE_STING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Abysm.id("cnidocyte_sting"));
	public static final RegistryKey<DamageType> PRESSURE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Abysm.id("pressure"));

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
