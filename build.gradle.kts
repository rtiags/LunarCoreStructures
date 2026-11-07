import java.security.MessageDigest
import java.util.zip.ZipFile

plugins {
    java
}

val lunarCoreVersion: String by project

// A server directory may retain libraries from several Paper builds. Resolve only
// the bundle matching the patched server hash, rather than mixing all cached JARs.
val localPaperHome = providers.gradleProperty("lbr.paperServerHome").orNull
if (localPaperHome != null) {
    val home = file(localPaperHome)
    val nativeJar = home.resolve("versions/26.3/paper-26.3.jar")
    require(nativeJar.isFile) { "Expected bootstrapped Paper 26.3 at $nativeJar" }
    val hash = MessageDigest.getInstance("SHA-256").digest(nativeJar.readBytes())
        .joinToString("") { "%02x".format(it) }
    val entries = home.listFiles().orEmpty().filter { it.isFile && it.extension == "jar" }.firstNotNullOfOrNull { jar ->
        runCatching {
            ZipFile(jar).use { zip ->
                val versions = zip.getEntry("META-INF/versions.list") ?: return@use null
                val matches = zip.getInputStream(versions).bufferedReader().use { reader ->
                    reader.readLines().any { it.substringBefore('\t') == hash }
                }
                if (!matches) return@use null
                zip.getInputStream(zip.getEntry("META-INF/libraries.list")).bufferedReader().use { it.readLines() }
            }
        }.getOrNull()
    } ?: error("Keep the matching Paper server bundle JAR in $home for local native builds")
    val libraries = entries.filter { it.isNotBlank() }.map { home.resolve("libraries/" + it.substringAfterLast('\t')) }
    require(libraries.all { it.isFile }) { "Bootstrap the matching Paper bundle to extract all native dependencies" }
    extra["lbrLocalNativeClasspath"] = files(nativeJar, libraries)
}

allprojects {
    group = "com.lunarboreal"
    version = rootProject.version
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        providers.gradleProperty("lunarCoreRepository").orNull?.let { maven { url = uri(it) } }
        mavenLocal()
    }
}
subprojects {
    apply(plugin = "java")
    java { toolchain.languageVersion.set(JavaLanguageVersion.of(25)) }
    tasks.withType<JavaCompile>().configureEach { options.encoding = "UTF-8"; options.release.set(25) }
    dependencies {
        val apiJar = providers.gradleProperty("lunarCoreApi").orNull
        val api = if (apiJar == null) "com.lunarboreal:lbr-api:$lunarCoreVersion" else files(apiJar)
        "compileOnly"(api)
        "testImplementation"(api)
        "testImplementation"("org.junit.jupiter:junit-jupiter:5.11.4")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        val testTemp = layout.buildDirectory.dir("test-tmp")
        systemProperty("java.io.tmpdir", testTemp.get().asFile.absolutePath)
        doFirst { testTemp.get().asFile.mkdirs() }
    }
    tasks.withType<ProcessResources>().configureEach {
        val props = mapOf("version" to project.version.toString())
        inputs.properties(props)
        filesMatching("paper-plugin.yml") { expand(props) }
    }
    tasks.withType<Jar>().configureEach {
        archiveVersion.set("")
        from(rootProject.file("LICENSE")) { into("META-INF") }
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
}
