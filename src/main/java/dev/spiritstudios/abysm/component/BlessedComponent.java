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

import java.awt.Color;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

// remind me to implement TooltipAppender later
public class BlessedComponent implements TooltipAppender {

	public static final BlessedComponent EMPTY = new BlessedComponent.Builder().build();

	protected static final int LIGHT = ColorHelper.fullAlpha(new Color(52, 189, 235).getRGB());
	protected static final int DARK = ColorHelper.fullAlpha(new Color(48, 115, 171).getRGB());

	public static final int SEVEN_HUNDRED = 700;
	public static final float RECIPROCAL_OF_SEVEN_HUNDRED = 1f / SEVEN_HUNDRED;

	public static final Codec<BlessedComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				ItemStack.OPTIONAL_CODEC.optionalFieldOf("heart").forGetter(component-> component.heart),
				Codec.BOOL.optionalFieldOf("loaded").forGetter(component -> component.loaded),
				Codec.INT.optionalFieldOf("ticksSinceShot").forGetter(component -> component.ticksSinceShot)
			)
			.apply(instance, BlessedComponent::new)
	);

	public static final PacketCodec<RegistryByteBuf, BlessedComponent> PACKET_CODEC = PacketCodec.tuple(
		ItemStack.OPTIONAL_PACKET_CODEC.collect(PacketCodecs::optional), (component -> component.heart),
		PacketCodecs.BOOLEAN.collect(PacketCodecs::optional), (component -> component.loaded),
		PacketCodecs.VAR_INT.collect(PacketCodecs::optional), (component -> component.ticksSinceShot),
		BlessedComponent::new
	);

	private final Optional<ItemStack> heart;
	private final Optional<Boolean> loaded;
	private final Optional<Integer> ticksSinceShot;

	public BlessedComponent(ItemStack stack, boolean loaded, int ticksSinceShot) {
		this(Optional.of(stack), Optional.of(loaded), Optional.of(ticksSinceShot));
	}

	public BlessedComponent(Optional<ItemStack> stack, Optional<Boolean> loaded, Optional<Integer> ticksSinceShot) {
		this.heart = stack;
		this.loaded = loaded;
		this.ticksSinceShot = ticksSinceShot;
	}

	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		if (this.isLoaded()) {
			textConsumer.accept(Text.literal("Loaded").formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.UNDERLINE));
		} else {
			textConsumer.accept(Text.literal("Unloaded").formatted(Formatting.LIGHT_PURPLE).formatted(Formatting.UNDERLINE));
		}
		if (!this.getStack().isEmpty()) {
			long time = Util.getMeasuringTimeMs();
			MutableText text = Text.literal("B").withColor(ColorHelper.lerp(((time) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			text.append(Text.literal("l").withColor(ColorHelper.lerp(((time + 100) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK)));
			text.append(Text.literal("e").withColor(ColorHelper.lerp(((time + 200) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK)));
			text.append(Text.literal("s").withColor(ColorHelper.lerp(((time + 300) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK)));
			text.append(Text.literal("s").withColor(ColorHelper.lerp(((time + 400) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK)));
			text.append(Text.literal("e").withColor(ColorHelper.lerp(((time + 500) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK)));
			text.append(Text.literal("d").withColor(ColorHelper.lerp(((time + 600) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK)));
			textConsumer.accept(text);
		}
	}

	public ItemStack getStack() {
		return this.heart.orElse(ItemStack.EMPTY);
	}

	public boolean isLoaded() {
		return this.loaded.orElse(true);
	}

	public int getTicksSinceShot() {
		return this.ticksSinceShot.orElse(0);
	}

	public Builder buildNew() {
		return new Builder().stack(this.getStack()).loaded(this.isLoaded()).ticksSinceShot(this.getTicksSinceShot());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BlessedComponent component)) return false;
		return ItemStack.areItemsAndComponentsEqual(this.getStack(), component.getStack()) && this.isLoaded() == component.isLoaded() && this.getTicksSinceShot() == component.getTicksSinceShot();
	}

	@Override
	public int hashCode() {
		return Objects.hash(heart, loaded, ticksSinceShot);
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
