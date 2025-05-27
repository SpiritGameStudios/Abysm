package dev.spiritstudios.abysm.entity.pattern;

import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.util.DyeColor;
import net.minecraft.world.ServerWorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface Patternable {

	List<Integer> DEFAULT_DYE_ENTITY_COLORS = Arrays.stream(DyeColor.values()).map(DyeColor::getEntityColor).toList();

	EntityPattern getEntityPattern();

	void setEntityPattern(EntityPattern pattern);

	default EntityPattern getPatternForInitialize(ServerWorldAccess world, Entity self, @Nullable EntityData entityData) {
		if(entityData instanceof EntityPatternEntityData patternData) {
			return patternData.pattern;
		} else {
			return getRandomPattern(world, self);
		}
	}

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

	default List<Integer> getBaseColors() {
		return DEFAULT_DYE_ENTITY_COLORS;
	}

	default List<Integer> getPatternColors() {
		return DEFAULT_DYE_ENTITY_COLORS;
	}

}
