package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.duck.CarverContextDuckInterface;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CarverContext.class)
public abstract class CarverContextMixin extends HeightContext implements CarverContextDuckInterface {
	private CarverContextMixin(ChunkGenerator generator, HeightLimitView world) {
		super(generator, world);
	}

	@Unique
	private StructureWeightSamplerDuckInterface sampler;

	@Override
	public void abysm$setSampler(StructureWeightSamplerDuckInterface sampler) {
		this.sampler = sampler;
	}

	@Override
	public @Nullable StructureWeightSamplerDuckInterface abysm$getSampler() {
		return this.sampler;
	}
}
