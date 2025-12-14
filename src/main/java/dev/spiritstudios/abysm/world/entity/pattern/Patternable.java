package dev.spiritstudios.abysm.world.entity.pattern;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.world.entity.floralreef.AbstractFloralFish;
import dev.spiritstudios.abysm.world.entity.floralreef.SmallFloralFish;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
 *         <li>Override the Entity#writeCustomDataToNbt and Entity#readCustomDataFromNbt to call {@link Patternable#writeEntityPattern(RegistryOps, CompoundTag)} and {@link Patternable#readEntityPattern(Entity, RegistryOps, CompoundTag)} respectively. These keep your variants persistent on world save.</li>
 *         <li>Override the MobEntity#initialize method to call {@link Patternable#getPatternForInitialize(ServerLevelAccessor, Entity, SpawnGroupData)}, which sets your entity's variant upon spawning.</li>
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
 * @see AbstractFloralFish
 * @see SmallFloralFish
 * @see AbysmEntityPatternVariants
 */
public interface Patternable {
	List<Integer> DEFAULT_DYE_ENTITY_COLORS = Arrays.stream(DyeColor.values()).map(DyeColor::getTextureDiffuseColor).toList();

	/**
	 * @return This Entity's EntityPattern. Use the {@link net.minecraft.network.syncher.SynchedEntityData#get(EntityDataAccessor)} method with your EntityPattern Tracked Data key.
	 * @see AbstractFloralFish#ENTITY_PATTERN
	 * @see AbstractFloralFish#getEntityPattern()
	 */
	EntityPattern getEntityPattern();

	/**
	 * @param pattern Set this Entity's EntityPattern. Use {@link net.minecraft.network.syncher.SynchedEntityData#set(EntityDataAccessor, Object)} with your EntityPattern Tracked Data key.
	 * @see AbstractFloralFish#ENTITY_PATTERN
	 * @see AbstractFloralFish#setEntityPattern(EntityPattern)
	 */
	void setEntityPattern(EntityPattern pattern);

	/**
	 * The default pattern, most likely used in case of an error. Under most circumstances, this should never get used.
	 *
	 * @return This entity's default pattern.
	 * @see Patternable#getFallbackPattern(Entity)
	 * @see Patternable#getCommonPatterns()
	 */
	EntityPattern getDefaultPattern(HolderGetter<EntityPatternVariant> lookup);

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
	 * If a list of common patterns is provided, a random one is chosen from there. Otherwise, the {@link Patternable#getDefaultPattern(HolderGetter)} pattern is used.
	 *
	 * @return A fallback pattern.
	 */
	default EntityPattern getFallbackPattern(Entity self) {
		if (this.getCommonPatterns().isEmpty())
			return this.getDefaultPattern(self.registryAccess().lookupOrThrow(AbysmRegistryKeys.ENTITY_PATTERN));

		return Util.getRandom(this.getCommonPatterns(), self.getRandom());
	}

	/**
	 * @return An EntityPattern for this entity upon initialization.
	 */
	default EntityPattern getPatternForInitialize(ServerLevelAccessor world, Entity self, @Nullable SpawnGroupData entityData) {
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
	 * See {@link Patternable#getPatternForInitialize(ServerLevelAccessor, Entity, SpawnGroupData)} instead for that!
	 * @see Patternable#getPatternForInitialize(ServerLevelAccessor, Entity, SpawnGroupData)
	 */
	default EntityPattern getRandomPattern(ServerLevelAccessor world, Entity self) {
		List<? extends Holder<EntityPatternVariant>> variants = EntityPatternVariant.getVariantsForEntityType(world, self.getType()).toList();

		return new EntityPattern(
			Util.getRandom(variants, self.getRandom()),
			Util.getRandom(this.getBaseColors(), self.getRandom()),
			Util.getRandom(this.getPatternColors(), self.getRandom())
		);
	}

	/**
	 * Write this entity's pattern to the world's nbt.
	 */
	default void writeEntityPattern(ValueOutput view) {
		view.store("entity_pattern", EntityPattern.CODEC, getEntityPattern());
	}

	/**
	 * Read an entity pattern from the world's nbt.
	 */
	default void readEntityPattern(Entity self, ValueInput view) {
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
