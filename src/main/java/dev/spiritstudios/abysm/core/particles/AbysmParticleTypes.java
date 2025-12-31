package dev.spiritstudios.abysm.core.particles;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import java.util.function.Function;

public final class AbysmParticleTypes {
	public static final SimpleParticleType ROSEBLOOM_GLIMMER = registerSimple("rosebloom_glimmer", false);
	public static final SimpleParticleType SUNBLOOM_GLIMMER = registerSimple("sunbloom_glimmer", false);
	public static final SimpleParticleType MALLOWBLOOM_GLIMMER = registerSimple("mallowbloom_glimmer", false);

	public static final SimpleParticleType ROSEBLOOM_PETALS = registerSimple("rosebloom_petals", false);
	public static final SimpleParticleType SUNBLOOM_PETALS = registerSimple("sunbloom_petals", false);
	public static final SimpleParticleType MALLOWBLOOM_PETALS = registerSimple("mallowbloom_petals", false);

	public static final SimpleParticleType SODALITE_THORNS = registerSimple("sodalite_thorns", false);
	public static final SimpleParticleType ANYOLITE_THORNS = registerSimple("anyolite_thorns", false);
	public static final SimpleParticleType MELILITE_THORNS = registerSimple("melilite_thorns", false);

	public static final SimpleParticleType OOGLY_BOOGLY_SPARKLE = registerSimple("oogly_boogly_sparkle", false);
	public static final SimpleParticleType OOGLY_BOOGLY_ELECTRICITY = registerSimple("oogly_boogly_electricity", false);
	public static final ParticleType<OoglyBooglyFumesParticleEffect> OOGLY_BOOGLY_FUMES = register("oogly_boogly_fumes", false, type -> OoglyBooglyFumesParticleEffect.CODEC, type -> OoglyBooglyFumesParticleEffect.STREAM_CODEC);
	public static final SimpleParticleType OOGLY_BOOGLY_ELECTRICITY_SPIRAL = registerSimple("oogly_boogly_electricity_spiral", false);
	public static final SimpleParticleType OOGLY_BOOGLY_ELECTRICITY_SPECK = registerSimple("oogly_boogly_electricity_speck", false);

	public static final SimpleParticleType POGGDRYGLL_SPORES = registerSimple("poggdrygll_spores", false);

	public static void init() {
		// NO-OP
	}

	private static ResourceKey<ParticleType<?>> keyOf(String id) {
		return ResourceKey.create(Registries.PARTICLE_TYPE, Abysm.id(id));
	}

	@SuppressWarnings("SameParameterValue")
	private static SimpleParticleType registerSimple(String name, boolean alwaysShow) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, keyOf(name), FabricParticleTypes.simple(alwaysShow));
	}

	private static <T extends ParticleOptions> ParticleType<T> register(
		String name,
		boolean alwaysShow,
		Function<ParticleType<T>, MapCodec<T>> codecGetter,
		Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> packetCodecGetter
	) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, keyOf(name), new ParticleType<T>(alwaysShow) {
			@Override
			public MapCodec<T> codec() {
				return codecGetter.apply(this);
			}

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return packetCodecGetter.apply(this);
			}
		});
	}
}
