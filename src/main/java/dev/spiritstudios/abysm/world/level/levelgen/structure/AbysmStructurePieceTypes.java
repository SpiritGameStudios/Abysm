package dev.spiritstudios.abysm.world.level.levelgen.structure;

import dev.spiritstudios.abysm.Abysm;
import java.util.Locale;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class AbysmStructurePieceTypes {
	// note: vanilla tends to keep the ids on these short, so that is followed here
	public static final StructurePieceType DENSITY_BLOB = register(DensityBlobStructurePiece::new, "DBlob");

	private static StructurePieceType register(StructurePieceType type, String id) {
		// convert to lowercase because vanilla does it, presumably to save characters by removing underscores
		Identifier identifier = Abysm.id(id.toLowerCase(Locale.ROOT));
		return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, identifier, type);
	}

	private static StructurePieceType register(StructurePieceType.ContextlessType type, String id) {
		return register((StructurePieceType) type, id);
	}

	private static StructurePieceType register(StructurePieceType.StructureTemplateType type, String id) {
		return register((StructurePieceType) type, id);
	}

	public static void init() {
		// NO-OP
	}
}
