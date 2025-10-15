package dev.spiritstudios.abysm.mixin.cauldron;

import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.throwables.MixinException;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorAccessor {
	@Invoker("isUnderwater")
	static boolean isUnderwater(World world, BlockPos pos) {
		throw new MixinException("CauldronBehaviorAccessor failed to load!");
	}

	@Invoker("fillCauldron")
	static ActionResult fillCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
		throw new MixinException("CauldronBehaviorAccessor failed to load!");
	}
}
