package dev.spiritstudios.abysm.entity.variant;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.variant.AbstractEntityVariant;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.data.variant.GupGupEntityVariant;
import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import dev.spiritstudios.abysm.particle.OoglyBooglyFumesParticleEffect;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import dev.spiritstudios.abysm.registry.tags.AbysmBiomeTags;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.variant.BiomeCheck;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

public class AbysmEntityVariants {
	// region bloomrays
	public static final ResourceKey<BloomrayEntityVariant> ROSY_BLOOMRAY = ofBloomray("rosy");
	public static final ResourceKey<BloomrayEntityVariant> SUNNY_BLOOMRAY = ofBloomray("sunny");
	public static final ResourceKey<BloomrayEntityVariant> MAUVE_BLOOMRAY = ofBloomray("mauve");

	public static void bloomrayBootstrap(BootstrapContext<BloomrayEntityVariant> registerable) {
		registerBloomrayVariant(
			registerable,
			ROSY_BLOOMRAY,
			BloomrayEntityVariant.HideableCrownType.SODALITE_CROWN,
			AbysmParticleTypes.ROSEBLOOM_GLIMMER, AbysmParticleTypes.SODALITE_THORNS
		);

		registerBloomrayVariant(
			registerable,
			SUNNY_BLOOMRAY,
			BloomrayEntityVariant.HideableCrownType.ANYOLITE_CROWN,
			AbysmParticleTypes.SUNBLOOM_GLIMMER, AbysmParticleTypes.ANYOLITE_THORNS
		);

		registerBloomrayVariant(
			registerable,
			MAUVE_BLOOMRAY,
			BloomrayEntityVariant.HideableCrownType.MELILITE_CROWN,
			AbysmParticleTypes.MALLOWBLOOM_GLIMMER, AbysmParticleTypes.MELILITE_THORNS
		);
	}
	// endregion

	// region electric oogly booglies
	public static final ResourceKey<ElectricOoglyBooglyVariant> ELECTRIC_OOGLY_BOOGLY = ofOoglyBoogly("electric_oogly");
	public static final ResourceKey<ElectricOoglyBooglyVariant> ELLY_EEGY_BOOGLY = ofOoglyBoogly("elly_eegy");
	public static final ResourceKey<ElectricOoglyBooglyVariant> SMOLLY_MOLLY = ofOoglyBoogly("smolly_molly");
	public static final ResourceKey<ElectricOoglyBooglyVariant> ELECTRIC_ZOOGLY = ofOoglyBoogly("electric_zoogly");

	public static void ooglyBooglyBootstrap(BootstrapContext<ElectricOoglyBooglyVariant> registerable) {
		registerOoglyBoogly(registerable, ELECTRIC_OOGLY_BOOGLY, ARGB.colorFromFloat(1f, 0.75f, 1f, 0.75f), 0F);
		registerOoglyBoogly(registerable, ELLY_EEGY_BOOGLY, ARGB.colorFromFloat(1f, 0.75f, 0.9f, 1f), 0F);
		registerOoglyBoogly(registerable, SMOLLY_MOLLY, ARGB.colorFromFloat(1f, 1f, 0.9f, 1f), 0F);
		registerOoglyBoogly(registerable, ELECTRIC_ZOOGLY, ARGB.colorFromFloat(1f, 1f, 0.75f, 0.85f), 8F);
	}
	// endregion

	// region gup gup
	public static final ResourceKey<GupGupEntityVariant> GUP_GUP = ofGupGup("gup_gup");
	public static final ResourceKey<GupGupEntityVariant> SUP_SUP = ofGupGup("sup_sup");
	public static final ResourceKey<GupGupEntityVariant> HUP_HUP = ofGupGup("hup_hup");
	public static final ResourceKey<GupGupEntityVariant> PUP_PUP = ofGupGup("pup_pup");

	public static void gupGupBootstrap(BootstrapContext<GupGupEntityVariant> registerable) {
		registerGupGup(
			registerable,
			GUP_GUP
		);

		registerGupGup(
			registerable,
			SUP_SUP
		);

		registerGupGup(
			registerable,
			HUP_HUP
		);

		registerGupGup(
			registerable,
			PUP_PUP
		);
	}
	// endregion

