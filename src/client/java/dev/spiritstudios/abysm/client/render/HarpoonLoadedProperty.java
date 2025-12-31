package dev.spiritstudios.abysm.client.render;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.core.component.HarpoonComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import dev.spiritstudios.abysm.core.component.AbysmDataComponents;
import org.jetbrains.annotations.Nullable;

public record HarpoonLoadedProperty() implements ConditionalItemModelProperty {
	public static final MapCodec<HarpoonLoadedProperty> CODEC = MapCodec.unit(new HarpoonLoadedProperty());

	@Override
	public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext context) {
		HarpoonComponent component = stack.get(AbysmDataComponents.HARPOON);
		return component != null && component.loaded();
	}

	@Override
	public MapCodec<HarpoonLoadedProperty> type() {
		return CODEC;
	}
}
