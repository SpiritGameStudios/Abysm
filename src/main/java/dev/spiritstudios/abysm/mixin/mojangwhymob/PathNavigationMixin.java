package dev.spiritstudios.abysm.mixin.mojangwhymob;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.IdentityHashMap;
import java.util.Map;

@Debug(export = true)
@Mixin(PathNavigation.class)
public class PathNavigationMixin {

	@Shadow
	@Final
	protected Mob mob;
	@Shadow
	protected float maxDistanceToWaypoint;
	@Shadow
	@Nullable
	protected Path path;
	@Shadow
	protected boolean hasDelayedRecomputation;
	@Unique
	private static final Map<EntityType<?>, Boolean> ABYSM_ENTITIES = new IdentityHashMap<>();

	@Inject(method = "followThePath", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/navigation/PathNavigation;maxDistanceToWaypoint:F", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
	private void change(CallbackInfo ci) {
		if (!abysm$is(this.mob)) {
			return;
		}
		if (this.mob.getBbWidth() > 1.5F) {
			this.maxDistanceToWaypoint *= 3F;
		}
	}

	@ModifyExpressionValue(method = "followThePath", at = @At(value = "CONSTANT", args = "doubleValue=1.0"))
	private double changeYDiff(double original) {
		if (!abysm$is(this.mob)) {
			return original;
		}
		if (this.mob.getBbWidth() > 1.5F) {
			return original * 2;
		}
		return original;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void noWorldborder(CallbackInfo ci) {
		if (!abysm$is(this.mob)) {
			return;
		}
		if (this.path == null) {
			return;
		}
		Level level = this.mob.level();
		if (level.getGameTime() % 60 != 0) {
			return;
		}
		if (!level.getWorldBorder().isWithinBounds(this.path.getEndNode().asVec3())) {
			this.hasDelayedRecomputation = true;
		}
	}

	@Unique
	private static boolean abysm$is(Entity entity) {
		return ABYSM_ENTITIES.computeIfAbsent(entity.getType(), type ->
			BuiltInRegistries.ENTITY_TYPE.getKey(type).getNamespace().equals(Abysm.MODID)
		);
	}
}
