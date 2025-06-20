package dev.spiritstudios.abysm.mixin.loottable;

import com.google.common.collect.ImmutableList;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.LootPoolEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPool.Builder.class)
public interface LootPoolBuilderAccessor {
	@Accessor("entries")
	ImmutableList.Builder<LootPoolEntry> abysm$getEntries();
}
