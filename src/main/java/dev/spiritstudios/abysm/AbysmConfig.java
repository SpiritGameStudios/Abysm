//package dev.spiritstudios.abysm;
//
//import dev.spiritstudios.specter.api.config.Config;
//import dev.spiritstudios.specter.api.config.ConfigHolder;
//import dev.spiritstudios.specter.api.config.Value;
//
//public final class AbysmConfig extends Config<AbysmConfig> {
//	public static final ConfigHolder<AbysmConfig, ?> HOLDER = ConfigHolder.builder(Abysm.id("abysm"), AbysmConfig.class)
//		.build();
//
//	public static final AbysmConfig INSTANCE = HOLDER.get();
//
//	public final Value<Boolean> underwaterSoundFilters = booleanValue(true).build();
//}
