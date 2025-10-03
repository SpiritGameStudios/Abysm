package dev.spiritstudios.abysm.entity.variant;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.variant.AbstractEntityVariant;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.data.variant.GupGupEntityVariant;
import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import dev.spiritstudios.abysm.registry.tags.AbysmBiomeTags;
import net.minecraft.entity.spawn.BiomeSpawnCondition;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class AbysmEntityVariants {
	// region bloomrays
	public static final RegistryKey<BloomrayEntityVariant> ROSY_BLOOMRAY = ofBloomray("rosy");
	public static final RegistryKey<BloomrayEntityVariant> SUNNY_BLOOMRAY = ofBloomray("sunny");
	public static final RegistryKey<BloomrayEntityVariant> MAUVE_BLOOMRAY = ofBloomray("mauve");

	public static void bloomrayBootstrap(Registerable<BloomrayEntityVariant> registerable) {
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
	public static final RegistryKey<ElectricOoglyBooglyVariant> ELECTRIC_OOGLY_BOOGLY = ofOoglyBoogly("electric_oogly");
	public static final RegistryKey<ElectricOoglyBooglyVariant> ELLY_EEGY_BOOGLY = ofOoglyBoogly("elly_eegy");
	public static final RegistryKey<ElectricOoglyBooglyVariant> SMOLLY_MOLLY = ofOoglyBoogly("smolly_molly");
	public static final RegistryKey<ElectricOoglyBooglyVariant> ELECTRIC_ZOOGLY = ofOoglyBoogly("electric_zoogly");

	public static void ooglyBooglyBootstrap(Registerable<ElectricOoglyBooglyVariant> registerable) {
		registerOoglyBoogly(registerable, ELECTRIC_OOGLY_BOOGLY, ColorHelper.fromFloats(1f, 0.75f, 1f, 0.75f), false);
		registerOoglyBoogly(registerable, ELLY_EEGY_BOOGLY, ColorHelper.fromFloats(1f, 0.75f, 0.9f, 1f), false);
		registerOoglyBoogly(registerable, SMOLLY_MOLLY, ColorHelper.fromFloats(1f, 1f, 0.9f, 1f), false);
		registerOoglyBoogly(registerable, ELECTRIC_ZOOGLY, ColorHelper.fromFloats(1f, 1f, 0.75f, 0.85f), true);
	}
	// endregion

	// region gup gup
	public static final RegistryKey<GupGupEntityVariant> GUP_GUP = ofGupGup("gup_gup");
	public static final RegistryKey<GupGupEntityVariant> SUP_SUP = ofGupGup("sup_sup");
	public static final RegistryKey<GupGupEntityVariant> HUP_HUP = ofGupGup("hup_hup");
	public static final RegistryKey<GupGupEntityVariant> PUP_PUP = ofGupGup("pup_pup");

	public static void gupGupBootstrap(Registerable<GupGupEntityVariant> registerable) {
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
	public static final RegistryKey<SnapperEntityVariant> CHROMATIC = ofSnapper("chromatic");
	public static final RegistryKey<SnapperEntityVariant> DEPTH = ofSnapper("depth");
	public static void snapperBootstrap(Registerable<SnapperEntityVariant> registerable) {
		var biomeLookup = registerable.getRegistryLookup(RegistryKeys.BIOME);

		registerSnapper(
			registerable,
			CHROMATIC,
			SpawnConditionSelectors.EMPTY
		);

		registerSnapper(
			registerable,
			DEPTH,
			SpawnConditionSelectors.createSingle(
				new BiomeSpawnCondition(biomeLookup.getOrThrow(AbysmBiomeTags.SPAWNS_VARIANT_DEPTH_SNAPPER)),
				1
			)
		);
	}
	// endregion

	private static RegistryKey<BloomrayEntityVariant> ofBloomray(String path) {
		return of(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT, path);
	}

	private static RegistryKey<ElectricOoglyBooglyVariant> ofOoglyBoogly(String path) {
		return of(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT, path);
	}

	private static RegistryKey<GupGupEntityVariant> ofGupGup(String path) {
		return of(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT, path);
	}

	private static RegistryKey<SnapperEntityVariant> ofSnapper(String path) {
		return of(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT, path);
	}


	private static <T extends AbstractEntityVariant> RegistryKey<T> of(RegistryKey<Registry<T>> key, String path) {
		return RegistryKey.of(key, Abysm.id(path));
	}

	private static void registerBloomrayVariant(Registerable<BloomrayEntityVariant> registry, RegistryKey<BloomrayEntityVariant> key, BloomrayEntityVariant.HideableCrownType crownType, SimpleParticleType glimmer, SimpleParticleType thorns) {
		Identifier variantPath = Abysm.id("textures/entity/" + key.getValue().getPath() + "_bloomray.png");
		Text name = Text.translatable("abysm.entity.bloomray." + key.getValue().getPath());
		BloomrayEntityVariant variant = new BloomrayEntityVariant(name, variantPath, SpawnConditionSelectors.EMPTY, crownType, glimmer, thorns);
		register(registry, key, variant);
	}

	private static void registerOoglyBoogly(Registerable<ElectricOoglyBooglyVariant> registry, RegistryKey<ElectricOoglyBooglyVariant> key, int electricityColor, boolean deadly) {
		Identifier variantPath = Abysm.id("textures/entity/" + key.getValue().getPath() + "_boogly.png");
		Text name = Text.translatable("abysm.entity.electric_oogly_boogly." + key.getValue().getPath());
		ElectricOoglyBooglyVariant variant = new ElectricOoglyBooglyVariant(name, variantPath, SpawnConditionSelectors.EMPTY, electricityColor, deadly);
		register(registry, key, variant);
	}

	private static void registerGupGup(Registerable<GupGupEntityVariant> registry, RegistryKey<GupGupEntityVariant> key) {
		Identifier variantPath = Abysm.id("textures/entity/" + key.getValue().getPath() + ".png");
		Text name = Text.translatable("abysm.entity.gup_gup." + key.getValue().getPath());
		GupGupEntityVariant variant = new GupGupEntityVariant(name, variantPath, SpawnConditionSelectors.EMPTY);
		register(registry, key, variant);
	}

	private static void registerSnapper(Registerable<SnapperEntityVariant> registry, RegistryKey<SnapperEntityVariant> key, SpawnConditionSelectors spawnConditions) {
		Identifier variantPath = Abysm.id("textures/entity/" + key.getValue().getPath() + "_snapper.png");
		Text name = Text.translatable("abysm.entity.snapper." + key.getValue().getPath());
		SnapperEntityVariant variant = new SnapperEntityVariant(name, variantPath, spawnConditions);
		register(registry, key, variant);
	}

	private static <T extends AbstractEntityVariant> void register(Registerable<T> registerable, RegistryKey<T> key, T variant) {
		registerable.register(key, variant);
	}

}
