package dev.spiritstudios.abysm.mixin.ecosystem;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

// TODO: Use specter registry events instead
@Mixin(value = Registries.class, priority = 1000000)
public class RegistriesMixin {

	@Shadow
	@Final
	public static DefaultedRegistry<EntityType<?>> ENTITY_TYPE;

	@WrapMethod(method = "freezeRegistries")
	private static void initMap(Operation<Void> original) {
		original.call();
		AbysmRegistries.ECOSYSTEM_TYPE_REGISTRY.forEach(ecosystemType -> {
			try {
				int rawId = ENTITY_TYPE.getRawIdOrThrow(ecosystemType.entityType());
				EcosystemType.TYPES.put(rawId, ecosystemType);
			} catch (IllegalArgumentException e) {
				Abysm.LOGGER.error("An error occurred when loading the EcosystemType map!", e);
			}
		});
	}
}
