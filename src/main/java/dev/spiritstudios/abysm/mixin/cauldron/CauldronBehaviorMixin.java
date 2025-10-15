package dev.spiritstudios.abysm.mixin.cauldron;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.block.BrineCauldronBlock;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {

    @Shadow
    private static boolean isUnderwater(World world, BlockPos pos) {
        throw new AssertionError();
    }

    @Shadow
    static ActionResult fillCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
        throw new AssertionError();
    }

    @Shadow static void registerBucketBehavior(Map<Item, CauldronBehavior> behavior) {}

    @Inject(method = "registerBehavior", at = @At("TAIL"))
    private static void registerCauldronBehavior(CallbackInfo ci) {
        registerBucketBehavior(BrineCauldronBlock.BEHAVIOR.map());
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "registerBucketBehavior", at = @At("TAIL"))
    private static void registerBrineBucket(Map<Item, CauldronBehavior> behavior, CallbackInfo ci) {
//        behavior.put(AbysmItems.BRINE_BUCKET, (state, world, pos, player, hand, stack) ->
//            isUnderwater(world, pos) ? ActionResult.CONSUME : fillCauldron(world, pos, player, hand, stack, AbysmBlocks.BRINE_CAULDRON.getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY));
    }

}
