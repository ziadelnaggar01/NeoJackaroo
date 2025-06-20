# Exit immediately if a command fails
$ErrorActionPreference = "Stop"

# Define color message functions
function Info($msg) { Write-Host ">>> $msg" -ForegroundColor Cyan }
function Success($msg) { Write-Host "[OK] $msg" -ForegroundColor Green }
function ErrorMsg($msg) { Write-Host "[ERROR] $msg" -ForegroundColor Red }

# Define paths
$buildDir = "buildsystem"
$runtimeDir = "$buildDir/NeoJackaroo-runtime"
$distDir = "$buildDir/dist"
$mainJar = "NeoJackaroo.jar"
$iconPath = "src/icon.ico"
$appName = "NeoJackaroo"

Info "Cleaning and building shadow JAR..."
Push-Location $buildDir
./gradlew clean shadowJar
Pop-Location

Info "Deleting old runtime directory (if exists)..."
if (Test-Path $runtimeDir) {
    Remove-Item -Recurse -Force $runtimeDir
}

Info "Creating custom Java runtime image..."
jlink `
  --add-modules java.base,java.sql,javafx.controls,javafx.fxml,javafx.media,javafx.graphics `
  --output $runtimeDir `
  --strip-debug `
  --compress=2 `
  --no-header-files `
  --no-man-pages

Info "Packaging application with jpackage..."
jpackage `
  --name $appName `
  --input "$buildDir/build/libs" `
  --main-jar $mainJar `
  --runtime-image $runtimeDir `
  --dest $distDir `
  --icon $iconPath `
  --win-shortcut `
  --win-menu `
  --type msi `
  --app-version 1.0 `
  --vendor "Ziad Elnaggar" `
  --copyright "Copyright © 2024 Ziad Elnaggar. All rights reserved."

Success "Installer created successfully in '$distDir/'"
