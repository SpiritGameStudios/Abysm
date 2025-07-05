package dev.spiritstudios.abysm.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import org.joml.Vector3f;

public class OoglyBooglyFumesParticleEffect implements ParticleEffect {
	public static final MapCodec<OoglyBooglyFumesParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			Codecs.RGB.fieldOf("color").forGetter(particle -> particle.color),
			Codec.BOOL.fieldOf("deadly").forGetter(particle -> particle.deadly)
		).apply(instance, OoglyBooglyFumesParticleEffect::new)
	);

	public static final PacketCodec<RegistryByteBuf, OoglyBooglyFumesParticleEffect> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER, particle -> particle.color,
		PacketCodecs.BOOLEAN, particle -> particle.deadly,
		OoglyBooglyFumesParticleEffect::new
	);

	private final int color;
	private final boolean deadly;

	public OoglyBooglyFumesParticleEffect(int color, boolean deadly) {
		this.color = color;
		this.deadly = deadly;
	}

	@Override
	public ParticleType<?> getType() {
		return AbysmParticleTypes.OOGLY_BOOGLY_FUMES;
	}

	public Vector3f getColor() {
		return ColorHelper.toVector(this.color);
	}

	public boolean isDeadly() {
		return deadly;
	}
}
