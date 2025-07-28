package dev.spiritstudios.abysm.client.render;

import net.minecraft.client.render.entity.state.EntityRenderState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class GeoUtil {
	public static <T, R extends GeoRenderState> @NotNull T getOrDefaultGeoData(R state, DataTicket<T> dataTicket, T defaultValue) {
		T value = state.getOrDefaultGeckolibData(dataTicket, defaultValue);
		if (value == null) value = defaultValue;
		return value;
	}

	public static <R extends GeoRenderState> boolean getOrDefaultGeoData(R state, DataTicket<Boolean> dataTicket, boolean defaultValue) {
		Boolean value = state.getOrDefaultGeckolibData(dataTicket, defaultValue);
		if (value == null) value = defaultValue;
		return value;
	}

	public static <R extends GeoRenderState> double getOrDefaultGeoData(R state, DataTicket<Double> dataTicket, double defaultValue) {
		Double value = state.getOrDefaultGeckolibData(dataTicket, defaultValue);
		if (value == null) value = defaultValue;
		return value;
	}

	public static <R extends GeoRenderState> double getAge(R state, double defaultValue) {
		if (state instanceof EntityRenderState entityRenderState) {
			return entityRenderState.age;
		}
		return getOrDefaultGeoData(state, DataTickets.TICK, defaultValue);
	}
}
