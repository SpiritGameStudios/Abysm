plugins {
	java
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.minotaur)
}

class ModInfo {
	val id = property("mod.id").toString()
	val group = property("mod.group").toString()
	val version = property("mod.version").toString()
}

val mod = ModInfo()

version = "${mod.version}+${libs.versions.minecraft.get()}"
group = mod.group

base.archivesName = mod.id

fabricApi {
	configureDataGeneration {
		client = true
	}
}

loom {
	splitEnvironmentSourceSets()

	mods.create(mod.id) {
		sourceSet(sourceSets.getByName("main"))
		sourceSet(sourceSets.getByName("client"))
	}
}

repositories {
	mavenCentral()
	maven("https://maven.spiritstudios.dev/releases/")
	maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
	maven("https://maven.terraformersmc.com/")
	maven("https://jitpack.io/")
	maven("https://api.modrinth.com/maven/")
}

dependencies {
	minecraft(libs.minecraft)
	mappings(variantOf(libs.yarn) { classifier("v2") })
	modImplementation(libs.fabric.loader)

	include(libs.bundles.specter)
	modImplementation(libs.bundles.specter)
	modRuntimeOnly(libs.specter.debug)

	modImplementation(libs.geckolib)

	include(libs.biolith)
	modImplementation(libs.biolith)

	include(libs.fabrik)
	modImplementation(libs.fabrik)

	modRuntimeOnly(libs.sodium)
	modRuntimeOnly(libs.modmenu)

	modImplementation(libs.fabric.api)
}

tasks.processResources {
	val map = mapOf(
		"mod_id" to mod.id,
		"mod_version" to mod.version,
		"fabric_loader_version" to libs.versions.fabric.loader.get(),
		"minecraft_version" to libs.versions.minecraft.get()
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
	projectId.set(mod.id)
	versionNumber.set(mod.version)
	uploadFile.set(tasks.remapJar)
	gameVersions.addAll(libs.versions.minecraft.get())
	loaders.addAll("fabric", "quilt")
	syncBodyFrom.set(rootProject.file("README.md").readText())
	dependencies {
		required.version("fabric-api", libs.versions.fabric.api.get())
		required.version("geckolib", libs.versions.geckolib.get())
	}
}
