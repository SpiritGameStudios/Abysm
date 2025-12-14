package dev.spiritstudios.abysm.world.entity.ai;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.entity.leviathan.Leviathan;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

public class AbysmSensorTypes {

	public static final SensorType<Leviathan.AttackablesSensor> LEVIATHAN_ATTACKABLES = register("leviathan_attackables", Leviathan.AttackablesSensor::new);

	private static <U extends Sensor<?>> SensorType<U> register(String path, Supplier<U> factory) {
		return Registry.register(BuiltInRegistries.SENSOR_TYPE, Abysm.id(path), new SensorType<>(factory));
	}

	public static void init() {
		// NO-OP
	}
}
