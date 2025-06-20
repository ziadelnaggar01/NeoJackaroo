# Exit immediately if a command fails
$ErrorActionPreference = "Stop"

# Define variables
$runtimeDir = "NeoJackaroo-runtime"
$distDir = "dist"
$mainJar = "NeoJackaroo.jar"
$iconPath = "src/icon.ico"
$appName = "NeoJackaroo"

Write-Host ">>> Cleaning and building shadow JAR..."
Push-Location buildsystem
./gradlew clean shadowJar
Pop-Location

Write-Host ">>> Deleting old runtime directory (if exists)..."
if (Test-Path $runtimeDir) {
    Remove-Item -Recurse -Force $runtimeDir
}

Write-Host ">>> Creating custom Java runtime image..."
jlink `
  --add-modules java.base,java.sql,javafx.controls,javafx.fxml,javafx.media,javafx.graphics `
  --output $runtimeDir `
  --strip-debug `
  --compress=2 `
  --no-header-files `
  --no-man-pages

Write-Host ">>> Packaging application with jpackage..."
jpackage `
  --name $appName `
  --input "build/libs" `
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

Write-Host ">>> Installer created successfully in ./$distDir/"
