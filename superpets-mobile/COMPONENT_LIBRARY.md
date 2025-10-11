# Superpets Component Library

Complete reference for the Superpets mobile app reusable component library built with Compose Multiplatform.

## Table of Contents

1. [Buttons](#buttons)
2. [Input Fields](#input-fields)
3. [Cards](#cards)
4. [Navigation](#navigation)
5. [Badges](#badges)
6. [Loading Indicators](#loading-indicators)
7. [State Components](#state-components)

---

## Buttons

Location: `ui/components/buttons/Buttons.kt`

### PrimaryButton

Primary action button with the golden yellow brand color.

**Features:**
- Full width by default
- Loading state support
- Optional icon
- Primary color background

**Usage:**
```kotlin
PrimaryButton(
    text = "Get Started",
    onClick = { /* handle click */ }
)

// With icon and loading
PrimaryButton(
    text = "Generate Images",
    onClick = { /* handle click */ },
    icon = Icons.Default.Add,
    isLoading = isGenerating,
    enabled = hasEnoughCredits
)
```

**Parameters:**
- `text: String` - Button text
- `onClick: () -> Unit` - Click handler
- `modifier: Modifier = Modifier`
- `enabled: Boolean = true`
- `isLoading: Boolean = false` - Shows loading spinner
- `icon: ImageVector? = null` - Leading icon
- `fullWidth: Boolean = true`

---

### LargePrimaryButton

Larger version of the primary button for prominent actions.

**Usage:**
```kotlin
LargePrimaryButton(
    text = "Get Started",
    onClick = { navigateToSignup() }
)
```

---

### SecondaryButton

Secondary action button with primary color text and light background.

**Usage:**
```kotlin
SecondaryButton(
    text = "Sign In",
    onClick = { navigateToLogin() }
)
```

---

### TertiaryButton

Outlined button for tertiary actions.

**Usage:**
```kotlin
TertiaryButton(
    text = "Learn More",
    onClick = { /* handle click */ }
)
```

---

### TextButton

Minimal text-only button.

**Usage:**
```kotlin
TextButton(
    text = "Forgot password?",
    onClick = { navigateToPasswordReset() }
)
```

---

## Input Fields

Location: `ui/components/input/TextFields.kt`

### SuperpetsTextField

Base text field component matching the design system.

**Features:**
- Rounded corners
- Primary color focus
- Error state support
- Leading/trailing icons
- Visual transformations

**Usage:**
```kotlin
var text by remember { mutableStateOf("") }

SuperpetsTextField(
    value = text,
    onValueChange = { text = it },
    label = "Name",
    placeholder = "Enter your name"
)
```

**With Error:**
```kotlin
SuperpetsTextField(
    value = email,
    onValueChange = { email = it },
    label = "Email",
    isError = !isValidEmail(email),
    errorMessage = "Please enter a valid email address"
)
```

---

### EmailTextField

Specialized email input with email keyboard.

**Usage:**
```kotlin
EmailTextField(
    value = email,
    onValueChange = { email = it },
    imeAction = ImeAction.Next,
    onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
)
```

---

### PasswordTextField

Password input with visibility toggle.

**Usage:**
```kotlin
PasswordTextField(
    value = password,
    onValueChange = { password = it },
    imeAction = ImeAction.Done,
    onImeAction = { submitForm() }
)
```

---

### SearchTextField

Search input with search icon and clear button.

**Usage:**
```kotlin
SearchTextField(
    value = searchQuery,
    onValueChange = { searchQuery = it },
    placeholder = "Search for a hero",
    onSearch = { performSearch(it) }
)
```

---

## Cards

Location: `ui/components/cards/Cards.kt`

### HeroCard

Card for displaying hero in selection screen.

**Features:**
- Square aspect ratio
- Image with gradient overlay
- Selection indicator
- Hero name label

**Usage:**
```kotlin
HeroCard(
    heroName = "Captain Canine",
    imageUrl = hero.imageUrl,
    isSelected = selectedHeroId == hero.id,
    onClick = { onHeroSelected(hero.id) }
)
```

**In a Grid:**
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    items(heroes) { hero ->
        HeroCard(
            heroName = hero.name,
            imageUrl = hero.imageUrl,
            isSelected = selectedHeroId == hero.id,
            onClick = { onHeroSelected(hero.id) }
        )
    }
}
```

---

### HistoryCard

Card for displaying past edit generations.

**Usage:**
```kotlin
HistoryCard(
    imageUrl = edit.outputImages.first(),
    heroName = edit.heroName,
    timestamp = "3 days ago",
    creditsUsed = edit.creditsUsed,
    onClick = { navigateToDetail(edit.id) }
)
```

---

### CreditPackageCard

Card for credit packages in pricing screen.

**Usage:**
```kotlin
CreditPackageCard(
    packageName = "Popular",
    credits = 25,
    price = "$9.99",
    imageUrl = packageImageUrl,
    isSelected = selectedPackage == "popular",
    isBestValue = true,
    onClick = { selectPackage("popular") }
)
```

---

### StatsCard

Card for displaying statistics on dashboard.

**Usage:**
```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp)
) {
    StatsCard(
        label = "Total creations",
        value = "12",
        modifier = Modifier.weight(1f)
    )
    StatsCard(
        label = "Recent activity",
        value = "3 days ago",
        modifier = Modifier.weight(1f)
    )
}
```

---

## Navigation

Location: `ui/components/navigation/Navigation.kt`

### SuperpetsTopAppBar

Top app bar with back button, title, and optional credit badge.

**Usage:**
```kotlin
Scaffold(
    topBar = {
        SuperpetsTopAppBar(
            title = "Choose Your Hero",
            onBackClick = { navController.popBackStack() },
            credits = userCredits
        )
    }
) { paddingValues ->
    // Screen content
}
```

**With Close Button:**
```kotlin
SuperpetsTopAppBar(
    title = "Credits",
    onCloseClick = { dismiss() },
    credits = null
)
```

---

### SuperpetsHeader

Header with logo and credit badge for landing/home screens.

**Usage:**
```kotlin
Column {
    SuperpetsHeader(credits = userCredits)
    // Rest of screen content
}
```

---

### SuperpetsBottomNavigationBar

Standard bottom navigation bar.

**Usage:**
```kotlin
val navItems = listOf(
    NavigationItem("home", Icons.Default.Home, "Home"),
    NavigationItem("create", Icons.Default.Add, "Create"),
    NavigationItem("history", Icons.Default.History, "History"),
    NavigationItem("profile", Icons.Default.Person, "Profile")
)

Scaffold(
    bottomBar = {
        SuperpetsBottomNavigationBar(
            selectedRoute = currentRoute,
            items = navItems,
            onItemSelected = { route -> navController.navigate(route) }
        )
    }
) { paddingValues ->
    // Screen content
}
```

---

### SuperpetsFloatingBottomNav

Alternative bottom navigation with prominent center button.

**Usage:**
```kotlin
SuperpetsFloatingBottomNav(
    selectedRoute = currentRoute,
    items = navItems.take(4), // Should be 4 items
    onItemSelected = { route -> navController.navigate(route) },
    onCreateClick = { navigateToCreate() }
)
```

---

## Badges

Location: `ui/components/badges/Badges.kt`

### CreditBadge

Badge displaying credit count with diamond icon.

**Usage:**
```kotlin
// Normal badge
CreditBadge(credits = 12)

// With gradient (for special badges)
CreditBadge(
    credits = 12,
    useGradient = true
)
```

---

### FreeCreditsGradientBadge

Special badge for free credits on landing page.

**Usage:**
```kotlin
FreeCreditsGradientBadge(credits = 5)
```

---

### StatusBadge

Badge for status indicators.

**Usage:**
```kotlin
StatusBadge(
    text = "Processing",
    status = BadgeStatus.INFO
)

StatusBadge(
    text = "Completed",
    status = BadgeStatus.SUCCESS
)

StatusBadge(
    text = "Failed",
    status = BadgeStatus.ERROR
)
```

---

## Loading Indicators

Location: `ui/components/loading/LoadingIndicators.kt`

### LoadingIndicator

Simple circular loading spinner.

**Usage:**
```kotlin
Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    LoadingIndicator()
}
```

---

### LoadingScreen

Full-screen loading with optional message.

**Usage:**
```kotlin
if (isLoading) {
    LoadingScreen(message = "Loading heroes...")
}
```

---

### GenerationLoadingScreen

Specialized loading screen for image generation.

**Features:**
- Animated icon
- Progress bar
- Custom message

**Usage:**
```kotlin
// With progress
GenerationLoadingScreen(
    progress = generationProgress, // 0.0 to 1.0
    message = "Creating your superhero..."
)

// Indeterminate
GenerationLoadingScreen(
    progress = null,
    message = "Preparing your images..."
)
```

---

### AnimatedLoadingIcon

Rotating animated icon for custom loading states.

**Usage:**
```kotlin
AnimatedLoadingIcon(size = 120.dp)
```

---

### SkeletonCard

Pulsing placeholder for loading content.

**Usage:**
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2)
) {
    if (isLoading) {
        items(8) {
            SkeletonCard()
        }
    } else {
        items(heroes) { hero ->
            HeroCard(/* ... */)
        }
    }
}
```

---

## State Components

Location: `ui/components/states/States.kt`

### EmptyState

Generic empty state for no content.

**Usage:**
```kotlin
if (heroes.isEmpty()) {
    EmptyState(
        title = "No Heroes Found",
        message = "We couldn't find any heroes. Please try again later.",
        icon = Icons.Default.Search,
        actionText = "Refresh",
        onActionClick = { refreshHeroes() }
    )
}
```

---

### ErrorState

Generic error state with retry.

**Usage:**
```kotlin
if (error != null) {
    ErrorState(
        title = "Something Went Wrong",
        message = error.message ?: "An unexpected error occurred",
        onRetryClick = { retryOperation() },
        dismissText = "Go Back",
        onDismissClick = { navController.popBackStack() }
    )
}
```

---

### InsufficientCreditsState

Specialized state for insufficient credits.

**Usage:**
```kotlin
if (credits < creditsNeeded) {
    InsufficientCreditsState(
        creditsNeeded = creditsNeeded,
        currentCredits = credits,
        onBuyCreditsClick = { navigateToPricing() },
        onDismissClick = { dismiss() }
    )
}
```

---

### NetworkErrorState

Specialized state for network errors.

**Usage:**
```kotlin
if (isNetworkError) {
    NetworkErrorState(
        onRetryClick = { retryRequest() }
    )
}
```

---

### NoResultsState

Specialized state for empty search results.

**Usage:**
```kotlin
if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
    NoResultsState(
        searchQuery = searchQuery,
        onClearClick = { searchQuery = "" }
    )
}
```

---

## Best Practices

### 1. Consistent Spacing

Always use the spacing system from the theme:

```kotlin
val spacing = MaterialTheme.spacing

Column(
    modifier = Modifier.padding(spacing.screenPadding),
    verticalArrangement = Arrangement.spacedBy(spacing.sectionGap)
) {
    // Content
}
```

### 2. Theme Colors

Use theme colors instead of hardcoded values:

```kotlin
// Good
Text(
    text = "Hello",
    color = MaterialTheme.colorScheme.onBackground
)

// Bad
Text(
    text = "Hello",
    color = Color(0xFF000000)
)
```

### 3. Reusable Components

Always prefer using components from this library over creating custom implementations:

```kotlin
// Good
PrimaryButton(
    text = "Submit",
    onClick = { submit() }
)

// Bad
Button(
    onClick = { submit() },
    colors = ButtonDefaults.buttonColors(
        containerColor = Primary
    )
) {
    Text("Submit")
}
```

### 4. Error Handling

Use appropriate state components for different scenarios:

```kotlin
when {
    isLoading -> LoadingScreen()
    error != null -> ErrorState(
        title = "Error",
        message = error.message,
        onRetryClick = { retry() }
    )
    items.isEmpty() -> EmptyState(
        title = "No Items",
        message = "No items to display"
    )
    else -> ItemList(items)
}
```

### 5. Accessibility

Always provide content descriptions for icons:

```kotlin
Icon(
    imageVector = Icons.Default.Home,
    contentDescription = "Home" // Required for accessibility
)
```

---

## Component Checklist

When building new screens, use these components:

- [ ] **Buttons**: PrimaryButton, SecondaryButton, TextButton
- [ ] **Inputs**: EmailTextField, PasswordTextField, SearchTextField
- [ ] **Cards**: HeroCard, HistoryCard, CreditPackageCard, StatsCard
- [ ] **Navigation**: SuperpetsTopAppBar, SuperpetsBottomNavigationBar
- [ ] **Badges**: CreditBadge, StatusBadge
- [ ] **Loading**: LoadingScreen, GenerationLoadingScreen
- [ ] **States**: EmptyState, ErrorState, InsufficientCreditsState

---

## Next Steps

1. **Add custom fonts**: Import Be Vietnam Pro font files
2. **Add icons**: Create custom icons for specific features
3. **Add animations**: Enhance component transitions
4. **Add accessibility**: Improve screen reader support
5. **Add haptic feedback**: Add tactile feedback for buttons

---

**Last Updated:** October 11, 2025
**Version:** 1.0.0
