package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.registry.EntityPatternVariantRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AbysmEntityPatternVariantProvider extends FabricDynamicRegistryProvider {

	public AbysmEntityPatternVariantProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
		entries.addAll(wrapperLookup.getOrThrow(EntityPatternVariantRegistry.ENTITY_PATTERN_VARIANT_KEY));
//		for (RegistryKey<Registry<EntityPatternVariant>> key : EntityPatternVariantRegistry.KEYS) {
//			entries.addAll(wrapperLookup.getOrThrow(key));
//		}
	}

	@Override
	public String getName() {
		return "Abysm Entity Pattern Variant";
	}
}
