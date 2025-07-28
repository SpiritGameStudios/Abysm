package dev.spiritstudios.abysm.client.registry;

import dev.spiritstudios.abysm.client.particle.BloomGlimmerParticle;
import dev.spiritstudios.abysm.client.particle.BloomPetalParticle;
import dev.spiritstudios.abysm.client.particle.OoglyBooglyElectricityParticle;
import dev.spiritstudios.abysm.client.particle.OoglyBooglyFumesParticle;
import dev.spiritstudios.abysm.client.particle.OoglyBooglySparkleParticle;
import dev.spiritstudios.abysm.client.particle.PoggdrygllSporesParticle;
import dev.spiritstudios.abysm.client.particle.SpiralingParticle;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

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

	public static <T extends ParticleEffect> void register(ParticleType<T> type, ParticleFactory<T> provider) {
		getParticleFactoryRegistry().register(type, provider);
	}

	public static <T extends ParticleEffect> void register(ParticleType<T> type, ParticleFactory.BlockLeakParticleFactory<T> provider) {
		getParticleFactoryRegistry().register(
			type,
			prov -> (options, level, d, e, f, g, h, i) -> {
				SpriteBillboardParticle particle = provider.createParticle(
					options, level, d, e, f, g, h, i
				);
				if (particle != null) {
					particle.setSprite(prov);
				}

				return particle;
			});
	}

	public static <T extends ParticleEffect> void register(ParticleType<T> type, ParticleRegistration<T> registration) {
		getParticleFactoryRegistry().register(type, registration::create);
	}

	private static ParticleFactoryRegistry getParticleFactoryRegistry() {
		return ParticleFactoryRegistry.getInstance();
	}

	@FunctionalInterface
	public interface ParticleRegistration<T extends ParticleEffect> {
		ParticleFactory<T> create(SpriteProvider sprites);
	}
}
