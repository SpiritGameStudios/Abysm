package dev.spiritstudios.abysm.registry;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class AbysmParticleTypes {
	public static final SimpleParticleType ROSEBLOOM_GLIMMER = registerSimple("rosebloom_glimmer", false);
	public static final SimpleParticleType SUNBLOOM_GLIMMER = registerSimple("sunbloom_glimmer", false);
	public static final SimpleParticleType MALLOWBLOOM_GLIMMER = registerSimple("mallowbloom_glimmer", false);

	public static void init() {
		// NO-OP
	}

	private static RegistryKey<ParticleType<?>> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.PARTICLE_TYPE, Abysm.id(id));
	}

	private static SimpleParticleType registerSimple(String name, boolean alwaysShow) {
		return Registry.register(Registries.PARTICLE_TYPE, keyOf(name), FabricParticleTypes.simple(alwaysShow));
	}

	private static <T extends ParticleEffect> ParticleType<T> register(
		String name,
		boolean alwaysShow,
		Function<ParticleType<T>, MapCodec<T>> codecGetter,
		Function<ParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter
	) {
		return Registry.register(Registries.PARTICLE_TYPE, keyOf(name), new ParticleType<T>(alwaysShow) {
			@Override
			public MapCodec<T> getCodec() {
				return codecGetter.apply(this);
			}

			@Override
			public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
				return packetCodecGetter.apply(this);
			}
		});
	}
}
