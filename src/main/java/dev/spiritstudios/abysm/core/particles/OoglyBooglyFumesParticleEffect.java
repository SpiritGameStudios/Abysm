package dev.spiritstudios.abysm.core.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.util.AbysmCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record OoglyBooglyFumesParticleEffect(int color, boolean deadly) implements ParticleOptions {
	public static final MapCodec<OoglyBooglyFumesParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			AbysmCodecs.ARGB.fieldOf("color").forGetter(particle -> particle.color),
			Codec.BOOL.fieldOf("deadly").forGetter(particle -> particle.deadly)
		).apply(instance, OoglyBooglyFumesParticleEffect::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, OoglyBooglyFumesParticleEffect> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, particle -> particle.color,
		ByteBufCodecs.BOOL, particle -> particle.deadly,
		OoglyBooglyFumesParticleEffect::new
	);

	@Override
	public ParticleType<?> getType() {
		return AbysmParticleTypes.OOGLY_BOOGLY_FUMES;
	}
}
