package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.network.MouseInput;

public interface MouseInputPlayer {
	default void spectre$latestInput(MouseInput input) {
		throw new IllegalStateException("Implemented via mixin");
	}

	default MouseInput spectre$latestInput() {
		throw new IllegalStateException("Implemented via mixin");
	}
}
