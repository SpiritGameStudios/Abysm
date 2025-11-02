package dev.spiritstudios.abysm.mixin.worldgen;

import com.mojang.datafixers.DataFixer;
import dev.spiritstudios.abysm.duck.RandomStateDuckInterface;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.GeneratingChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.TicketStorage;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Mixin(ChunkMap.class)
public abstract class ServerChunkLoadingManagerMixin extends ChunkStorage implements ChunkHolder.PlayerProvider, GeneratingChunkMap {
	@Shadow
	@Final
	private RandomState randomState;

	private ServerChunkLoadingManagerMixin(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean sync) {
		super(info, folder, fixerUpper, sync);
	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;createState(Lnet/minecraft/core/HolderLookup;Lnet/minecraft/world/level/levelgen/RandomState;J)Lnet/minecraft/world/level/chunk/ChunkGeneratorStructureState;"))
	private void attachFunctionsToNoiseConfig(
		ServerLevel world,
		LevelStorageSource.LevelStorageAccess session,
		DataFixer dataFixer,
		StructureTemplateManager structureTemplateManager,
		Executor executor,
		BlockableEventLoop<Runnable> mainThreadExecutor,
		LightChunkGetter chunkProvider,
		ChunkGenerator chunkGenerator,
		ChunkProgressListener worldGenerationProgressListener,
		ChunkStatusUpdateListener chunkStatusChangeListener,
		Supplier<DimensionDataStorage> persistentStateManagerFactory,
		TicketStorage ticketManager,
		int viewDistance,
		boolean dsync,
		CallbackInfo ci
	) {
		((RandomStateDuckInterface) (Object) this.randomState).abysm$attachBonusFunctions(world);
	}
}
