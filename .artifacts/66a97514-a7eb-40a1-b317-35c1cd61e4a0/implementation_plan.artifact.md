# Implementation Plan - Fix NullPointerException in MainActivity

The application crashes on startup with a `NullPointerException` because `MainActivity` attempts to load a `null` fragment into the `fragment_container`. This occurs because the initial call to `loadFragment` passes `null` for the `fragment` parameter, and subsequent navigation items (except "Sobre") also pass `null`.

## Proposed Changes

### [Component Name] Components/Fragments

#### [NEW] [HomeFragment.java](file:///C:/Users/tayss/Documents/java-utfpr/app/app/src/main/java/edu/utfpr/investimentoapp/HomeFragment.java)
- Create a new `HomeFragment` class that extends `Fragment`.
- Implement logic to load and display the list of investments, similar to `ListaInvestimentosActivity`.

#### [NEW] [fragment_home.xml](file:///C:/Users/tayss/Documents/java-utfpr/app/app/src/main/res/layout/fragment_home.xml)
- Define the layout for `HomeFragment`, containing a `ListView` for investments.

#### [NEW] [PlaceholderFragment.java](file:///C:/Users/tayss/Documents/java-utfpr/app/app/src/main/java/edu/utfpr/investimentoapp/PlaceholderFragment.java)
- Create a simple `PlaceholderFragment` to use as a fallback for missing sections (Corretoras, Ativos, Movimentações).

#### [NEW] [fragment_placeholder.xml](file:///C:/Users/tayss/Documents/java-utfpr/app/app/src/main/res/layout/fragment_placeholder.xml)
- Define a simple layout for the placeholder fragment.

### [Component Name] MainActivity

#### [MODIFY] [MainActivity.java](file:///C:/Users/tayss/Documents/java-utfpr/app/app/src/main/java/edu/utfpr/investimentoapp/MainActivity.java)
- Update `onCreate` to load `HomeFragment` instead of `null`.
- Update the `BottomNavigationView` listener to load the appropriate fragments (or `PlaceholderFragment` if not yet implemented) instead of `null`.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors.

### Manual Verification
- Run the application on an emulator or device.
- Verify that the app starts without crashing and displays the "Carteira" (Home) list.
- Navigate through the bottom navigation tabs and ensure each one loads a fragment without crashing.
