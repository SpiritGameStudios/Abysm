package dev.spiritstudios.abysm.command.debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class EcosystemGlowCommand {
	private static final Set<EcosystemType<?>> currentlyGlowing = new ObjectOpenHashSet<>();

	// FIXME - This isn't working with all the ecosystem types except the bloomray and lectorfin for some reason
	public static final SuggestionProvider<ServerCommandSource> ECOSYSTEM_ENTITIES = SuggestionProviders.register(
		Abysm.id("abysm_entities_command_provider"),
		(commandContext, suggestionsBuilder) -> CommandSource.suggestFromIdentifier(
			AbysmRegistries.ECOSYSTEM_TYPE.stream(),
			suggestionsBuilder,
			ecosystemType -> EntityType.getId(ecosystemType.entityType()),
			ecosystemType -> Text.translatable(Util.createTranslationKey("ecosystem_type", EntityType.getId(ecosystemType.entityType())))
		)
	);

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		dispatcher.register(CommandManager.literal("ecoglow")
			.requires(source -> source.hasPermissionLevel(3))
			.then(
				CommandManager.argument("ecosystem_type_param", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, AbysmRegistryKeys.ECOSYSTEM_TYPE))
					.suggests(ECOSYSTEM_ENTITIES)
					.executes(
						context -> toggleGlow(context.getSource(), RegistryEntryReferenceArgumentType.getRegistryEntry(context, "ecosystem_type_param", AbysmRegistryKeys.ECOSYSTEM_TYPE))
					)
			)
		);
	}

	private static int toggleGlow(ServerCommandSource source, RegistryEntry.Reference<EcosystemType<?>> ecosystemTypeReference) throws CommandSyntaxException {
		ServerWorld world = source.getWorld();
		ServerPlayerEntity player = source.getPlayerOrThrow();
		ChunkPos playerChunk = player.getChunkPos();
		Vec3d searchCenter = Vec3d.of(playerChunk.getCenterAtY((int) player.getY()));

		EcosystemType<?> ecosystemType = ecosystemTypeReference.value();
		EntityType<?> entityType = ecosystemType.entityType();
		int searchRange = ecosystemType.populationChunkSearchRadius() * 32; // convert chunks to blocks doubled for diameter

		if(currentlyGlowing.contains(ecosystemType)) {
			int removalRange = searchRange * 2;
			Box box = Box.of(searchCenter, removalRange, removalRange, removalRange);

			// This is super cursed because I think it technically checks every entity around you
			// but its debug so its fine for now
			world.getEntitiesByType(TypeFilter.instanceOf(entityType.getBaseClass()), box, entity -> ecosystemTypesMatch(entity, ecosystemType)).forEach(entity -> {
				if(entity instanceof LivingEntity livingEntity) {
					livingEntity.removeStatusEffect(StatusEffects.GLOWING);
				}
			});

			currentlyGlowing.remove(ecosystemType);
			source.sendFeedback(() -> Text.literal("Removed glow!").formatted(Formatting.DARK_GREEN), false);
		} else {
			Box box = Box.of(searchCenter, searchRange, searchRange * 2, searchRange);

			world.getEntitiesByType(TypeFilter.instanceOf(entityType.getBaseClass()), box, entity -> ecosystemTypesMatch(entity, ecosystemType)).forEach(entity -> {
				if(entity instanceof LivingEntity livingEntity) {
					// 5 minutes incase removal doesn't work
					StatusEffectInstance glowingEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, 6000, 1);
					livingEntity.addStatusEffect(glowingEffectInstance);
				}
			});

			currentlyGlowing.add(ecosystemType);
			source.sendFeedback(() -> Text.literal("Added glow!").formatted(Formatting.GREEN), false);
		}
		return 1;
	}

	private static boolean ecosystemTypesMatch(Entity entity, EcosystemType<?> type) {
		if(!entity.isAlive()) return false;
		if(!(entity instanceof EcologicalEntity ecologicalEntity)) return false;

		return ecologicalEntity.getEcosystemType() == type;
	}

}
