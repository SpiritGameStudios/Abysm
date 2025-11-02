package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.duck.RandomStateDuckInterface;
import dev.spiritstudios.abysm.worldgen.noise.NoiseConfigAttachment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RandomState.class)
public abstract class RandomStateMixin implements RandomStateDuckInterface {
	@Shadow
	public abstract NormalNoise getOrCreateNoise(ResourceKey<NormalNoise.NoiseParameters> noiseParametersKey);

	@Unique
	private final NoiseConfigAttachment abysm$attachment = new NoiseConfigAttachment();

	@Override
	public void abysm$attachBonusFunctions(ServerLevel world) {
		this.abysm$attachment.attachBonusFunctions(world, this::getOrCreateNoise);
	}

	@Override
	public NoiseConfigAttachment abysm$getAttachment() {
		return this.abysm$attachment;
	}
}
