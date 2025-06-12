package dev.spiritstudios.abysm.client.registry;

import dev.spiritstudios.abysm.client.particle.BloomGlimmerParticle;
import dev.spiritstudios.abysm.client.particle.BloomPetalParticle;
import dev.spiritstudios.abysm.registry.AbysmParticleTypes;
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
