package dev.spiritstudios.abysm.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class AbysmCodecs {
	public static final Codec<Integer> ARGB = Codec.withAlternative(
		Codec.STRING.comapFlatMap(
			AbysmCodecs::parseColor,
			AbysmCodecs::encodeColor
		),
		Codec.INT
	);

	private static final Map<String, Integer> BY_NAME = Stream.of(Formatting.values())
		.filter(Objects::nonNull)
		.filter(format -> format.getColorValue() != null)
		.collect(ImmutableMap.toImmutableMap(
			format -> Objects.requireNonNull(format).getName(),
			format -> Objects.requireNonNull(format).getColorValue()
		));

	public static String encodeColor(int color) {
		return "#" + Integer.toHexString(color);
	}

	public static DataResult<Integer> parseColor(String name) {
		if (name.startsWith("#")) {
			try {
				int color = Integer.parseInt(name.substring(1), 16);
				return DataResult.success(color, Lifecycle.stable());
			} catch (NumberFormatException ignored) {
				return DataResult.error(() -> "Invalid color value: " + name);
			}
		} else {
			Integer color = BY_NAME.get(name);
			return color == null ? DataResult.error(() -> "Invalid color name: " + name) : DataResult.success(color, Lifecycle.stable());
		}
	}
}
