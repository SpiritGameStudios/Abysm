package dev.spiritstudios.abysm.mixin.ecosystem;

import dev.spiritstudios.abysm.duck.EcosystemManagedWorld;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements EcosystemManagedWorld {
	@Unique
	public EcosystemAreaManager abysm$ecosystemAreaManager = null;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void abysm$createEcosystemAreaManager(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List spawners, boolean shouldTickTime, RandomSequencesState randomSequencesState, CallbackInfo ci) {
		ServerWorld world = (ServerWorld) (Object) this;
		this.abysm$ecosystemAreaManager = new EcosystemAreaManager(world);
	}

	// Instead of using a Fabric API event, simply inject into the tick method because we're already here with a mixin
	@Inject(method = "tick", at = @At("TAIL"))
	public void abysm$tickEcosystemAreaManager(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		this.abysm$ecosystemAreaManager.tick();
	}

	@Override
	public EcosystemAreaManager abysm$getEcosystemAreaManager() {
		return this.abysm$ecosystemAreaManager;
	}
}
