# UI Preview - Min Activity Tracker Enhanced Features

## Home Screen - Overview Tab

### Layout Structure
```
┌─────────────────────────────────────────┐
│         Overview                         │
├─────────────────────────────────────────┤
│  ┌───────────┐  ┌───────────────┐       │
│  │  Today    │  │  This Week    │       │
│  │   2h 45m  │  │   15h 30m     │       │
│  └───────────┘  └───────────────┘       │
├─────────────────────────────────────────┤
│        Usage by Category                 │
│  ┌─────────────────────────────────┐    │
│  │   [Column Chart]                │    │
│  │    ■ Social Network: 8h         │    │
│  │    ■ Entertainment: 4h          │    │
│  │    ■ Productivity: 3h           │    │
│  └─────────────────────────────────┘    │
├─────────────────────────────────────────┤
│         Top Apps Today                   │
│  ┌─────────────────────────────────┐    │
│  │ 📱 Instagram        2h 15m      │    │
│  │    Social Network · 12 sessions │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │ 📱 YouTube          1h 45m      │    │
│  │    Video · 8 sessions           │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │ 📱 Gmail            45m         │    │
│  │    Communication · 15 sessions  │    │
│  └─────────────────────────────────┘    │
├─────────────────────────────────────────┤
│         Recent Activity                  │
│  ┌─────────────────────────────────┐    │
│  │ chrome                          │    │
│  │ Started at 14:30        15m     │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │ whatsapp                        │    │
│  │ Started at 14:15        12m     │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

### Design Features
- **Material 3 Cards**: Elevated cards with subtle shadows
- **Color Scheme**: 
  - Primary Container for summary cards (blue/teal)
  - Surface for app cards (white/dark based on theme)
  - Secondary Container for category cards (purple/pink)
- **Typography**: Material 3 type scale with proper hierarchy
- **Spacing**: Consistent 16dp padding, 8dp item spacing

## Apps Screen - Apps Tab

### Layout Structure
```
┌─────────────────────────────────────────┐
│         Apps Usage                       │
├─────────────────────────────────────────┤
│  ┌─────┐ ┌──────┐ ┌───────┐ ┌─────────┐│
│  │Today│ │ Week │ │ Month │ │All Time ││
│  └─────┘ └──────┘ └───────┘ └─────────┘│
├─────────────────────────────────────────┤
│        Usage by Category                 │
│  ┌─────────────────────────────────┐    │
│  │   [Column Chart]                │    │
│  │   Visual comparison of          │    │
│  │   category usage                │    │
│  └─────────────────────────────────┘    │
├─────────────────────────────────────────┤
│  ┌─────────────────────────────────┐    │
│  │ 🎮 Games                15h 30m  │    │
│  │    5 apps · 45 sessions         │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │ 📱 Social Network       12h 45m  │    │
│  │    3 apps · 38 sessions         │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │ 💼 Productivity         8h 20m   │    │
│  │    7 apps · 52 sessions         │    │
│  └─────────────────────────────────┘    │
├─────────────────────────────────────────┤
│         All Apps                         │
│  ┌─────────────────────────────────┐    │
│  │ instagram                2h 15m  │    │
│  │ Social Network · 12 sessions    │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │ youtube                  1h 45m  │    │
│  │ Video · 8 sessions              │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │ slack                    1h 20m  │    │
│  │ Productivity · 25 sessions      │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

### Design Features
- **FilterChips**: Material 3 filter chips for time period selection
- **Charts**: Vico column charts with Material 3 theming
- **Category Badges**: Color-coded category indicators
- **Sorting**: By total time (most used first)

## App Categories (14+)

### Category Icons & Colors (Conceptual)
```
🎮 Games            - Purple
📱 Social Network   - Blue
💼 Productivity     - Orange
🎬 Entertainment    - Red
💬 Communication    - Green
🛒 Shopping         - Yellow
📚 Education        - Indigo
🏃 Health & Fitness - Pink
✈️  Travel          - Cyan
📰 News             - Grey
📷 Photography      - Magenta
🎵 Music            - Purple
🎥 Video            - Red
🔧 Utilities        - Blue-Grey
📦 Other            - Grey
```

## Bottom Navigation Bar

```
┌─────────────────────────────────────────┐
│  🏠 Home  📱 Apps  📤 Export  ⚙️ Settings │
└─────────────────────────────────────────┘
```

## Key UI Improvements

### Before
- Simple list of recent sessions
- No categorization
- No summary statistics
- No visual charts
- Package names shown raw

### After
- ✨ Rich summary cards with key metrics
- 📊 Visual charts for easy comprehension
- 🏷️  Automatic app categorization
- ⏱️  Time period filters
- 📱 Cleaner app name display
- 🎨 Material 3 design throughout
- 📈 Category-grouped analytics

## Interaction Flow

### Home Screen
1. User opens app → sees today's summary at top
2. Scrolls down → views category chart
3. Continues → sees top apps
4. Scrolls more → reviews recent activity

### Apps Screen
1. User switches to Apps tab
2. Selects time period (Week by default)
3. Views category chart showing distribution
4. Scrolls to see category cards
5. Continues to all apps list with details

## Responsive Design

- **Portrait**: Single column layout with full-width cards
- **Landscape**: Could add two-column layout (not yet implemented)
- **Different Screens**: Adapts to various Android devices
- **Text Scaling**: Respects user's font size preferences

## Accessibility Features

- **Screen Reader**: All elements have content descriptions
- **Touch Targets**: Minimum 48dp touch target size
- **Color Contrast**: Meets WCAG AA standards
- **Text Size**: Respects system font scaling

## Performance Optimizations

- **LazyColumn**: Efficient scrolling for large lists
- **Flow**: Reactive updates without full recomposition
- **Chart Caching**: Vico handles efficient chart rendering
- **State Management**: Proper scoping prevents unnecessary updates

## Dark Mode Support

All UI elements adapt to dark theme:
- Cards use surface colors
- Text uses on-surface colors
- Charts use theme-appropriate colors
- Proper contrast maintained

## Future UI Enhancements (Not Implemented)

- [ ] Location map view
- [ ] Daily/weekly trend line charts
- [ ] Usage goal progress bars
- [ ] App icons display
- [ ] Swipe actions on cards
- [ ] Pull to refresh
- [ ] Empty state illustrations
- [ ] Onboarding tour
- [ ] Settings for customization
- [ ] Widget for home screen
