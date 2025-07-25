package io.github.warforged5.theorygames.dataclass
import kotlinx.serialization.Serializable

@Serializable
enum class PlayerAvatar(val emoji: String, val names: String) {
    SCIENTIST("🧬", "Scientist"),
    MATHEMATICIAN("📐", "Mathematician"),
    ASTRONOMER("🔭", "Astronomer"),
    CHEMIST("⚗️", "Chemist"),
    PHYSICIST("⚛️", "Physicist"),
    GEOGRAPHER("🌍", "Geographer"),
    ECONOMIST("📊", "Economist"),
    ENGINEER("⚙️", "Engineer")
}