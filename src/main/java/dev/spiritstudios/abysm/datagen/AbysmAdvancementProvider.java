package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.advancement.AbysmCriteria;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AbysmAdvancementProvider extends FabricAdvancementProvider {

    public AbysmAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        Advancement.Builder.create()
            .parent(new AdvancementEntry(Identifier.ofVanilla("adventure/root"), null))
            .criterion("hero_brine", AbysmCriteria.HERO_BRINE.create())
            .display(
                AbysmItems.BRINE_BUCKET,
                Text.translatable("advancements.adventure.abysm.hero_brine.title"),
                Text.translatable("advancements.adventure.abysm.hero_brine.description"),
                null,
                AdvancementFrame.TASK,
                true,
                true,
                true
            )
            .build(consumer, "hero_brine");
    }

}
