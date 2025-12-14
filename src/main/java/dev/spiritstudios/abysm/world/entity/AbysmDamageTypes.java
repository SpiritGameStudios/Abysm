package dev.spiritstudios.abysm.world.entity;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.Abysm;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class AbysmDamageTypes {

	public static final ResourceKey<DamageType> HARPOON = of("harpoon");
	public static final ResourceKey<DamageType> CNIDOCYTE_STING = of("cnidocyte_sting");
	public static final ResourceKey<DamageType> PRESSURE = of("pressure");

	private static ResourceKey<DamageType> of(String path) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, Abysm.id(path));
	}

	public static Holder.Reference<DamageType> getOrThrow(Level world, ResourceKey<DamageType> registryKey) {
		return getOrThrow(world.registryAccess(), registryKey);
	}

	public static Holder.Reference<DamageType> getOrThrow(RegistryAccess drm, ResourceKey<DamageType> registryKey) {
		return drm.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(registryKey);
	}

	public static ImmutableMap<ResourceKey<DamageType>, DamageType> asDamageTypes() {
		ImmutableMap.Builder<ResourceKey<DamageType>, DamageType> damageTypes = ImmutableMap.builder();
		damageTypes.put(HARPOON, new DamageType("abysm.harpoon", 0.1F));
		damageTypes.put(CNIDOCYTE_STING, new DamageType("abysm.cnidocyte_sting", 0.2F));
		damageTypes.put(PRESSURE, new DamageType("abysm.pressure", 0.1F));
		return damageTypes.build();
	}

	public static void bootstrap(BootstrapContext<DamageType> damageTypeRegisterable) {
		for (Map.Entry<ResourceKey<DamageType>, DamageType> entry : asDamageTypes().entrySet()) {
			damageTypeRegisterable.register(entry.getKey(), entry.getValue());
		}
	}
}
