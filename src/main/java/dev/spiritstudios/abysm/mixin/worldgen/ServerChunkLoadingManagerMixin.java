package dev.spiritstudios.abysm.mixin.worldgen;

import com.mojang.datafixers.DataFixer;
import dev.spiritstudios.abysm.duck.NoiseConfigDuckInterface;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerChunkLoadingManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.ChunkLoadingManager;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.ChunkStatusChangeListener;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.storage.StorageKey;
import net.minecraft.world.storage.VersionedChunkStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ServerChunkLoadingManager.class)
public abstract class ServerChunkLoadingManagerMixin extends VersionedChunkStorage implements ChunkHolder.PlayersWatchingChunkProvider, ChunkLoadingManager {
	private ServerChunkLoadingManagerMixin(StorageKey storageKey, Path directory, DataFixer dataFixer, boolean dsync) {
		super(storageKey, directory, dataFixer, dsync);
	}

	@Shadow
	@Final
	private NoiseConfig noiseConfig;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;createStructurePlacementCalculator(Lnet/minecraft/registry/RegistryWrapper;Lnet/minecraft/world/gen/noise/NoiseConfig;J)Lnet/minecraft/world/gen/chunk/placement/StructurePlacementCalculator;"))
	private void attachFunctionsToNoiseConfig(
		ServerWorld world,
		LevelStorage.Session session,
		DataFixer dataFixer,
		StructureTemplateManager structureTemplateManager,
		Executor executor,
		ThreadExecutor<Runnable> mainThreadExecutor,
		ChunkProvider chunkProvider,
		ChunkGenerator chunkGenerator,
		WorldGenerationProgressListener worldGenerationProgressListener,
		ChunkStatusChangeListener chunkStatusChangeListener,
		Supplier<PersistentStateManager> persistentStateManagerFactory,
		ChunkTicketManager ticketManager,
		int viewDistance,
		boolean dsync,
		CallbackInfo ci
	) {
		((NoiseConfigDuckInterface) (Object) this.noiseConfig).abysm$attachBonusFunctions(world);
	}
}
