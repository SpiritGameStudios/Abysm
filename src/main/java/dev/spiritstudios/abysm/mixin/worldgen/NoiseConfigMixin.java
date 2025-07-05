package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.duck.NoiseConfigDuckInterface;
import dev.spiritstudios.abysm.worldgen.noise.NoiseConfigAttachment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NoiseConfig.class)
public abstract class NoiseConfigMixin implements NoiseConfigDuckInterface {

	@Shadow
	public abstract DoublePerlinNoiseSampler getOrCreateSampler(RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersKey);

	@Unique
	private final NoiseConfigAttachment attachment = new NoiseConfigAttachment();

	@Override
	public void abysm$attachBonusFunctions(ServerWorld world) {
		this.attachment.attachBonusFunctions(world, this::getOrCreateSampler);
	}

	@Override
	public NoiseConfigAttachment abysm$getAttachment() {
		return this.attachment;
	}
}
