package dev.spiritstudios.abysm.entity.leviathan;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

// I need a better name for this
public interface ServerWorldLeviathan {

	default Int2ObjectMap<LeviathanPart> abysm$getLeviathanParts() {
		throw new UnsupportedOperationException("Injected interface should be implemented by mixin!");
	}
}
