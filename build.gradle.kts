import okhttp3.HttpUrl.Companion.toHttpUrl

plugins {
	alias(libs.plugins.loom)
	alias(libs.plugins.mod.publish)
	`maven-publish`
}

val modVersion: String by project
val branchName: String by project
val slug: String by project
val compatibleVersions: String by project

version = "$modVersion+$branchName"

repositories {
	// ...
	//maven {
	//	url = "https://api.modrinth.com/maven"
	//}


	exclusiveContent {
		forRepository {
			maven {
				name = "Dual Stance"
				url = uri("github.com/mintynoura/dual-stance/tree/main")
			}
		}
		filter {
			includeGroup("io.github.mintynoura.dualstance")
		}
	}

	exclusiveContent {
		forRepository {
			maven {
				name = "Modrinth"
				url = uri("https://api.modrinth.com/maven")
			}
		}
		filter {
			includeGroup("maven.modrinth")
		}
	}

	//mixin squared
	maven { url = uri("https://maven.bawnorton.com/releases") }
}

dependencies {
	minecraft(libs.minecraft)
	api(libs.bundles.fabric)
	//Dual Stance
	compileOnly("maven.modrinth:z6Esg6rK:dceSO2Rx")
}

java {
	withSourcesJar()
	toolchain.languageVersion = JavaLanguageVersion.of(25)
}

loom {
	accessWidenerPath = file("src/main/resources/bloom_festal.accesswidener")
}

tasks.processResources {
	val user: String by project
	val authors: String by project
	val contributors: String by project

	val meta: Map<String, Any> = mapOf(
		"version" to project.version,
		"modId" to providers.gradleProperty("modId"),
		"modName" to providers.gradleProperty("modName"),
		"modDescription" to providers.gradleProperty("modDescription"),
		"homepage" to "https://modrinth.com/mod/$slug",
		"issues" to "https://github.com/$user/$slug/issues",
		"sources" to "https://github.com/$user/$slug",
		"license" to providers.gradleProperty("license"),
		"authors" to authors.split(", ").joinToString("\",\n    \""),
		"contributors" to contributors.split(", ").joinToString("\",\n    \""),
		"members" to "$authors${if (contributors.isEmpty()) "" else ". Contributions by $contributors."}",
		"minecraftVersion" to compatibleVersions.split(", ")[0],
		"fabricLoaderVersion" to libs.versions.fabric.loader,
		"fabricApiVersion" to libs.versions.fabric.api
	)

	inputs.properties(meta)

	filesMatching(listOf("*.mod.json", "META-INF/*mods.toml")) {
		// providers must be invoked manually or else you get stuff like "provider(?)" instead of "mod-id"
		expand(meta.mapValues { when (val value = it.value) {
			is Provider<*> -> value.get()
			else -> value
		}})
	}
}

publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}
}

publishMods {
	file = tasks.jar.flatMap { it.archiveFile }
	additionalFiles.from(tasks.named("sourcesJar"))
	changelog = providers.environmentVariable("CHANGELOG")

	type = version.map { when {
		it.contains("alpha") -> ALPHA
		it.contains("beta") -> BETA
		else -> STABLE
	}}

	val compatibleLoaders: String by project
	val readme: RegularFile = rootProject.layout.projectDirectory.file("README.md")

	modrinth {
		projectId = slug
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")

		minecraftVersions.addAll(compatibleVersions.split(", "))
		modLoaders.addAll(compatibleLoaders.split(", "))

		projectDescription = providers.fileContents(readme).asText.map {
			"<!--DO NOT EDIT MANUALLY: synced from gh readme-->\n$it"
		}

		requires {
			slug = "fabric-api"
			version = libs.versions.fabric.api
		}
	}
}
