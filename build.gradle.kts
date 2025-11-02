plugins {
	java
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.minotaur)
}

val modId: String by project
val modVersion: String by project

version = "$modVersion+${libs.versions.minecraft.get()}"
base.archivesName = modId

loom {
	splitEnvironmentSourceSets()
}

fabricApi {
	configureDataGeneration {
		client = true
	}
}

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
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")

		content {
			@Suppress("UnstableApiUsage")
			includeGroupAndSubgroups("org.parchmentmc")
		}
	}

	maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
	maven("https://maven.terraformersmc.com/")
	maven("https://jitpack.io/")
	maven("https://api.modrinth.com/maven/")
}

dependencies {
	minecraft(libs.minecraft)
	@Suppress("UnstableApiUsage")
	mappings(
		loom.layered {
			officialMojangMappings()
			parchment(libs.parchment)
		}
	)

	modImplementation(libs.fabric.loader)

	include(libs.bundles.specter)
	modImplementation(libs.bundles.specter)
	modRuntimeOnly(libs.specter.debug)

	modImplementation(libs.geckolib)

	include(libs.biolith)
	modImplementation(libs.biolith)

	modRuntimeOnly(libs.sodium)
	modRuntimeOnly(libs.modmenu)

	modImplementation(libs.fabric.api)
}

tasks.processResources {
	val map = mapOf(
		"version" to modVersion
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
	from("LICENSE") { rename { "${it}_${base.archivesName.get()}" } }
}

modrinth {
	token.set(System.getenv("MODRINTH_TOKEN"))
	projectId.set("abysm")
	versionNumber.set("$modVersion+${libs.versions.minecraft.get()}")
	uploadFile.set(tasks.remapJar)
	versionType = "alpha"
	gameVersions.addAll(libs.versions.minecraft.get())
	loaders.addAll("fabric", "quilt")
	syncBodyFrom.set(rootProject.file("README.md").readText())
	dependencies {
		required.version("fabric-api", libs.versions.fabric.api.get())
		required.version("geckolib", libs.versions.geckolib.get())
		required.version("biolith", libs.versions.biolith.get())
	}
}
