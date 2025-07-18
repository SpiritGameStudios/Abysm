package dev.spiritstudios.abysm.client.render;

import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class GeoUtil {
	public static <T, R extends GeoRenderState> @NotNull T getOrDefaultGeoData(R state, DataTicket<T> dataTicket, T defaultValue) {
		T value = state.getOrDefaultGeckolibData(dataTicket, defaultValue);
		if (value == null) value = defaultValue;
		return value;
	}
}
