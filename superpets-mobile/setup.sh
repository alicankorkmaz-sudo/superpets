#!/bin/bash

# Check if template.properties exists
if [ ! -f "template.properties" ]; then
    echo "Error: template.properties not found"
    exit 1
fi

# Function to read property value
get_property() {
    local key=$1
    local value=$(grep "^$key=" template.properties | cut -d'=' -f2)
    echo "$value"
}

# Read properties
APP_NAME=$(get_property "app.name")
APP_PACKAGE=$(get_property "app.package")
APP_VERSION=$(get_property "app.version")
APP_ORGANIZATION=$(get_property "app.organization")
MIN_SDK=$(get_property "min.sdk")
TARGET_SDK=$(get_property "target.sdk")

# Convert package name to directory structure
PACKAGE_DIR=$(echo $APP_PACKAGE | tr . /)

# Convert app name to resource package name (lowercase, underscores)
RESOURCE_PACKAGE=$(echo $APP_NAME | tr '[:upper:]' '[:lower:]' | tr ' ' '_')

# Function to replace package name in files
replace_package_name() {
    local file=$1
    if [ -f "$file" ]; then
        # Replace package declarations
        sed -i '' "s/package com.template.mvp/package $APP_PACKAGE/g" "$file"
        # Replace imports
        sed -i '' "s/import com.template.mvp/import $APP_PACKAGE/g" "$file"
        # Replace resource package references
        sed -i '' "s/kmp_app_template/$RESOURCE_PACKAGE/g" "$file"
        sed -i '' "s/import kmp_app_template/import $RESOURCE_PACKAGE/g" "$file"
    fi
}

# Function to process directory recursively
process_directory() {
    local dir=$1
    for file in $(find "$dir" -type f -name "*.kt" -o -name "*.kts" -o -name "*.xcconfig" -o -name "*.pbxproj"); do
        replace_package_name "$file"
    done
}

# Update Android configuration
echo "Updating Android configuration..."
sed -i '' "s/namespace = \"com.template.mvp\"/namespace = \"$APP_PACKAGE\"/" composeApp/build.gradle.kts
sed -i '' "s/applicationId = \"com.template.mvp\"/applicationId = \"$APP_PACKAGE\"/" composeApp/build.gradle.kts
sed -i '' "s/versionName = \"1.0\"/versionName = \"$APP_VERSION\"/" composeApp/build.gradle.kts
sed -i '' "s/minSdk = 24/minSdk = $MIN_SDK/" composeApp/build.gradle.kts
sed -i '' "s/targetSdk = 35/targetSdk = $TARGET_SDK/" composeApp/build.gradle.kts
sed -i '' "s/packageName = \"com.template.mvp\"/packageName = \"$APP_PACKAGE\"/" composeApp/build.gradle.kts

# Update iOS configuration
echo "Updating iOS configuration..."
sed -i '' "s/BUNDLE_ID=com.template.mvp/BUNDLE_ID=$APP_PACKAGE/" iosApp/Configuration/Config.xcconfig
sed -i '' "s/APP_NAME=MVPTemplate/APP_NAME=$APP_NAME/" iosApp/Configuration/Config.xcconfig
sed -i '' "s/ORGANIZATIONNAME = \"com.template.mvp\"/ORGANIZATIONNAME = \"$APP_ORGANIZATION\"/" iosApp/iosApp.xcodeproj/project.pbxproj

# Process all Kotlin files
echo "Updating package names in Kotlin files..."
process_directory "composeApp/src"

# Create new package directory structure for all source sets
echo "Creating new package directory structure..."
for source_set in "commonMain" "androidMain" "iosMain"; do
    NEW_PACKAGE_DIR="composeApp/src/$source_set/kotlin/$PACKAGE_DIR"
    mkdir -p "$NEW_PACKAGE_DIR"

    # Move files to new package structure
    echo "Moving files in $source_set..."
    if [ -d "composeApp/src/$source_set/kotlin/com/template/mvp" ]; then
        cp -r "composeApp/src/$source_set/kotlin/com/template/mvp/"* "$NEW_PACKAGE_DIR/"
        rm -rf "composeApp/src/$source_set/kotlin/com/template/mvp"
    fi

    # Clean up leftover com directory if empty
    if [ -d "composeApp/src/$source_set/kotlin/com" ]; then
        if [ -z "$(ls -A composeApp/src/$source_set/kotlin/com)" ]; then
            rm -rf "composeApp/src/$source_set/kotlin/com"
        fi
    fi
done

# Update AndroidManifest.xml
echo "Updating AndroidManifest.xml..."
sed -i '' "s/package=\"com.template.mvp\"/package=\"$APP_PACKAGE\"/" composeApp/src/androidMain/AndroidManifest.xml
sed -i '' "s/android:name=\".Application\"/android:name=\"$APP_PACKAGE.Application\"/" composeApp/src/androidMain/AndroidManifest.xml
sed -i '' "s/android:name=\".MainActivity\"/android:name=\"$APP_PACKAGE.MainActivity\"/" composeApp/src/androidMain/AndroidManifest.xml

# Update settings.gradle.kts
echo "Updating project settings..."
sed -i '' "s/rootProject.name = \"KMP-App-Template\"/rootProject.name = \"$APP_NAME\"/" settings.gradle.kts

# Clean up build directories
echo "Cleaning build directories..."
rm -rf composeApp/build
rm -rf iosApp/build

echo "Template setup complete! You can now start developing your app." 