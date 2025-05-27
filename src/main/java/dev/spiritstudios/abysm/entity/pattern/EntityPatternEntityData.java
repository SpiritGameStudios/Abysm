package dev.spiritstudios.abysm.entity.pattern;

import net.minecraft.entity.EntityData;

public class EntityPatternEntityData implements EntityData {
	public final EntityPattern pattern;

	public EntityPatternEntityData(EntityPattern pattern) {
		this.pattern = pattern;
	}
}
