rootProject.name = "eazy-release-plugin"

include("eazy-release-plugin-core")
project(":eazy-release-plugin-core").projectDir = file("core")

include("eazyGradleReleasePlugin")
project(":eazyGradleReleasePlugin").projectDir = file("gradle-release-plugin")

include("eazyMavenReleasePlugin")
project(":eazyMavenReleasePlugin").projectDir = file("maven-release-plugin")
