package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.abysm.client.AbysmClient;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.WaterFogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WaterFogModifier.class)
public abstract class WaterFogModifierMixin {
	@Shadow
	private static long updateTime;

	@WrapMethod(method = "getFogColor")
	private int adjustWaterFogColor(ClientWorld world, Camera camera, int viewDistance, float skyDarkness, Operation<Integer> original) {
		int value = original.call(world, camera, viewDistance, skyDarkness);

		CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
		if (cameraSubmersionType == CameraSubmersionType.WATER) {
			float lightness = 0.5F + 2.0F * MathHelper.clamp(MathHelper.cos(world.getSkyAngle(1.0F) * MathHelper.TAU), -0.25F, 0.25F);
			RegistryEntry<Biome> biome = world.getBiome(BlockPos.ofFloored(camera.getPos()));

			// update underwater visiblity
			float visibilityMultiplier;
			if (biome.matchesKey(AbysmBiomes.FLORAL_REEF)) {
				visibilityMultiplier = 0.3F + 0.7F * lightness;
			} else if (biome.matchesKey(AbysmBiomes.DEEP_SEA_RUINS)) {
				visibilityMultiplier = 0.13F;
			} else {
				visibilityMultiplier = 1.0F;
			}

			if (updateTime < 0L) {
				AbysmClient.underwaterVisibilityMultiplier = visibilityMultiplier;
				AbysmClient.nextUnderwaterVisibilityMultiplier = visibilityMultiplier;
			} else {
				AbysmClient.underwaterVisibilityMultiplier = AbysmClient.nextUnderwaterVisibilityMultiplier;
				AbysmClient.nextUnderwaterVisibilityMultiplier = MathHelper.lerp(0.007F, AbysmClient.underwaterVisibilityMultiplier, visibilityMultiplier);
			}

			// adjust fog color
			if (lightness < 0.999F && biome.matchesKey(AbysmBiomes.FLORAL_REEF)) {
				int nightWaterFogColor = 0x11082F;
				return ColorHelper.lerp(lightness, nightWaterFogColor, value);
			} else {
				return value;
			}
		} else {
			return value;
		}
	}


}
