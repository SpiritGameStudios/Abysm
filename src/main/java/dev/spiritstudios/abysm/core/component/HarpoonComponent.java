package dev.spiritstudios.abysm.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import java.awt.*;
import java.util.function.Consumer;

public record HarpoonComponent(ItemStack stack, boolean loaded, int ticksSinceShot) implements TooltipProvider {

	public static final HarpoonComponent EMPTY = new HarpoonComponent.Builder().build();

	private static final int LIGHT = ARGB.opaque(new Color(52, 189, 235).getRGB());
	private static final int DARK = ARGB.opaque(new Color(48, 115, 171).getRGB());

	public static final int SEVEN_HUNDRED = 700;
	public static final float RECIPROCAL_OF_SEVEN_HUNDRED = 1f / SEVEN_HUNDRED;

	public static final Codec<HarpoonComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				ItemStack.OPTIONAL_CODEC.fieldOf("heart").forGetter(component -> component.stack),
				Codec.BOOL.optionalFieldOf("loaded", true).forGetter(component -> component.loaded),
				Codec.INT.optionalFieldOf("ticksSinceShot", 0).forGetter(component -> component.ticksSinceShot)
			)
			.apply(instance, HarpoonComponent::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, HarpoonComponent> PACKET_CODEC = StreamCodec.composite(
		ItemStack.OPTIONAL_STREAM_CODEC, component -> component.stack,
		ByteBufCodecs.BOOL, component -> component.loaded,
		ByteBufCodecs.VAR_INT, component -> component.ticksSinceShot,
		HarpoonComponent::new
	);

	public boolean isBlessed() {
		return !this.stack.isEmpty();
	}

	@Override
	public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
		if (this.loaded()) {
			textConsumer.accept(Component.translatable("item.abysm.harpoon.loaded"));
		}

		if (isBlessed()) {
			textConsumer.accept(scrollingGradient(Component.translatable("item.abysm.harpoon.blessed"), SEVEN_HUNDRED, RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK, false));
		}
	}

	public static MutableComponent scrollingGradient(Component original, int wrap, float reciprocalWrap, int startColor, int endColor, boolean forward) {
		String blessed = original.getString();
		int length = blessed.length();
		long time = Util.getMillis();
		MutableComponent text = Component.literal(blessed.substring(0, 1)).withColor(ARGB.srgbLerp(((time) % wrap) * reciprocalWrap, startColor, endColor));
		float incr = (float) wrap / length;
		for (int i = 1; i < length; i++) {
			float deltaHalfCalculated;
			if (forward) {
				deltaHalfCalculated = time - (int) (incr * i);
			} else {
				deltaHalfCalculated = time + (int) (incr * i);
			}
			text.append(Component.literal(blessed.substring(i, i + 1)).withColor(ARGB.srgbLerp((deltaHalfCalculated % wrap) * reciprocalWrap, startColor, endColor)));
		}
		return text;
	}


	public Builder builder() {
		return new Builder().stack(this.stack()).loaded(this.loaded()).ticksSinceShot(this.ticksSinceShot());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof HarpoonComponent(ItemStack stack1, boolean loaded1, int sinceShot))) return false;

		return ItemStack.isSameItemSameComponents(this.stack(), stack1) && this.loaded() == loaded1 && this.ticksSinceShot() == sinceShot;
	}

	public static class Builder {
		ItemStack heart = ItemStack.EMPTY;
		boolean loaded = true;
		int ticksSinceShot = 0;

		public Builder stack(ItemStack stack) {
			this.heart = stack;
			return this;
		}

		public Builder loaded(boolean loaded) {
			this.loaded = loaded;
			return this;
		}

		public Builder ticksSinceShot(int ticksSinceShot) {
			this.ticksSinceShot = ticksSinceShot;
			return this;
		}

		public HarpoonComponent build() {
			return new HarpoonComponent(this.heart, this.loaded, this.ticksSinceShot);
		}
	}
}
