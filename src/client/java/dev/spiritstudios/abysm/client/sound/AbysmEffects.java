package dev.spiritstudios.abysm.client.sound;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class AbysmEffects {
	public static final Filter UNDERWATER = new LowPassFilter(
		0.5F,
		0.5F
	);

	static {
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
			UNDERWATER.free();
		});
	}
}
