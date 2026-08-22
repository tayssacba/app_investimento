# Fix Build Error: MPAndroidChart Dependency Missing

The project is failing to build because the `MPAndroidChart` library is being used in a layout file (`fragment_home.xml`), but the dependency is not declared in the build configuration. This causes the generated ViewBinding class `FragmentHomeBinding` to fail compilation.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/tayss/Documents/java-utfpr/app/gradle/libs.versions.toml)
- Add `mpandroidchart` version `3.1.0`.
- Add `mpandroidchart` library definition.

#### [MODIFY] [build.gradle](file:///C:/Users/tayss/Documents/java-utfpr/app/app/build.gradle)
- Add `implementation libs.mpandroidchart` to the dependencies block.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to ensure the project builds successfully.
- Run `./gradlew :app:compileDebugJavaWithJavac` (as requested by the user's error context) to specifically verify the binding class compilation.

### Manual Verification
- N/A (Build fix only)
