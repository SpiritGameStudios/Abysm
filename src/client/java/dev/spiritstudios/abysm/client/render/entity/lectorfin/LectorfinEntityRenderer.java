package dev.spiritstudios.abysm.client.render.entity.lectorfin;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.LectorfinRenderState;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.world.entity.ruins.LectorfinEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;

public class LectorfinEntityRenderer extends MobRenderer<LectorfinEntity, LectorfinRenderState, LectorfinEntityRenderer.Model> {
	public static final Identifier TEXTURE_LOCATION = Abysm.id("textures/entity/lectorfin.png");
	public static final Identifier EYES_TEXTURE_LOCATION = Abysm.id("textures/entity/lectorfin_eyes.png");

	private final Map<ResourceKey<FishEnchantment>, FishEnchantmentModel> enchantmentModels = new HashMap<>();

	public LectorfinEntityRenderer(EntityRendererProvider.Context context) {
		super(
			context,
			new Model(context.bakeLayer(
				new ModelLayerLocation(Abysm.id("lectorfin"), "main")
			)),
			1F
		);

//		Minecraft minecraft = Minecraft.getInstance();
//		RegistryAccess registries = minecraft.player.registryAccess();
//
//		this.enchantmentModels = registries.lookupOrThrow(AbysmRegistryKeys.FISH_ENCHANTMENT)
//			.listElements()
//			.collect(Collectors.toMap(
//				Holder.Reference::key,
//				holder -> new FishEnchantmentModel(context, holder)
//			));

	}

	@Override
	public Identifier getTextureLocation(LectorfinRenderState renderState) {
		return TEXTURE_LOCATION;
	}

	@Override
	public LectorfinRenderState createRenderState() {
		return new LectorfinRenderState();
	}

	public static class Model extends EntityModel<LectorfinRenderState> {
		public Model(ModelPart part) {
			super(part);
		}
	}
}
