package com.minactivitytracker.data

enum class AppCategory {
    PRODUCTIVITY,
    SOCIAL_NETWORK,
    ENTERTAINMENT,
    GAMES,
    COMMUNICATION,
    SHOPPING,
    EDUCATION,
    HEALTH_FITNESS,
    TRAVEL,
    NEWS,
    PHOTOGRAPHY,
    MUSIC,
    VIDEO,
    UTILITIES,
    OTHER;

    fun displayName(): String = when (this) {
        PRODUCTIVITY -> "Productivity"
        SOCIAL_NETWORK -> "Social Network"
        ENTERTAINMENT -> "Entertainment"
        GAMES -> "Games"
        COMMUNICATION -> "Communication"
        SHOPPING -> "Shopping"
        EDUCATION -> "Education"
        HEALTH_FITNESS -> "Health & Fitness"
        TRAVEL -> "Travel"
        NEWS -> "News"
        PHOTOGRAPHY -> "Photography"
        MUSIC -> "Music"
        VIDEO -> "Video"
        UTILITIES -> "Utilities"
        OTHER -> "Other"
    }
}

object AppCategoryClassifier {
    private val categoryKeywords = mapOf(
        AppCategory.PRODUCTIVITY to listOf(
            "office", "docs", "sheets", "slides", "note", "calendar", "task", "todo",
            "evernote", "notion", "trello", "asana", "slack", "teams", "zoom",
            "microsoft", "google.docs", "google.sheets", "google.slides"
        ),
        AppCategory.SOCIAL_NETWORK to listOf(
            "facebook", "instagram", "twitter", "tiktok", "snapchat", "linkedin",
            "reddit", "pinterest", "tumblr", "social", "meta"
        ),
        AppCategory.GAMES to listOf(
            "game", "gaming", "play", "clash", "candy", "pokemon", "minecraft",
            "roblox", "fortnite", "pubg", "callofduty"
        ),
        AppCategory.ENTERTAINMENT to listOf(
            "entertainment", "fun", "comedy", "meme"
        ),
        AppCategory.COMMUNICATION to listOf(
            "whatsapp", "messenger", "telegram", "signal", "viber", "wechat",
            "line", "kakao", "discord", "messages", "sms", "chat", "mail", "gmail", "outlook"
        ),
        AppCategory.SHOPPING to listOf(
            "shop", "amazon", "ebay", "alibaba", "walmart", "store", "market",
            "buy", "cart", "shopping"
        ),
        AppCategory.EDUCATION to listOf(
            "learn", "education", "study", "course", "udemy", "coursera", "khan",
            "duolingo", "school", "university", "classroom"
        ),
        AppCategory.HEALTH_FITNESS to listOf(
            "health", "fitness", "workout", "exercise", "gym", "yoga", "meditation",
            "strava", "fitbit", "myfitnesspal", "headspace", "calm"
        ),
        AppCategory.TRAVEL to listOf(
            "travel", "maps", "uber", "lyft", "airbnb", "booking", "hotel",
            "flight", "trip", "navigation", "transit"
        ),
        AppCategory.NEWS to listOf(
            "news", "bbc", "cnn", "nytimes", "guardian", "reuters", "ap", "newspaper"
        ),
        AppCategory.PHOTOGRAPHY to listOf(
            "camera", "photo", "picture", "snap", "photoshop", "lightroom",
            "vsco", "instagram" // Instagram can be both social and photo
        ),
        AppCategory.MUSIC to listOf(
            "music", "spotify", "pandora", "soundcloud", "tidal", "apple.music",
            "youtube.music", "audio", "podcast"
        ),
        AppCategory.VIDEO to listOf(
            "youtube", "netflix", "hulu", "disney", "prime.video", "hbo",
            "video", "streaming", "tv", "twitch"
        ),
        AppCategory.UTILITIES to listOf(
            "util", "tool", "launcher", "cleaner", "battery", "file", "manager",
            "settings", "calculator", "flashlight", "clock", "weather"
        )
    )

    fun classify(packageName: String): AppCategory {
        val lowerPackage = packageName.lowercase()
        
        for ((category, keywords) in categoryKeywords) {
            if (keywords.any { lowerPackage.contains(it) }) {
                return category
            }
        }
        
        return AppCategory.OTHER
    }
}
