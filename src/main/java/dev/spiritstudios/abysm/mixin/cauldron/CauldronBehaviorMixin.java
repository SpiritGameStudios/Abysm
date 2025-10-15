package dev.spiritstudios.abysm.mixin.cauldron;

import dev.spiritstudios.abysm.block.BrineCauldronBlock;
import dev.spiritstudios.abysm.fluids.AbysmCauldronBehaviors;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {
	@Shadow
	static void registerBucketBehavior(Map<Item, CauldronBehavior> behavior) {
	}

	@Inject(method = "registerBehavior", at = @At("TAIL"))
	private static void registerCauldronBehavior(CallbackInfo ci) {
		registerBucketBehavior(BrineCauldronBlock.BEHAVIOR.map());
	}

	@Inject(method = "registerBucketBehavior", at = @At("TAIL"))
	private static void registerBrineBucket(Map<Item, CauldronBehavior> behavior, CallbackInfo ci) {
		AbysmCauldronBehaviors.BUCKETABLE_MAPS.add(behavior);
	}
}
