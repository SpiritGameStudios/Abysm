package dev.spiritstudios.abysm.data.variant;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.floral_reef.BloomrayEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Abstract class containing basic necessary fields for entity texture variants, along with helper methods. (Not to be confused with Entity Pattern Variants!)<br><br>
 * To add a new Entity (Texture) Variant:
 * <ul type="1">
 *     <li>Create a new class extending this class. You'll need a codec, and likely want a default value & your own static methods for {@link AbstractEntityVariant#fromIntId(RegistryKey, AbstractEntityVariant, DynamicRegistryManager, int)} & {@link AbstractEntityVariant#toIntId(RegistryKey, DynamicRegistryManager, AbstractEntityVariant)}.</li>
 *     <li>Create a RegistryKey in {@link dev.spiritstudios.abysm.registry.AbysmRegistries} & add it to the dynamic registries.</li>
 *     <li>Create your own bootstrap method with the built-in variant in {@link dev.spiritstudios.abysm.registry.AbysmEntityVariants} for your key, and add that bootstrap method to AbysmDatagen#buildRegistry.</li>
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
 * @see dev.spiritstudios.abysm.registry.AbysmRegistries
 * @see dev.spiritstudios.abysm.registry.AbysmEntityVariants
 * @see BloomrayEntityVariant
 * @see BloomrayEntity
 */
public abstract class AbstractEntityVariant {
	// TODO - Easier default id getting
	// TODO - Easier random entry getting
	public final Text name;
	public final Identifier texture;

	public AbstractEntityVariant(Text name, Identifier texture) {
		this.name = name;
		this.texture = texture;
	}

	// If there's a better/cleaner way of doing this child-class-fills-in-static-params-with-its-own-static-method
	// for the int id conversions while remaining fully static that I(Kat) didn't know about, please change it!

	// Static helper methods to convert variants from/to int ids for packets.
	// Doing it this way helps with performance, which is a concern of mine if we're going to data-drive multiple entities.
	// Child classes of AbstractEntityVariant can add their own static method filling in the key and fallback params.
	public static <T extends AbstractEntityVariant> T fromIntId(RegistryKey<Registry<T>> key, T fallback, DynamicRegistryManager registryManager, int id) {
		Optional<RegistryEntry.Reference<T>> reference = registryManager.getOrThrow(key).getEntry(id);
		return reference.map(RegistryEntry.Reference::value).orElse(fallback);
	}

	// Child classes of AbstractEntityVariant can add their own static method filling in the key.
	public static <T extends AbstractEntityVariant> int toIntId(RegistryKey<Registry<T>> key, DynamicRegistryManager registryManager, T variant) {
		return registryManager.getOrThrow(key).getRawId(variant);
	}

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

	// Helper methods for creating variant codecs
	protected static @NotNull <T extends AbstractEntityVariant> RecordCodecBuilder<T, Text> getNameCodec() {
		return TextCodecs.CODEC.fieldOf("name").forGetter(variant -> variant.name);
	}

	protected static @NotNull <T extends AbstractEntityVariant> RecordCodecBuilder<T, Identifier> getTextureCodec() {
		return Identifier.CODEC.fieldOf("texture").forGetter(variant -> variant.texture);
	}
}
