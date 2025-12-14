plugins {
	java

	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.modpublish)
}

val modVersion = "1.0.0"
val modId = "abysm"
val modName = "Abysm"

val modrinthProject = "abysm"
val githubRepository = "SpiritGameStudios/Abysm"

group = "dev.spiritstudios"
base.archivesName = modId

version = "$modVersion+${libs.versions.minecraft.get()}"

repositories {
	mavenCentral()

	maven {
		name = "Spirit Studios Releases"
		url = uri("https://maven.spiritstudios.dev/releases/")

		content {
			@Suppress("UnstableApiUsage")
			includeGroupAndSubgroups("dev.spiritstudios")
		}
	}

	maven {
		name = "Spirit Studios Snapshots"
		url = uri("https://maven.spiritstudios.dev/snapshots/")

		content {
			@Suppress("UnstableApiUsage")
			includeGroupAndSubgroups("dev.spiritstudios")
		}
	}


	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")

		content {
			@Suppress("UnstableApiUsage")
			includeGroupAndSubgroups("org.parchmentmc")
		}
	}

	maven {
		name = "Cloudsmith (Geckolib)"
		url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")

		content {
			@Suppress("UnstableApiUsage")
			includeGroupAndSubgroups("software.bernie.geckolib")
		}
	}

	maven("https://maven.terraformersmc.com/")
	maven("https://jitpack.io/")
	maven("https://api.modrinth.com/maven/")
}

loom {
	splitEnvironmentSourceSets()
}

fabricApi {
	configureDataGeneration {
		client = true
	}
}

dependencies {
	minecraft(libs.minecraft)
	mappings(
		@Suppress("UnstableApiUsage")
		loom.layered {
			officialMojangMappings()
			parchment(libs.parchment)
		}
	)

	modImplementation(libs.fabric.loader)
	modImplementation(libs.fabric.api)

	modImplementation(libs.spectre)
	include(libs.spectre)

	modImplementation(libs.biolith)
	include(libs.biolith)

	modRuntimeOnly(libs.modmenu)
}

tasks.processResources {
	val map = mapOf(
		"version" to modVersion,
		"loader_version" to libs.versions.fabric.loader.get()
	)

	inputs.properties(map)

	filesMatching("fabric.mod.json") { expand(map) }
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.release = 21
}

tasks.jar {
	from("LICENSE") { rename { "${it}_$modId" } }
}

publishMods {
	file = tasks.remapJar.get().archiveFile
	modLoaders.add("fabric")

	version = modVersion
	type = ALPHA
	displayName = "$modName $modVersion for Minecraft ${libs.versions.minecraft.get()}"

	modrinth {
		accessToken = providers.gradleProperty("secrets.modrinth_token")
		projectId = modrinthProject
		minecraftVersions.add(libs.versions.minecraft.get())

		projectDescription = providers.fileContents(layout.projectDirectory.file("README.md")).asText

		requires("fabric-api")
		requires("biolith")
	}
}
