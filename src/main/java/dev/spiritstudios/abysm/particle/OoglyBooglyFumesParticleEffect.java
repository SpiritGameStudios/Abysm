package dev.spiritstudios.abysm.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.util.AbysmCodecs;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public record OoglyBooglyFumesParticleEffect(int color, boolean deadly) implements ParticleEffect {
	public static final MapCodec<OoglyBooglyFumesParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			AbysmCodecs.ARGB.fieldOf("color").forGetter(particle -> particle.color),
			Codec.BOOL.fieldOf("deadly").forGetter(particle -> particle.deadly)
		).apply(instance, OoglyBooglyFumesParticleEffect::new)
	);

	public static final PacketCodec<RegistryByteBuf, OoglyBooglyFumesParticleEffect> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER, particle -> particle.color,
		PacketCodecs.BOOLEAN, particle -> particle.deadly,
		OoglyBooglyFumesParticleEffect::new
	);

	@Override
	public ParticleType<?> getType() {
		return AbysmParticleTypes.OOGLY_BOOGLY_FUMES;
	}
}
