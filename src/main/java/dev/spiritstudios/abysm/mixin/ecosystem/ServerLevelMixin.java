package dev.spiritstudios.abysm.mixin.ecosystem;

import dev.spiritstudios.abysm.duck.EcosystemManagedLevel;
import dev.spiritstudios.abysm.world.ecosystem.chunk.EcosystemAreaManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements EcosystemManagedLevel {
	@Unique
	public EcosystemAreaManager abysm$ecosystemAreaManager = null;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void abysm$createEcosystemAreaManager(MinecraftServer server, Executor dispatcher, LevelStorageSource.LevelStorageAccess storageSource, ServerLevelData levelData, ResourceKey<?> dimension, LevelStem levelStem, boolean isDebug, long biomeZoomSeed, List<?> customSpawners, boolean tickTime, RandomSequences randomSequences, CallbackInfo ci) {
		this.abysm$ecosystemAreaManager = new EcosystemAreaManager((ServerLevel) (Object) this);
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
