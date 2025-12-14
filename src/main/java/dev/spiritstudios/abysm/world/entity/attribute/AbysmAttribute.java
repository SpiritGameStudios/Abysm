package dev.spiritstudios.abysm.world.entity.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

/**
 * @author Ampflower
 */
@SuppressWarnings("JavadocDeclaration")
public record AbysmAttribute(
		Holder<Attribute> attribute,
		AttributeModifier modifier
) {
	/**
	 * @author SkyNotTheLimit
	 */
	public static final Codec<AbysmAttribute> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Attribute.CODEC.fieldOf("type").forGetter(AbysmAttribute::attribute),
							AttributeModifier.MAP_CODEC.forGetter(AbysmAttribute::modifier)
					)
					.apply(instance, AbysmAttribute::new)
	);

	/**
	 * @author SkyNotTheLimit
	 */
	public static final StreamCodec<RegistryFriendlyByteBuf, AbysmAttribute> PACKET_CODEC = StreamCodec.composite(
			Attribute.STREAM_CODEC, AbysmAttribute::attribute,
			AttributeModifier.STREAM_CODEC, AbysmAttribute::modifier,
			AbysmAttribute::new
	);

	private AbysmAttribute(
			final Holder<Attribute> attribute,
			final String abysmId,
			final double value,
			final AttributeModifier.Operation operation
	) {
		this(attribute, new AttributeModifier(Abysm.id(abysmId), value, operation));
	}

	public static AbysmAttribute ofAdd(
			final Holder<Attribute> attribute,
			final String abysmId,
			final double value
	) {
		return new AbysmAttribute(attribute, abysmId, value, AttributeModifier.Operation.ADD_VALUE);
	}

	@SuppressWarnings("unused")
	public static AbysmAttribute ofMultipliedBase(
			final Holder<Attribute> attribute,
			final String abysmId,
			final double value
	) {
		return new AbysmAttribute(attribute, abysmId, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	@SuppressWarnings("unused")
	public static AbysmAttribute ofMultipliedTotal(
			final Holder<Attribute> attribute,
			final String abysmId,
			final double value
	) {
		return new AbysmAttribute(attribute, abysmId, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	public ItemAttributeModifiers.Entry toEntry(
			final EquipmentSlotGroup slot
	) {
		return new ItemAttributeModifiers.Entry(this.attribute(), this.modifier(), slot);
	}

	public ItemAttributeModifiers toComponent(
			final EquipmentSlotGroup slot
	) {
		return new ItemAttributeModifiers(List.of(this.toEntry(slot)));
	}

	/**
	 * @author SkyNotTheLimit
	 */
	@SuppressWarnings("unused")
	public boolean matches(Holder<Attribute> attribute, Identifier modifierId) {
		return attribute.equals(this.attribute) && this.modifier.is(modifierId);
	}
}
