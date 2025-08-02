package dev.spiritstudios.abysm.worldgen.structure;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class AbysmStructurePieceTypes {
	// note: vanilla tends to keep the ids on these short, so that is followed here
	public static final StructurePieceType DENSITY_BLOB = register(DensityBlobStructurePiece::new, "DBlob");

	private static StructurePieceType register(StructurePieceType type, String id) {
		// convert to lowercase because vanilla does it, presumably to save characters by removing underscores
		Identifier identifier = Abysm.id(id.toLowerCase(Locale.ROOT));
		return Registry.register(Registries.STRUCTURE_PIECE, identifier, type);
	}

	private static StructurePieceType register(StructurePieceType.Simple type, String id) {
		return register((StructurePieceType) type, id);
	}

	private static StructurePieceType register(StructurePieceType.ManagerAware type, String id) {
		return register((StructurePieceType) type, id);
	}

	public static void init() {
		// NO-OP
	}
}
