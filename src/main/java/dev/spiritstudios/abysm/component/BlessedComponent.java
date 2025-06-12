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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;

import java.awt.Color;
import java.util.function.Consumer;

// remind me to implement TooltipAppender later
public class BlessedComponent implements TooltipAppender {

	public static final BlessedComponent EMPTY = new BlessedComponent(ItemStack.EMPTY);

	protected static final int LIGHT = ColorHelper.fullAlpha(new Color(52, 189, 235).getRGB());
	protected static final int DARK = ColorHelper.fullAlpha(new Color(48, 115, 171).getRGB());

	public static final int SEVEN_HUNDRED = 700;
	public static final float RECIPROCAL_OF_SEVEN_HUNDRED = 1f / SEVEN_HUNDRED;

	public static final Codec<BlessedComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
				ItemStack.OPTIONAL_CODEC.fieldOf("heart").forGetter((component) -> component.heart)
			)
			.apply(instance, BlessedComponent::new)
	);

	public static final PacketCodec<RegistryByteBuf, BlessedComponent> PACKET_CODEC = PacketCodec.tuple(
		ItemStack.OPTIONAL_PACKET_CODEC, ((component) -> component.heart),
		BlessedComponent::new
	);

	private final ItemStack heart;

	public BlessedComponent(ItemStack stack) {
		this.heart = stack;
	}

	@Override
	public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
		if (!heart.isEmpty()) {
			long time = Util.getMeasuringTimeMs();
			MutableText one = Text.literal("B").withColor(ColorHelper.lerp(((time) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			MutableText two = Text.literal("l").withColor(ColorHelper.lerp(((time + 100) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			MutableText three = Text.literal("e").withColor(ColorHelper.lerp(((time + 200) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			MutableText four = Text.literal("s").withColor(ColorHelper.lerp(((time + 300) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			MutableText five = Text.literal("s").withColor(ColorHelper.lerp(((time + 400) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			MutableText six = Text.literal("e").withColor(ColorHelper.lerp(((time + 500) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			MutableText seven = Text.literal("d").withColor(ColorHelper.lerp(((time + 600) % SEVEN_HUNDRED) * RECIPROCAL_OF_SEVEN_HUNDRED, LIGHT, DARK));
			textConsumer.accept(one.append(two).append(three).append(four).append(five).append(six).append(seven));
		}
	}

	public ItemStack getStack() {
		return this.heart;
	}
}
