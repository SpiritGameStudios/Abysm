package dev.spiritstudios.abysm.mixin.accessors;

import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/**
 * @author Ampflower
 **/
@Mixin(ArmorMaterials.class)
public interface ArmorMaterialsAccessor {
	@Invoker
	static Map<EquipmentType, Integer> invokeCreateDefenseMap(
			final int boots,
			final int leggings,
			final int chestplate,
			final int helmet,
			final int body
	) {
		throw new AssertionError();
	}
}
