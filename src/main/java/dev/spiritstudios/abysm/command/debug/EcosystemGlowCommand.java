//package dev.spiritstudios.abysm.command.debug;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.brigadier.suggestion.SuggestionProvider;
//import dev.spiritstudios.abysm.Abysm;
//import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
//import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
//import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
//import dev.spiritstudios.abysm.core.registries.AbysmRegistries;
//import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
//import dev.spiritstudios.spectre.api.registry.MetatagKey;
//import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
//import net.minecraft.ChatFormatting;
//import net.minecraft.SharedConstants;
//import net.minecraft.Util;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.commands.SharedSuggestionProvider;
//import net.minecraft.commands.arguments.ResourceArgument;
//import net.minecraft.commands.synchronization.SuggestionProviders;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.ChunkPos;
//import net.minecraft.world.level.entity.EntityTypeTest;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import java.util.Set;
//FIXME
//public class EcosystemGlowCommand {
//	private static final Set<EcosystemData> currentlyGlowing = new ObjectOpenHashSet<>();
//
//	public static final SuggestionProvider<CommandSourceStack> ECOSYSTEM_ENTITIES = SuggestionProviders.register(
//		Abysm.id("abysm_entities_command_provider"),
//		(commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggestResource(
//			AbysmRegistries.ECOSYSTEM_TYPE.listElementIds(),
//			suggestionsBuilder,
//			ResourceKey::location,
//			ecosystemType -> Component.translatable(Util.makeDescriptionId("ecosystem_type", ecosystemType.location()))
//		)
//	);
//
//	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
//		dispatcher.register(Commands.literal("ecoglow")
//			.requires(source -> source.hasPermission(3))
//			.then(
//				Commands.argument("ecosystem_type_param", ResourceArgument.resource(registryAccess, AbysmRegistryKeys.ECOSYSTEM_TYPE))
//					.suggests(ECOSYSTEM_ENTITIES)
//					.executes(
//						context -> toggleGlow(context.getSource(), ResourceArgument.getResource(context, "ecosystem_type_param", AbysmRegistryKeys.ECOSYSTEM_TYPE))
//					)
//			)
//		);
//	}
//
//	private static int toggleGlow(CommandSourceStack source, EntityType<?> type) throws CommandSyntaxException {
//		ServerLevel world = source.getLevel();
//		ServerPlayer player = source.getPlayerOrException();
//		ChunkPos playerChunk = player.chunkPosition();
//		Vec3 searchCenter = Vec3.atLowerCornerOf(playerChunk.getMiddleBlockPosition((int) player.getY()));
//
//		EcosystemData ecosystemData = type.getDataOrThrow(AbysmMetatags.ECOSYSTEM_DATA);
//		int chunkSearchRange = 2;
//		int searchRange = chunkSearchRange * (SharedConstants.WORLD_RESOLUTION * 2); // convert chunks to blocks doubled for diameter
//
//		if (currentlyGlowing.contains(ecosystemData)) {
//			int removalRange = searchRange * 2;
//			AABB box = AABB.ofSize(searchCenter, removalRange, removalRange, removalRange);
//
//			// This is super cursed because I think it technically checks every entity around you
//			// but its debug so it's fine for now
//			world.getEntities(EntityTypeTest.forClass(type.getBaseClass()), box, entity -> ecosystemTypesMatch(entity, ecosystemData)).forEach(entity -> {
//				if (entity instanceof LivingEntity livingEntity) {
//					livingEntity.removeEffect(MobEffects.GLOWING);
//				}
//			});
//
//			currentlyGlowing.remove(ecosystemData);
//			source.sendSuccess(() -> Component.literal("Removed glow!").withStyle(ChatFormatting.DARK_GREEN), false);
//		} else {
//			AABB box = AABB.ofSize(searchCenter, searchRange, searchRange * 2, searchRange);
//
//			world.getEntities(EntityTypeTest.forClass(type.getBaseClass()), box, entity -> ecosystemTypesMatch(entity, ecosystemData)).forEach(entity -> {
//				if (entity instanceof LivingEntity livingEntity) {
//					// 5 minutes in case removal doesn't work
//					MobEffectInstance glowingEffectInstance = new MobEffectInstance(MobEffects.GLOWING, 6000, 1);
//					livingEntity.addEffect(glowingEffectInstance);
//				}
//			});
//
//			currentlyGlowing.add(ecosystemData);
//			source.sendSuccess(() -> Component.literal("Added glow!").withStyle(ChatFormatting.GREEN), false);
//		}
//		return 1;
//	}
//
//	private static boolean ecosystemTypesMatch(Entity entity, EcosystemData type) {
//		if (!entity.isAlive()) return false;
//
//		return entity.getType().getData(AbysmMetatags.ECOSYSTEM_DATA).orElse(null) == type;
//	}
//
//}
