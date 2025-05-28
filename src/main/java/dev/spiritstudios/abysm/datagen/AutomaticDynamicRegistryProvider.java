package dev.spiritstudios.abysm.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

// TODO: Move to specter-registry
public abstract class AutomaticDynamicRegistryProvider<T> extends FabricDynamicRegistryProvider {
	private final RegistryKey<Registry<T>> registryKey;
	private final String namespace;

	public AutomaticDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, RegistryKey<Registry<T>> registryKey, String namespace) {
		super(output, registriesFuture);
		this.registryKey = registryKey;
		this.namespace = namespace;
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
		RegistryWrapper<T> wrapper = wrapperLookup.getOrThrow(registryKey);

		wrapper.streamKeys()
			.filter(key -> key.getValue().getNamespace().equals(namespace))
			.forEach(key -> entries.add(key, wrapper.getOrThrow(key).value()));
	}

	public static <T> FabricDataGenerator.Pack.RegistryDependentFactory<AutomaticDynamicRegistryProvider<T>> factory(RegistryKey<Registry<T>> registryKey, String namespace) {
		return (output, registriesFuture) -> new AutomaticDynamicRegistryProvider<>(output, registriesFuture, registryKey, namespace) {
			@Override
			public String getName() {
				return "Dynamic Registry Entries for " + registryKey.getValue();
			}
		};
	}
}
