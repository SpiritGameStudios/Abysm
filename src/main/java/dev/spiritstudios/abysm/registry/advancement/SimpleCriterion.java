package dev.spiritstudios.abysm.registry.advancement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class SimpleCriterion extends AbstractCriterion<SimpleCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    public AdvancementCriterion<Conditions> create() {
        return super.create(new Conditions());
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public static class Conditions implements AbstractCriterion.Conditions {

        private static final Codec<Conditions> CODEC = Codec.unit(new Conditions());

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }

    }

}
