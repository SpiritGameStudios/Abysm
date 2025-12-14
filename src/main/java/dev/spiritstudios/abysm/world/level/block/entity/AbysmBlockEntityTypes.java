package dev.spiritstudios.abysm.world.level.block.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class AbysmBlockEntityTypes {
	public static final BlockEntityType<DensityBlobBlockEntity> DENSITY_BLOB_BLOCK = register(
		"density_blob_block",
		DensityBlobBlockEntity::new,
		AbysmBlocks.DENSITY_BLOB_BLOCK
	);

	private static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
		return Registry.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,
			Abysm.id(id),
			FabricBlockEntityTypeBuilder.create(factory, blocks).build()
		);
	}

	public static void init() {
		// NO-OP
	}
}
