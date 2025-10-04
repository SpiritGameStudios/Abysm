package dev.spiritstudios.abysm.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * @author Ampflower
 **/
public record AbysmAttribute(
		RegistryEntry<EntityAttribute> attribute,
		EntityAttributeModifier modifier
) {
	/**
	 * @author SkyNotTheLimit
	 * */
	public static final Codec<AbysmAttribute> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							EntityAttribute.CODEC.fieldOf("type").forGetter(AbysmAttribute::attribute),
							EntityAttributeModifier.MAP_CODEC.forGetter(AbysmAttribute::modifier)
					)
					.apply(instance, AbysmAttribute::new)
	);

	/**
	 * @author SkyNotTheLimit
	 * */
	public static final PacketCodec<RegistryByteBuf, AbysmAttribute> PACKET_CODEC = PacketCodec.tuple(
			EntityAttribute.PACKET_CODEC, AbysmAttribute::attribute,
			EntityAttributeModifier.PACKET_CODEC, AbysmAttribute::modifier,
			AbysmAttribute::new
	);

	private AbysmAttribute(
			final RegistryEntry<EntityAttribute> attribute,
			final String abysmId,
			final double value,
			final EntityAttributeModifier.Operation operation
	) {
		this(attribute, new EntityAttributeModifier(Abysm.id(abysmId), value, operation));
	}

	public static AbysmAttribute ofAdd(
			final RegistryEntry<EntityAttribute> attribute,
			final String abysmId,
			final double value
	) {
		return new AbysmAttribute(attribute, abysmId, value, EntityAttributeModifier.Operation.ADD_VALUE);
	}

	public static AbysmAttribute ofMultipliedBase(
			final RegistryEntry<EntityAttribute> attribute,
			final String abysmId,
			final double value
	) {
		return new AbysmAttribute(attribute, abysmId, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

	public static AbysmAttribute ofMultipliedTotal(
			final RegistryEntry<EntityAttribute> attribute,
			final String abysmId,
			final double value
	) {
		return new AbysmAttribute(attribute, abysmId, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	public AttributeModifiersComponent.Entry toEntry(
			final AttributeModifierSlot slot
	) {
		return new AttributeModifiersComponent.Entry(this.attribute(), this.modifier(), slot);
	}

	public AttributeModifiersComponent toComponent(
			final AttributeModifierSlot slot
	) {
		return new AttributeModifiersComponent(List.of(this.toEntry(slot)));
	}

	/**
	 * @author SkyNotTheLimit
	 * */
	public boolean matches(RegistryEntry<EntityAttribute> attribute, Identifier modifierId) {
		return attribute.equals(this.attribute) && this.modifier.idMatches(modifierId);
	}
}