	// region snapper
	public static final ResourceKey<SnapperEntityVariant> CHROMATIC = ofSnapper("chromatic");
	public static final ResourceKey<SnapperEntityVariant> DEPTH = ofSnapper("depth");
	public static void snapperBootstrap(BootstrapContext<SnapperEntityVariant> registerable) {
		var biomeLookup = registerable.lookup(Registries.BIOME);

		registerSnapper(
			registerable,
			CHROMATIC,
			SpawnPrioritySelectors.fallback(0)
		);

		registerSnapper(
			registerable,
			DEPTH,
			SpawnPrioritySelectors.single(
				new BiomeCheck(biomeLookup.getOrThrow(AbysmBiomeTags.SPAWNS_VARIANT_DEPTH_SNAPPER)),
				1
			)
		);
	}
	// endregion

	private static ResourceKey<BloomrayEntityVariant> ofBloomray(String path) {
		return of(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT, path);
	}

	private static ResourceKey<ElectricOoglyBooglyVariant> ofOoglyBoogly(String path) {
		return of(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT, path);
	}

	private static ResourceKey<GupGupEntityVariant> ofGupGup(String path) {
		return of(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT, path);
	}

	private static ResourceKey<SnapperEntityVariant> ofSnapper(String path) {
		return of(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT, path);
	}


	private static <T extends AbstractEntityVariant> ResourceKey<T> of(ResourceKey<Registry<T>> key, String path) {
		return ResourceKey.create(key, Abysm.id(path));
	}

	private static void registerBloomrayVariant(BootstrapContext<BloomrayEntityVariant> registry, ResourceKey<BloomrayEntityVariant> key, BloomrayEntityVariant.HideableCrownType crownType, SimpleParticleType glimmer, SimpleParticleType thorns) {
		ResourceLocation variantPath = Abysm.id("textures/entity/" + key.location().getPath() + "_bloomray.png");
		Component name = Component.translatable("abysm.entity.bloomray." + key.location().getPath());
		BloomrayEntityVariant variant = new BloomrayEntityVariant(name, variantPath, SpawnPrioritySelectors.fallback(0), crownType, glimmer, thorns);
		register(registry, key, variant);
	}

	private static void registerOoglyBoogly(BootstrapContext<ElectricOoglyBooglyVariant> registry, ResourceKey<ElectricOoglyBooglyVariant> key, int electricityColor, float explosionPower) {
		ResourceLocation variantPath = Abysm.id("textures/entity/" + key.location().getPath() + "_boogly.png");
		Component name = Component.translatable("abysm.entity.electric_oogly_boogly." + key.location().getPath());
		ElectricOoglyBooglyVariant variant = new ElectricOoglyBooglyVariant(name, variantPath, SpawnPrioritySelectors.fallback(0), new OoglyBooglyFumesParticleEffect(electricityColor, explosionPower > 0F), explosionPower);
		register(registry, key, variant);
	}

	private static void registerGupGup(BootstrapContext<GupGupEntityVariant> registry, ResourceKey<GupGupEntityVariant> key) {
		ResourceLocation variantPath = Abysm.id("textures/entity/" + key.location().getPath() + ".png");
		Component name = Component.translatable("abysm.entity.gup_gup." + key.location().getPath());
		GupGupEntityVariant variant = new GupGupEntityVariant(name, variantPath, SpawnPrioritySelectors.fallback(0));
		register(registry, key, variant);
	}

	private static void registerSnapper(BootstrapContext<SnapperEntityVariant> registry, ResourceKey<SnapperEntityVariant> key, SpawnPrioritySelectors spawnConditions) {
		ResourceLocation variantPath = Abysm.id("textures/entity/" + key.location().getPath() + "_snapper.png");
		Component name = Component.translatable("abysm.entity.snapper." + key.location().getPath());
		SnapperEntityVariant variant = new SnapperEntityVariant(name, variantPath, spawnConditions);
		register(registry, key, variant);
	}

	private static <T extends AbstractEntityVariant> void register(BootstrapContext<T> registerable, ResourceKey<T> key, T variant) {
		registerable.register(key, variant);
	}

}
