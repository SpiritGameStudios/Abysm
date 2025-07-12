package dev.spiritstudios.abysm.entity.ai;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.leviathan.test.Lehydrathan;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class AbysmSensorTypes {

	public static final SensorType<Lehydrathan.AttackablesSensor> LEHYDRATHAN_ATTACKABLES = register("lehydrathan_attackables", Lehydrathan.AttackablesSensor::new);

	private static <U extends Sensor<?>> SensorType<U> register(String path, Supplier<U> factory) {
		return Registry.register(Registries.SENSOR_TYPE, Abysm.id(path), new SensorType<>(factory));
	}

	public static void init() {
		// NO-OP
	}
}
