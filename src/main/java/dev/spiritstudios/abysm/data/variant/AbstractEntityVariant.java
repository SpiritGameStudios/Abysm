package dev.spiritstudios.abysm.data.variant;

import com.mojang.datafixers.Products;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.core.registries.AbysmRegistries;
import dev.spiritstudios.abysm.world.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.abysm.world.entity.variant.AbysmEntityVariants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;

import java.util.List;

/**
 * Abstract class containing basic necessary fields for entity texture variants, along with helper methods. (Not to be confused with Entity Pattern Variants!)<br><br>
 * To add a new Entity (Texture) Variant:
 * <ul type="1">
 *     <li>Create a new class extending this class. You'll need a codec, and likely want a default value & your own static methods for {@link AbstractEntityVariant#fromIntId(RegistryKey, AbstractEntityVariant, DynamicRegistryManager, int)} & {@link AbstractEntityVariant#toIntId(RegistryKey, DynamicRegistryManager, AbstractEntityVariant)}.</li>
 *     <li>Create a RegistryKey in {@link AbysmRegistries} & add it to the dynamic registries.</li>
 *     <li>Create your own bootstrap method with the built-in variant in {@link AbysmEntityVariants} for your key, and add that bootstrap method to AbysmDatagen#buildRegistry.</li>
 *     <li>In your Entity class:
 *     <ul type="1">
 *         <li>Implement {@link dev.spiritstudios.abysm.world.entity.variant.Variantable} and fill in the methods.</li>
 *         <li>
 *         Create a (public) static final TrackedData key with an Integer, and add that to the {@link net.minecraft.world.entity.Entity#defineSynchedData(SynchedEntityData.Builder)}. This will sync your EntityVariant's registry int id.<br><br>
 *         Alternatively, a custom TrackedDataHandler can be created with Fabric API, and that can be used instead. However, using the Registry integer ids is typically more performant.
 *         </li>
 *         <li>Use the variant as wanted! For rendering, you can access the Entity's EntityVariant (via GeckoLib DataTickets or a custom RenderState), and set the texture as wanted.</li>
 *     </ul>
 *     </li>
 * </ul>
 *
 * @see AbysmRegistries
 * @see AbysmEntityVariants
 * @see BloomrayEntityVariant
 * @see BloomrayEntity
 */
public abstract class AbstractEntityVariant implements PriorityProvider<SpawnContext, SpawnCondition> {
	// TODO - Easier default id getting
	// TODO - Easier random entry getting
	public final Component name;
	public final Identifier texture;
	public final SpawnPrioritySelectors spawnConditions;

	public static <T extends AbstractEntityVariant> Products.P3<RecordCodecBuilder.Mu<T>, Component, Identifier, SpawnPrioritySelectors> fillFields(RecordCodecBuilder.Instance<T> instance) {
		return instance.group(
			ComponentSerialization.CODEC.fieldOf("name").forGetter(variant -> variant.name),
			Identifier.CODEC.fieldOf("texture").forGetter(variant -> variant.texture),
			SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(variant -> variant.spawnConditions)
		);
	}

	public AbstractEntityVariant(Component name, Identifier texture, SpawnPrioritySelectors spawnConditions) {
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
	public Component getName() {
		return name;
	}


	@Override
	public List<Selector<SpawnContext, SpawnCondition>> selectors() {
		return this.spawnConditions.selectors();
	}
}
