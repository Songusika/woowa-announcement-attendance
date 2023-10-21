import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.openapi.generator") version "6.6.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "com.woowahan"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories.mavenCentral()

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.4.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
}

ktlint {
    filter {
        exclude { it.file.absolutePath.contains("/generated/") }
    }
    filter {
        verbose.set(true)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val openApiPackages = Pair(
    "openapi",
    listOf("openapi.api", "openapi.invoker", "openapi.model"),
)

val openApiGeneratedDir = "openApiGenerate"
val contractDir = "contract"
val dirs = mapOf(
    contractDir to "$rootDir/../docs/contract",
    openApiGeneratedDir to "$buildDir/openapi",
)

val contractFileNames = fileTree(dirs[contractDir]!!)
    .filter { it.extension == "yaml" }
    .map { it.name }

val generateOpenApiTasks = contractFileNames.map { createOpenApiGenerateTask(it) }

tasks.register("createOpenApi") {
    doFirst {
        println("Creating Code By OpenAPI...")
    }
    doLast {
        println("OpenAPI Code created.")
    }
    dependsOn(generateOpenApiTasks)
}

tasks.register("moveGeneratedSources") {
    doFirst {
        println("Moving generated sources...")
    }
    doLast {
        openApiPackages.second
            .map { it.replace(".", "/") }
            .forEach { packagePath ->
                val originDir = file("${dirs[openApiGeneratedDir]}/src/main/kotlin/$packagePath")
                val destinationDir = file("$buildDir/generated/$packagePath")
                originDir.listFiles { file -> file.extension == "kt" }?.forEach { file ->
                    val resolvedFile = destinationDir.resolve(file.name)
                    if (!resolvedFile.exists() && file.name != "Application.kt") {
                        file.copyTo(destinationDir.resolve(file.name), true)
                    }
                }
            }
        println("Generated sources moved.")
    }
    dependsOn("createOpenApi")
}

tasks.register("cleanGeneratedDirectory") {
    doFirst {
        println("Cleaning generated directory...")
    }
    doLast {
        file(dirs[openApiGeneratedDir]!!).deleteRecursively()
        println("Generated directory cleaned.")
    }
    dependsOn("moveGeneratedSources")
}

sourceSets {
    main {
        kotlin.srcDir("$buildDir/generated")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
    dependsOn("cleanGeneratedDirectory")
}

fun createOpenApiGenerateTask(fileName: String) = tasks.register<GenerateTask>("openApiGenerate_$fileName") {
    generatorName.set("kotlin-spring")
    inputSpec.set("${dirs[contractDir]}/$fileName")
    outputDir.set(dirs[openApiGeneratedDir])
    apiPackage.set(openApiPackages.second[0])
    invokerPackage.set(openApiPackages.second[1])
    modelPackage.set(openApiPackages.second[2])
    configOptions.set(
        mapOf(
            "dateLibrary" to "kotlin-spring",
            "useSpringBoot3" to "true",
            "useTags" to "true",
            "interfaceOnly" to "true",
        ),
    )
    templateDir.set("${dirs[contractDir]}/template")
}
