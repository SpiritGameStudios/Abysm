package dev.spiritstudios.abysm.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;

public class AbysmCodecs {
	public static final Codec<SimpleParticleType> PARTICLE_TYPE_CODEC = Registries.PARTICLE_TYPE
		.getCodec()
		.comapFlatMap(
			particleType -> particleType instanceof SimpleParticleType simpleParticleType
				? DataResult.success(simpleParticleType)
				: DataResult.error(() -> "Not a SimpleParticleType: " + particleType),
			particleType -> particleType
		);
}
