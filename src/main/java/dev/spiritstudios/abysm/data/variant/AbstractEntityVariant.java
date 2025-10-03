package dev.spiritstudios.abysm.data.variant;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import net.minecraft.entity.VariantSelectorProvider;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.spawn.SpawnCondition;
import net.minecraft.entity.spawn.SpawnConditionSelectors;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * Abstract class containing basic necessary fields for entity texture variants, along with helper methods. (Not to be confused with Entity Pattern Variants!)<br><br>
 * To add a new Entity (Texture) Variant:
 * <ul type="1">
 *     <li>Create a new class extending this class. You'll need a codec, and likely want a default value & your own static methods for {@link AbstractEntityVariant#fromIntId(RegistryKey, AbstractEntityVariant, DynamicRegistryManager, int)} & {@link AbstractEntityVariant#toIntId(RegistryKey, DynamicRegistryManager, AbstractEntityVariant)}.</li>
 *     <li>Create a RegistryKey in {@link dev.spiritstudios.abysm.registry.AbysmRegistries} & add it to the dynamic registries.</li>
 *     <li>Create your own bootstrap method with the built-in variant in {@link AbysmEntityVariants} for your key, and add that bootstrap method to AbysmDatagen#buildRegistry.</li>
 *     <li>In your Entity class:
 *     <ul type="1">
 *         <li>Implement {@link dev.spiritstudios.abysm.entity.variant.Variantable} and fill in the methods.</li>
 *         <li>
 *         Create a (public) static final TrackedData key with an Integer, and add that to the {@link net.minecraft.entity.Entity#initDataTracker(DataTracker.Builder)}. This will sync your EntityVariant's registry int id.<br><br>
 *         Alternatively, a custom TrackedDataHandler can be created with Fabric API, and that can be used instead. However, using the Registry integer ids is typically more performant.
 *         </li>
 *         <li>Use the variant as wanted! For rendering, you can access the Entity's EntityVariant (via GeckoLib DataTickets or a custom RenderState), and set the texture as wanted.</li>
 *     </ul>
 *     </li>
 * </ul>
 *
 * @see dev.spiritstudios.abysm.registry.AbysmRegistries
 * @see AbysmEntityVariants
 * @see BloomrayEntityVariant
 * @see BloomrayEntity
 */
public abstract class AbstractEntityVariant implements VariantSelectorProvider<SpawnContext, SpawnCondition> {
	// TODO - Easier default id getting
	// TODO - Easier random entry getting
	public final Text name;
	public final Identifier texture;
	public final SpawnConditionSelectors spawnConditions;

	public static <T extends AbstractEntityVariant> Products.P3<RecordCodecBuilder.Mu<T>, Text, Identifier, SpawnConditionSelectors> fillFields(RecordCodecBuilder.Instance<T> instance) {
		return instance.group(
			TextCodecs.CODEC.fieldOf("name").forGetter(variant -> variant.name),
			Identifier.CODEC.fieldOf("texture").forGetter(variant -> variant.texture),
			SpawnConditionSelectors.CODEC.fieldOf("spawn_conditions").forGetter(variant -> variant.spawnConditions)
		);
	}

	public AbstractEntityVariant(Text name, Identifier texture, SpawnConditionSelectors spawnConditions) {
		this.name = name;
		this.texture = texture;
		this.spawnConditions = spawnConditions;
	}

	// If there's a better/cleaner way of doing this child-class-fills-in-static-params-with-its-own-static-method
	// for the int id conversions while remaining fully static that I(Kat) didn't know about, please change it!

	// Static helper methods to convert variants from/to int ids for packets.
	// Doing it this way helps with performance, which is a concern of mine if we're going to data-drive multiple entities.
	// Child classes of AbstractEntityVariant can add their own static method filling in the key and fallback params.
	public static Identifier buildEntityTexturePath(String path) {
		return Abysm.id("textures/entity/" + path + ".png");
	}

	// Generic getters
	public Text getName() {
		return name;
	}

	public Identifier getTexture() {
		return texture;
	}

	@Override
	public List<Selector<SpawnContext, SpawnCondition>> getSelectors() {
		return this.spawnConditions.selectors();
	}
}
