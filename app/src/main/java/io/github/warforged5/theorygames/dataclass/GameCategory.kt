package io.github.warforged5.theorygames.dataclass
import kotlinx.serialization.Serializable

@Serializable
enum class GameCategory(val displayName: String, val icon: String) {
    HDI("Human Development Index", "📈"),
    GDP("GDP per Capita", "💰"),
    ATOMIC_NUMBER("Atomic Numbers", "⚛️"),
    POPULATION("Population", "👥"),
    AREA("Country Area", "🗺️"),
    GPU("GPU Performance", "🎮"),
    SCIENTIFIC_CONSTANTS("Scientific Constants", "🔬"),
    STOCK_MARKET("Stock Market Values", "📊"),
    MOVIE_ENTERTAINMENT("Movie & Entertainment", "🎬") // NEW CATEGORY
}