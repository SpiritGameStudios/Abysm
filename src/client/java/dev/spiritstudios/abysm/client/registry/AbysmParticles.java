package dev.spiritstudios.abysm.client.registry;

import dev.spiritstudios.abysm.client.particle.BloomGlimmerParticle;
import dev.spiritstudios.abysm.client.particle.BloomPetalParticle;
import dev.spiritstudios.abysm.client.particle.OoglyBooglyElectricityParticle;
import dev.spiritstudios.abysm.client.particle.OoglyBooglyFumesParticle;
import dev.spiritstudios.abysm.client.particle.OoglyBooglySparkleParticle;
import dev.spiritstudios.abysm.client.particle.PoggdrygllSporesParticle;
import dev.spiritstudios.abysm.client.particle.SpiralingParticle;
import dev.spiritstudios.abysm.core.particles.AbysmParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class AbysmParticles {

	public static void init() {
		register(AbysmParticleTypes.ROSEBLOOM_GLIMMER, BloomGlimmerParticle.RosyFactory::new);
		register(AbysmParticleTypes.SUNBLOOM_GLIMMER, BloomGlimmerParticle.SunnyFactory::new);
		register(AbysmParticleTypes.MALLOWBLOOM_GLIMMER, BloomGlimmerParticle.MauveFactory::new);

		register(AbysmParticleTypes.ROSEBLOOM_PETALS, BloomPetalParticle.RosyFactory::new);
		register(AbysmParticleTypes.SUNBLOOM_PETALS, BloomPetalParticle.SunnyFactory::new);
		register(AbysmParticleTypes.MALLOWBLOOM_PETALS, BloomPetalParticle.MauveFactory::new);

		register(AbysmParticleTypes.SODALITE_THORNS, BloomGlimmerParticle.ThornFactory::new);
		register(AbysmParticleTypes.ANYOLITE_THORNS, BloomGlimmerParticle.ThornFactory::new);
		register(AbysmParticleTypes.MELILITE_THORNS, BloomGlimmerParticle.ThornFactory::new);

		register(AbysmParticleTypes.OOGLY_BOOGLY_SPARKLE, OoglyBooglySparkleParticle.Factory::new);
		register(AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY, OoglyBooglyElectricityParticle.Factory::new);
		register(AbysmParticleTypes.OOGLY_BOOGLY_FUMES, OoglyBooglyFumesParticle.Factory::new);
		register(AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY_SPIRAL, SpiralingParticle.ElectricitySpiral::new);
		register(AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY_SPECK, SpiralingParticle.ElectricitySpeck::new);

		register(AbysmParticleTypes.POGGDRYGLL_SPORES, PoggdrygllSporesParticle.Factory::new);
	}

	public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
		getParticleFactoryRegistry().register(type, provider);
	}

	public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleRegistration<T> registration) {
		getParticleFactoryRegistry().register(type, registration::create);
	}

	private static ParticleFactoryRegistry getParticleFactoryRegistry() {
		return ParticleFactoryRegistry.getInstance();
	}

	@FunctionalInterface
	public interface ParticleRegistration<T extends ParticleOptions> {
		ParticleProvider<T> create(SpriteSet sprites);
	}
}
