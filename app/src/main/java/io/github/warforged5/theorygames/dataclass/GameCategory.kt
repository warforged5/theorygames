package io.github.warforged5.theorygames.dataclass

enum class GameCategory(val displayName: String, val icon: String) {
    HDI("Human Development Index", "📈"),
    GDP("GDP per Capita", "💰"),
    ATOMIC_NUMBER("Atomic Numbers", "⚛️"),
    POPULATION("Population", "👥"),
    AREA("Country Area", "🗺️")
}