package dev.spiritstudios.abysm.util;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;

public class Codecs {
	public static final MapCodec<SimpleParticleType> PARTICLE_TYPE_CODEC = Registries.PARTICLE_TYPE
		.getCodec()
		.comapFlatMap(
			particleType -> particleType instanceof SimpleParticleType simpleParticleType
				? DataResult.success(simpleParticleType)
				: DataResult.error(() -> "Not a SimpleParticleType: " + particleType),
			particleType -> particleType
		)
		.fieldOf("particle_options");
}
