package dev.spiritstudios.abysm.entity.pattern;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * To create an entity with overlayable patterns:
 * <ul type="1">
 *     <li>For the Entity class(Preferably a GeckoLib entity, as pattern rendering stuff has already been created):</li>
 *     <ul>
 *         <li>Implement this interface and fill in the required methods.</li>
 *         <li>Override the Entity#initDataTracker method to include your DataTracked EntityVariant. This syncs your variant.</li>
 *         <li>Override the Entity#writeCustomDataToNbt and Entity#readCustomDataFromNbt to call {@link Patternable#writeEntityPatternNbt(NbtCompound)} and {@link Patternable#readEntityPatternNbt(Entity, NbtCompound)} respectively. These keep your variants persistent on world save.</li>
 *         <li>Override the MobEntity#initialize method to call {@link Patternable#getPatternForInitialize(ServerWorldAccess, Entity, EntityData)}, which sets your entity's variant upon spawning.</li>
 *     </ul>
 *     <li>For the entity renderer:</li>
 *     <ul>
 *         <li>Add the EntityPatternFeatureRenderer render layer in the constructor.</li>
 *         <li>Override the addAdditionalStateData method to include the custom DataTicket of the entity's EntityPattern. There's a pre-made one in EntityPatternFeatureRenderer#ENTITY_PATTERN_DATA_TICKET.</li>
 *         <li>Override the getTextureLocation method to allow for the EntityPattern's base texture.</li>
 *         <li>Override the getRenderColor method to allow for the EntityPattern's base color.</li>
 *     </ul>
 *     <li>Create all the built-in variant keys in {@link dev.spiritstudios.abysm.registry.AbysmEntityPatternVariants}, and all them in the bootstrap method. Don't forget to run datagen!</li>
 *     <li>Launch the game and test. I believe that's all that needs to be done.</li>
 * </ul>
 *
 * See the Small Floral Fish Entity (and its renderer) for examples!
 *
 * @see dev.spiritstudios.abysm.entity.floral_reef.AbstractFloralFishEntity
 * @see dev.spiritstudios.abysm.entity.floral_reef.SmallFloralFishEntity
 * @see dev.spiritstudios.abysm.registry.AbysmEntityPatternVariants
 */
public interface Patternable {

	List<Integer> DEFAULT_DYE_ENTITY_COLORS = Arrays.stream(DyeColor.values()).map(DyeColor::getEntityColor).toList();

	/**
	 * @return This Entity's EntityPattern. Use the {@link net.minecraft.entity.data.DataTracker#get(TrackedData)} method with your EntityPattern Tracked Data key.
	 * @see dev.spiritstudios.abysm.entity.floral_reef.AbstractFloralFishEntity#ENTITY_PATTERN
	 * @see dev.spiritstudios.abysm.entity.floral_reef.AbstractFloralFishEntity#getEntityPattern()
	 */
	EntityPattern getEntityPattern();

	/**
	 * @param pattern Set this Entity's EntityPattern. Use {@link net.minecraft.entity.data.DataTracker#set(TrackedData, Object)} with your EntityPattern Tracked Data key.
	 * @see dev.spiritstudios.abysm.entity.floral_reef.AbstractFloralFishEntity#ENTITY_PATTERN
	 * @see dev.spiritstudios.abysm.entity.floral_reef.AbstractFloralFishEntity#setEntityPattern(EntityPattern)
	 */
	void setEntityPattern(EntityPattern pattern);

	/**
	 * The default pattern, most likely used in case of an error. Under most circumstances, this should never get used.
	 *
	 * @return This entity's default pattern.
	 * @see	Patternable#getFallbackPattern(Entity)
	 * @see Patternable#getCommonPatterns()
	 */
	EntityPattern getDefaultPattern();

	/**
	 * @return A list of common patterns that a majority of entities will use. Leave empty for no common patterns. {@link Patternable#commonPatternChance()} determines the chance of this list being used.
	 * @see Patternable#commonPatternChance()
	 */
	default List<EntityPattern> getCommonPatterns() {
		return List.of();
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
	 * If a list of common patterns is provided, a random one is chosen from there. Otherwise the {@link Patternable#getDefaultPattern()} pattern is used.
	 * @return A fallback pattern.
	 */
	default EntityPattern getFallbackPattern(Entity self) {
		if(this.getCommonPatterns().isEmpty()) return this.getDefaultPattern();
		int index = self.getRandom().nextInt(this.getCommonPatterns().size());
		return this.getCommonPatterns().get(index);
	}

	/**
	 * @return An EntityPattern for this entity upon initialization.
	 */
	default EntityPattern getPatternForInitialize(ServerWorldAccess world, Entity self, @Nullable EntityData entityData) {
		if(entityData instanceof PatternedEntityData patternData) {
			return patternData.getPattern();
		}

		if(!this.getCommonPatterns().isEmpty() && world.getRandom().nextFloat() < this.commonPatternChance()) {
			List<EntityPattern> commonPatterns = this.getCommonPatterns();
			int patternIndex = world.getRandom().nextInt(commonPatterns.size());
			EntityPattern pattern = commonPatterns.get(patternIndex);
			if(pattern != null) return pattern;
		}

		return getRandomPattern(world, self);
	}

	/**
	 * @return A random EntityPattern. Note: this does <b>NOT</b> use the {@link Patternable#getCommonPatterns()} list!<br>
	 * See {@link Patternable#getPatternForInitialize(ServerWorldAccess, Entity, EntityData)} instead for that!
	 * @see Patternable#getPatternForInitialize(ServerWorldAccess, Entity, EntityData)
	 */
	default EntityPattern getRandomPattern(ServerWorldAccess world, Entity self) {
		List<EntityPatternVariant> variants = EntityPatternVariant.getVariantsForEntityType(world, self.getType()).stream().toList();
		int total = variants.size();
		int randomIndex = world.getRandom().nextInt(total);
		EntityPatternVariant variant = variants.get(randomIndex);

		List<Integer> baseColors = this.getBaseColors();
		int baseColor = baseColors.get(world.getRandom().nextInt(baseColors.size()));

		List<Integer> patternColors = this.getPatternColors();
		int patternColor = patternColors.get(world.getRandom().nextInt(patternColors.size()));

		return new EntityPattern(variant, baseColor, patternColor);
	}

	/**
	 * Write this entity's pattern to the world's nbt.
	 */
	default void writeEntityPatternNbt(NbtCompound nbt) {
		this.getEntityPattern().writeNbt(nbt);
	}

	/**
	 * Read an entity pattern from the world's nbt.
	 */
	default void readEntityPatternNbt(Entity self, NbtCompound nbt) {
		Optional<EntityPattern> pattern = EntityPattern.fromNbt(nbt);
		if(pattern.isPresent()) {
			this.setEntityPattern(pattern.get());
		} else {
			Abysm.LOGGER.warn("Could not read EntityPattern for {}! Using fallback pattern instead.", self.getType());
			this.setEntityPattern(this.getFallbackPattern(self));
		}
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
