package dev.spiritstudios.abysm.client.render;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.component.HarpoonComponent;
import dev.spiritstudios.abysm.component.AbysmDataComponentTypes;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record HarpoonLoadedProperty() implements BooleanProperty {
	public static final MapCodec<HarpoonLoadedProperty> CODEC = MapCodec.unit(new HarpoonLoadedProperty());

	@Override
	public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
		HarpoonComponent component = stack.get(AbysmDataComponentTypes.HARPOON);
		return component != null && component.loaded();
	}

	@Override
	public MapCodec<HarpoonLoadedProperty> getCodec() {
		return CODEC;
	}
}

