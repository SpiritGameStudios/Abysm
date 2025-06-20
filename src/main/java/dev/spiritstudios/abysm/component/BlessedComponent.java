package dev.spiritstudios.abysm.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;
import java.util.function.Consumer;

// remind me to implement TooltipAppender later
public record BlessedComponent(ItemStack stack, boolean loaded, int ticksSinceShot) implements TooltipAppender {

	public static final BlessedComponent EMPTY = new BlessedComponent.Builder().build();

	private static final int LIGHT = ColorHelper.fullAlpha(new Color(52, 189, 235).getRGB());
	private static final int DARK = ColorHelper.fullAlpha(new Color(48, 115, 171).getRGB());

	public static final int SEVEN_HUNDRED = 700;
	public static final float RECIPROCAL_OF_SEVEN_HUNDRED = 1f / SEVEN_HUNDRED;

	public static final Codec<BlessedComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				ItemStack.OPTIONAL_CODEC.optionalFieldOf("heart", ItemStack.EMPTY).forGetter(component -> component.stack),
				Codec.BOOL.optionalFieldOf("loaded", true).forGetter(component -> component.loaded),
				Codec.INT.optionalFieldOf("ticksSinceShot", 0).forGetter(component -> component.ticksSinceShot)
			)
			.apply(instance, BlessedComponent::new)
	);

	public static final PacketCodec<RegistryByteBuf, BlessedComponent> PACKET_CODEC = PacketCodec.tuple(
		ItemStack.PACKET_CODEC, (component -> component.stack),
		PacketCodecs.BOOLEAN, (component -> component.loaded),
		PacketCodecs.VAR_INT, (component -> component.ticksSinceShot),
		BlessedComponent::new
	);

	public boolean isBlessed() {
		return !this.stack().isEmpty();
	}

	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		MutableText loaded = this.loaded() ?
			Text.translatable("item.abysm.harpoon.loaded") :
			Text.translatable("item.abysm.harpoon.not_loaded");

		textConsumer.accept(loaded.formatted(Formatting.YELLOW, Formatting.UNDERLINE));

		if (!this.stack().isEmpty()) {
			textConsumer.accept(scrollingGradient(Text.translatable("item.abysm.harpoon.blessed"), SEVEN_HUNDRED, RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK, false));
		}
	}

	public static MutableText scrollingGradient(Text original, int wrap, float reciprocalWrap, int startColor, int endColor, boolean forward) {
		String blessed = original.getString();
		int length = blessed.length();
		long time = Util.getMeasuringTimeMs();
		MutableText text = Text.literal(blessed.substring(0, 1)).withColor(ColorHelper.lerp(((time) % wrap) * reciprocalWrap, startColor, endColor));
		float incr = (float) wrap / length;
		for (int i = 1; i < length; i++) {
			float deltaHalfCalculated;
			if (forward) {
				deltaHalfCalculated = time - (int) (incr * i);
			} else {
				deltaHalfCalculated = time + (int) (incr * i);
			}
			text.append(Text.literal(blessed.substring(i, i + 1)).withColor(ColorHelper.lerp((deltaHalfCalculated % wrap) * reciprocalWrap, startColor, endColor)));
		}
		return text;
	}


	public Builder buildNew() {
		return new Builder().stack(this.stack()).loaded(this.loaded()).ticksSinceShot(this.ticksSinceShot());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BlessedComponent(ItemStack stack1, boolean loaded1, int sinceShot))) return false;

		return ItemStack.areItemsAndComponentsEqual(this.stack(), stack1) && this.loaded() == loaded1 && this.ticksSinceShot() == sinceShot;
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

		public BlessedComponent build() {
			return new BlessedComponent(this.heart, this.loaded, this.ticksSinceShot);
		}
	}
}
