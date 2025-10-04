package dev.spiritstudios.abysm.entity.pattern;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * To create an entity with overlayable patterns:
 * <ul type="1">
 *     <li>For the Entity class(Preferably a GeckoLib entity, as pattern rendering stuff has already been created):</li>
 *     <ul>
 *         <li>Implement this interface and fill in the required methods.</li>
 *         <li>Override the Entity#initDataTracker method to include your DataTracked EntityVariant. This syncs your variant.</li>
 *         <li>Override the Entity#writeCustomDataToNbt and Entity#readCustomDataFromNbt to call {@link Patternable#writeEntityPattern(RegistryOps, NbtCompound)} and {@link Patternable#readEntityPattern(Entity, RegistryOps, NbtCompound)} respectively. These keep your variants persistent on world save.</li>
 *         <li>Override the MobEntity#initialize method to call {@link Patternable#getPatternForInitialize(ServerWorldAccess, Entity, EntityData)}, which sets your entity's variant upon spawning.</li>
 *     </ul>
 *     <li>For the entity renderer:</li>
 *     <ul>
 *         <li>Add the EntityPatternFeatureRenderer render layer in the constructor.</li>
 *         <li>Override the addAdditionalStateData method to include the custom DataTicket of the entity's EntityPattern. There's a pre-made one in EntityPatternFeatureRenderer#ENTITY_PATTERN_DATA_TICKET.</li>
 *         <li>Override the getTextureLocation method to allow for the EntityPattern's base texture.</li>
 *         <li>Override the getRenderColor method to allow for the EntityPattern's base color.</li>
 *     </ul>
 *     <li>Create all the built-in variant keys in {@link AbysmEntityPatternVariants}, and all them in the bootstrap method. Don't forget to run datagen!</li>
 *     <li>Launch the game and test. I believe that's all that needs to be done.</li>
 * </ul>
 * <p>
 * See the Small Floral Fish Entity (and its renderer) for examples!
 *
 * @see dev.spiritstudios.abysm.entity.floralreef.AbstractFloralFishEntity
 * @see dev.spiritstudios.abysm.entity.floralreef.SmallFloralFishEntity
 * @see AbysmEntityPatternVariants
 */
public interface Patternable {
	List<Integer> DEFAULT_DYE_ENTITY_COLORS = Arrays.stream(DyeColor.values()).map(DyeColor::getEntityColor).toList();

	/**
	 * @return This Entity's EntityPattern. Use the {@link net.minecraft.entity.data.DataTracker#get(TrackedData)} method with your EntityPattern Tracked Data key.
	 * @see dev.spiritstudios.abysm.entity.floralreef.AbstractFloralFishEntity#ENTITY_PATTERN
	 * @see dev.spiritstudios.abysm.entity.floralreef.AbstractFloralFishEntity#getEntityPattern()
	 */
	EntityPattern getEntityPattern();

	/**
	 * @param pattern Set this Entity's EntityPattern. Use {@link net.minecraft.entity.data.DataTracker#set(TrackedData, Object)} with your EntityPattern Tracked Data key.
	 * @see dev.spiritstudios.abysm.entity.floralreef.AbstractFloralFishEntity#ENTITY_PATTERN
	 * @see dev.spiritstudios.abysm.entity.floralreef.AbstractFloralFishEntity#setEntityPattern(EntityPattern)
	 */
	void setEntityPattern(EntityPattern pattern);

	/**
	 * The default pattern, most likely used in case of an error. Under most circumstances, this should never get used.
	 *
	 * @return This entity's default pattern.
	 * @see Patternable#getFallbackPattern(Entity)
	 * @see Patternable#getCommonPatterns()
	 */
	EntityPattern getDefaultPattern(RegistryEntryLookup<EntityPatternVariant> lookup);

	/**
	 * @return A list of common patterns that a majority of entities will use. Leave empty for no common patterns. {@link Patternable#commonPatternChance()} determines the chance of this list being used.
	 * @see Patternable#commonPatternChance()
	 */
	default List<EntityPattern> getCommonPatterns() {
		return Collections.emptyList();
	}

	/**
	 * @return The chance of {@link Patternable#getCommonPatterns()} being used. Ranges from 0f to 1f!
	 */
	@Range(from = 0, to = 1)
	default float commonPatternChance() {
		return 0.75f;
	}

	/**
	 * A fallback pattern for this entity to use in case of an error. This is used if this entity is being reloaded, but its pattern can't be found.<br><br>
	 * If a list of common patterns is provided, a random one is chosen from there. Otherwise, the {@link Patternable#getDefaultPattern(RegistryEntryLookup)} pattern is used.
	 *
	 * @return A fallback pattern.
	 */
	default EntityPattern getFallbackPattern(Entity self) {
		if (this.getCommonPatterns().isEmpty())
			return this.getDefaultPattern(self.getRegistryManager().getOrThrow(AbysmRegistryKeys.ENTITY_PATTERN));

		return Util.getRandom(this.getCommonPatterns(), self.getRandom());
	}

	/**
	 * @return An EntityPattern for this entity upon initialization.
	 */
	default EntityPattern getPatternForInitialize(ServerWorldAccess world, Entity self, @Nullable EntityData entityData) {
		if (entityData instanceof PatternedEntityData patternData) {
			return patternData.getPattern();
		}

		if (!this.getCommonPatterns().isEmpty() && world.getRandom().nextFloat() < this.commonPatternChance()) {
			EntityPattern pattern = Util.getRandom(getCommonPatterns(), self.getRandom());
			if (pattern != null) return pattern;
		}

		return getRandomPattern(world, self);
	}

	/**
	 * @return A random EntityPattern. Note: this does <b>NOT</b> use the {@link Patternable#getCommonPatterns()} list!<br>
	 * See {@link Patternable#getPatternForInitialize(ServerWorldAccess, Entity, EntityData)} instead for that!
	 * @see Patternable#getPatternForInitialize(ServerWorldAccess, Entity, EntityData)
	 */
	default EntityPattern getRandomPattern(ServerWorldAccess world, Entity self) {
		List<? extends RegistryEntry<EntityPatternVariant>> variants = EntityPatternVariant.getVariantsForEntityType(world, self.getType()).toList();

		return new EntityPattern(
			Util.getRandom(variants, self.getRandom()),
			Util.getRandom(this.getBaseColors(), self.getRandom()),
			Util.getRandom(this.getPatternColors(), self.getRandom())
		);
	}

	/**
	 * Write this entity's pattern to the world's nbt.
	 */
	default void writeEntityPattern(WriteView view) {
		view.put("entity_pattern", EntityPattern.CODEC, getEntityPattern());
	}

	/**
	 * Read an entity pattern from the world's nbt.
	 */
	default void readEntityPattern(Entity self, ReadView view) {
		view.read("entity_pattern", EntityPattern.CODEC).ifPresentOrElse(
			this::setEntityPattern,
			() -> {
				Abysm.LOGGER.warn("Could not read EntityPattern for {}! Using fallback pattern instead.", self.getType());
				this.setEntityPattern(this.getFallbackPattern(self));
			}
		);
	}

	/**
	 * @return The default list of possible (int) colors to apply to the base texture.
	 */
	default List<Integer> getBaseColors() {
		return DEFAULT_DYE_ENTITY_COLORS;
	}

	/**
	 * @return The default list of possible (int) colors to apply to the pattern texture.
	 */
	default List<Integer> getPatternColors() {
		return DEFAULT_DYE_ENTITY_COLORS;
	}
}
