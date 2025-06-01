package io.github.warforged5.theorygames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.warforged5.theorygames.ui.theme.TheorygamesTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

// Enhanced Data Models
data class Player(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    var score: Int = 0,
    var streak: Int = 0,
    var totalCorrect: Int = 0,
    var averageAccuracy: Double = 0.0,
    val achievements: MutableList<Achievement> = mutableListOf()
)

data class PlayerGuess(
    val player: Player,
    val guess: Double,
    val difference: Double,
    val isClosest: Boolean = false
)

data class GameSession(
    val players: MutableList<Player> = mutableListOf(),
    val gameType: String = "",
    val questionsAnswered: Int = 0,
    val totalQuestions: Int = 10,
    val difficulty: Difficulty = Difficulty.NORMAL,
    val hasTimer: Boolean = false,
    val timerSeconds: Int = 30,
    val currentStreak: Map<String, Int> = emptyMap()
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val unlockedAt: Long = System.currentTimeMillis()
)

enum class Difficulty(val displayName: String, val emoji: String, val multiplier: Double) {
    EASY("Easy Peasy", "😊", 0.8),
    NORMAL("Brain Teaser", "🤔", 1.0),
    HARD("Genius Mode", "🧠", 1.5),
    EXTREME("Impossible", "💀", 2.0)
}

data class Country(
    val name: String,
    val hdi: Double,
    val emoji: String = "🌍",
    val region: String = "Unknown",
    val funFact: String = ""
)

data class Element(
    val name: String,
    val symbol: String,
    val atomicNumber: Int,
    val category: String = "Unknown",
    val funFact: String = ""
)

// Enhanced Game Manager with more data
class GameManager {
    companion object {
        val achievements = listOf(
            Achievement("first_win", "First Victory! 🎉", "Win your first game", Icons.Default.EmojiEvents),
            Achievement("streak_5", "Hot Streak! 🔥", "Get 5 correct answers in a row", Icons.Default.LocalFireDepartment),
            Achievement("perfectionist", "Perfectionist 💎", "Get 100% accuracy in a game", Icons.Default.Diamond),
            Achievement("chemistry_master", "Chemistry Master ⚗️", "Score 8/10 in atomic numbers", Icons.Default.Science),
            Achievement("geography_guru", "Geography Guru 🌍", "Score 8/10 in HDI game", Icons.Default.Public),
            Achievement("speedster", "Speedster ⚡", "Answer 10 questions in under 5 minutes", Icons.Default.Speed)
        )

        val countries = listOf(
            Country("Iceland", 0.972, "🇮🇸", "Europe", "Land of fire and ice with geothermal energy"),
            Country("Norway", 0.970, "🇳🇴", "Europe", "Home to the midnight sun and Northern Lights"),
            Country("Switzerland", 0.965, "🇨🇭", "Europe", "Famous for chocolate, watches, and neutrality"),
            Country("Denmark", 0.962, "🇩🇰", "Europe", "Consistently ranked as the happiest country"),
            Country("Germany", 0.959, "🇩🇪", "Europe", "Europe's economic powerhouse"),
            Country("Sweden", 0.957, "🇸🇪", "Europe", "Pioneer in sustainability and innovation"),
            Country("Australia", 0.958, "🇦🇺", "Oceania", "Home to unique wildlife and the Great Barrier Reef"),
            Country("Netherlands", 0.955, "🇳🇱", "Europe", "Famous for tulips, windmills, and cycling"),
            Country("Hong Kong", 0.952, "🇭🇰", "Asia", "One of the world's major financial centers"),
            Country("Belgium", 0.951, "🇧🇪", "Europe", "Famous for waffles, chocolate, and beer"),
            Country("Ireland", 0.949, "🇮🇪", "Europe", "Known as the Emerald Isle"),
            Country("Finland", 0.948, "🇫🇮", "Europe", "Land of a thousand lakes and saunas"),
            Country("Singapore", 0.946, "🇸🇬", "Asia", "The Lion City and a global trading hub"),
            Country("United Kingdom", 0.946, "🇬🇧", "Europe", "Birthplace of Shakespeare and The Beatles"),
            Country("United Arab Emirates", 0.940, "🇦🇪", "Asia", "Desert nation with futuristic cities"),
            Country("Canada", 0.939, "🇨🇦", "North America", "Known for maple syrup and politeness"),
            Country("Liechtenstein", 0.938, "🇱🇮", "Europe", "Tiny alpine principality"),
            Country("New Zealand", 0.938, "🇳🇿", "Oceania", "Middle-earth in the Lord of the Rings films"),
            Country("United States", 0.937, "🇺🇸", "North America", "Land of the free, home of the brave"),
            Country("South Korea", 0.937, "🇰🇷", "Asia", "K-pop, kimchi, and technological innovation"),
            Country("Slovenia", 0.931, "🇸🇮", "Europe", "Green heart of Europe"),
            Country("Austria", 0.930, "🇦🇹", "Europe", "Mozart's birthplace and Sound of Music setting"),
            Country("Japan", 0.925, "🇯🇵", "Asia", "Land of the rising sun and cutting-edge technology"),
            Country("Malta", 0.924, "🇲🇹", "Europe", "Mediterranean island nation"),
            Country("Luxembourg", 0.922, "🇱🇺", "Europe", "One of the world's wealthiest nations"),
            Country("France", 0.920, "🇫🇷", "Europe", "City of lights and culinary excellence"),
            Country("Israel", 0.919, "🇮🇱", "Asia", "Startup nation with ancient history"),
            Country("Spain", 0.918, "🇪🇸", "Europe", "Famous for flamenco, paella, and siestas"),
            Country("Czechia", 0.915, "🇨🇿", "Europe", "Home to beautiful Prague and excellent beer"),
            Country("Italy", 0.914, "🇮🇹", "Europe", "Boot-shaped peninsula famous for pasta and art"),
            Country("San Marino", 0.913, "🇸🇲", "Europe", "One of the world's smallest countries"),
            Country("Andorra", 0.913, "🇦🇩", "Europe", "Small mountain nation in the Pyrenees"),
            Country("Cyprus", 0.912, "🇨🇾", "Europe", "Island nation in the Mediterranean"),
            Country("Greece", 0.908, "🇬🇷", "Europe", "Cradle of democracy and philosophy"),
            Country("Poland", 0.906, "🇵🇱", "Europe", "Pierogi, Pope John Paul II, and Solidarity"),
            Country("Estonia", 0.905, "🇪🇪", "Europe", "Digital pioneer and Baltic gem"),
            Country("Saudi Arabia", 0.900, "🇸🇦", "Asia", "Birthplace of Islam and oil-rich kingdom"),
            Country("Bahrain", 0.899, "🇧🇭", "Asia", "Pearl of the Gulf"),
            Country("Lithuania", 0.895, "🇱🇹", "Europe", "Baltic state with medieval charm"),
            Country("Portugal", 0.890, "🇵🇹", "Europe", "Home to Port wine and Fado music"),
            Country("Croatia", 0.889, "🇭🇷", "Europe", "Beautiful coastline along the Adriatic Sea"),
            Country("Latvia", 0.887, "🇱🇻", "Europe", "Baltic nation known for its forests"),
            Country("Qatar", 0.886, "🇶🇦", "Asia", "Wealthy Gulf state hosting FIFA World Cup 2022"),
            Country("Slovakia", 0.880, "🇸🇰", "Europe", "Heart of Europe with beautiful castles"),
            Country("Chile", 0.878, "🇨🇱", "South America", "Long, narrow country famous for wine"),
            Country("Hungary", 0.870, "🇭🇺", "Europe", "Thermal baths and paprika"),
            Country("Argentina", 0.865, "🇦🇷", "South America", "Tango, beef, and wine country"),
            Country("Montenegro", 0.862, "🇲🇪", "Europe", "Black Mountain with stunning landscapes"),
            Country("Uruguay", 0.858, "🇺🇾", "South America", "Progressive policies and beautiful beaches"),
            Country("Oman", 0.858, "🇴🇲", "Asia", "Sultanate on the Arabian Peninsula"),
            Country("Turkey", 0.853, "🇹🇷", "Europe/Asia", "Bridge between Europe and Asia"),
            Country("Kuwait", 0.852, "🇰🇼", "Asia", "Oil-rich nation in the Gulf"),
            Country("Antigua and Barbuda", 0.851, "🇦🇬", "Caribbean", "Twin-island nation in the Caribbean"),
            Country("Seychelles", 0.848, "🇸🇨", "Africa", "Pristine island paradise in the Indian Ocean"),
            Country("Bulgaria", 0.845, "🇧🇬", "Europe", "Land of roses and ancient Thracian culture"),
            Country("Romania", 0.842, "🇷🇴", "Europe", "Home to Dracula's castle"),
            Country("Georgia", 0.844, "🇬🇪", "Asia", "Cradle of wine and stunning mountain landscapes"),
            Country("Saint Kitts and Nevis", 0.840, "🇰🇳", "Caribbean", "Twin-island federation"),
            Country("Panama", 0.839, "🇵🇦", "Central America", "Famous for its canal connecting two oceans"),
            Country("Brunei", 0.837, "🇧🇳", "Asia", "Wealthy sultanate in Southeast Asia"),
            Country("Kazakhstan", 0.836, "🇰🇿", "Asia", "Largest landlocked country in the world"),
            Country("Costa Rica", 0.833, "🇨🇷", "Central America", "Pura Vida! No army since 1949"),
            Country("Serbia", 0.833, "🇷🇸", "Europe", "Crossroads of Central and Southeast Europe"),
            Country("Russia", 0.832, "🇷🇺", "Europe/Asia", "Largest country in the world"),
            Country("Belarus", 0.824, "🇧🇾", "Europe", "Last dictatorship in Europe"),
            Country("Bahamas", 0.820, "🇧🇸", "Caribbean", "Island nation famous for its clear waters"),
            Country("Malaysia", 0.819, "🇲🇾", "Asia", "Truly Asia - multicultural paradise"),
            Country("North Macedonia", 0.815, "🇲🇰", "Europe", "Land of ancient civilizations"),
            Country("Barbados", 0.811, "🇧🇧", "Caribbean", "Birthplace of rum"),
            Country("Armenia", 0.810, "🇦🇲", "Asia", "First nation to adopt Christianity"),
            Country("Albania", 0.810, "🇦🇱", "Europe", "Land of the eagles"),

            // High Human Development (0.700-0.799)
            Country("Trinidad and Tobago", 0.807, "🇹🇹", "Caribbean", "Twin-island republic in the Caribbean"),
            Country("Mauritius", 0.806, "🇲🇺", "Africa", "Diverse island nation in the Indian Ocean"),
            Country("Bosnia and Herzegovina", 0.804, "🇧🇦", "Europe", "Heart-shaped country in the Balkans"),
            Country("Iran", 0.799, "🇮🇷", "Asia", "Ancient Persia with rich cultural heritage"),
            Country("Saint Vincent and the Grenadines", 0.798, "🇻🇨", "Caribbean", "Multi-island nation"),
            Country("Thailand", 0.797, "🇹🇭", "Asia", "Land of smiles and amazing street food"),
            Country("China", 0.797, "🇨🇳", "Asia", "Most populous country and ancient civilization"),
            Country("Peru", 0.794, "🇵🇪", "South America", "Home to Machu Picchu and the Incas"),
            Country("Grenada", 0.791, "🇬🇩", "Caribbean", "Spice island of the Caribbean"),
            Country("Azerbaijan", 0.789, "🇦🇿", "Asia", "Land of fire with oil reserves"),
            Country("Mexico", 0.788, "🇲🇽", "North America", "Tacos, tequila, and ancient pyramids"),
            Country("Colombia", 0.788, "🇨🇴", "South America", "Land of magical realism"),
            Country("Brazil", 0.786, "🇧🇷", "South America", "Samba, soccer, and the Amazon rainforest"),
            Country("Palau", 0.785, "🇵🇼", "Oceania", "Island nation with pristine marine life"),
            Country("Moldova", 0.785, "🇲🇩", "Europe", "Wine country in Eastern Europe"),
            Country("Ukraine", 0.779, "🇺🇦", "Europe", "Breadbasket of Europe"),
            Country("Ecuador", 0.777, "🇪🇨", "South America", "Country crossed by the equator"),
            Country("Dominican Republic", 0.776, "🇩🇴", "Caribbean", "Island nation sharing Hispaniola"),
            Country("Guyana", 0.775, "🇬🇾", "South America", "Only English-speaking South American nation"),
            Country("Sri Lanka", 0.774, "🇱🇰", "Asia", "Pearl of the Indian Ocean"),
            Country("Tonga", 0.769, "🇹🇴", "Oceania", "Kingdom in the Pacific"),
            Country("Maldives", 0.766, "🇲🇻", "Asia", "Low-lying island paradise"),
            Country("Vietnam", 0.765, "🇻🇳", "Asia", "Pho, coffee culture, and resilient spirit"),
            Country("Turkmenistan", 0.764, "🇹🇲", "Asia", "Gas-rich nation in Central Asia"),
            Country("Algeria", 0.763, "🇩🇿", "Africa", "Largest country in Africa"),
            Country("Cuba", 0.762, "🇨🇺", "Caribbean", "Island nation known for cigars and classic cars"),
            Country("Dominica", 0.761, "🇩🇲", "Caribbean", "Nature island of the Caribbean"),
            Country("Paraguay", 0.756, "🇵🇾", "South America", "Landlocked heart of South America"),
            Country("Egypt", 0.754, "🇪🇬", "Africa", "Land of pharaohs and ancient pyramids"),
            Country("Jordan", 0.752, "🇯🇴", "Asia", "Desert kingdom with ancient Petra"),
            Country("Lebanon", 0.752, "🇱🇧", "Asia", "Crossroads of civilizations"),
            Country("Saint Lucia", 0.748, "🇱🇨", "Caribbean", "Helen of the West Indies"),
            Country("Mongolia", 0.747, "🇲🇳", "Asia", "Land of nomads and vast steppes"),
            Country("Tunisia", 0.746, "🇹🇳", "Africa", "Birthplace of the Arab Spring"),
            Country("South Africa", 0.741, "🇿🇦", "Africa", "Rainbow nation with 11 official languages"),
            Country("Uzbekistan", 0.740, "🇺🇿", "Asia", "Heart of the Silk Road"),
            Country("Bolivia", 0.733, "🇧🇴", "South America", "Land of many climates and cultures"),
            Country("Gabon", 0.732, "🇬🇦", "Africa", "Oil-rich equatorial nation"),
            Country("Marshall Islands", 0.730, "🇲🇭", "Oceania", "Low-lying Pacific island nation"),
            Country("Botswana", 0.731, "🇧🇼", "Africa", "Diamond-rich democracy in southern Africa"),
            Country("Fiji", 0.730, "🇫🇯", "Oceania", "Island paradise in the Pacific"),
            Country("Indonesia", 0.728, "🇮🇩", "Asia", "World's largest archipelago"),
            Country("Suriname", 0.722, "🇸🇷", "South America", "Diverse nation on South America's coast"),
            Country("Belize", 0.721, "🇧🇿", "Central America", "English-speaking nation in Central America"),
            Country("Libya", 0.718, "🇱🇾", "Africa", "Oil-rich North African nation"),
            Country("Jamaica", 0.720, "🇯🇲", "Caribbean", "Island home of reggae music"),
            Country("Kyrgyzstan", 0.718, "🇰🇬", "Asia", "Mountainous nation in Central Asia"),
            Country("Philippines", 0.719, "🇵🇭", "Asia", "Archipelago of over 7,000 islands"),
            Country("Morocco", 0.710, "🇲🇦", "Africa", "Gateway between Africa and Europe"),
            Country("Venezuela", 0.709, "🇻🇪", "South America", "Oil-rich nation facing economic challenges"),
            Country("Samoa", 0.708, "🇼🇸", "Oceania", "Polynesian island nation"),
            Country("Nicaragua", 0.706, "🇳🇮", "Central America", "Land of lakes and volcanoes"),
            Country("Nauru", 0.703, "🇳🇷", "Oceania", "World's smallest island nation"),

            // Medium Human Development (0.550-0.699)
            Country("Bhutan", 0.698, "🇧🇹", "Asia", "Last Himalayan kingdom measuring Gross National Happiness"),
            Country("Eswatini", 0.695, "🇸🇿", "Africa", "Small landlocked kingdom in southern Africa"),
            Country("Iraq", 0.692, "🇮🇶", "Asia", "Cradle of civilization between two rivers"),
            Country("Tajikistan", 0.691, "🇹🇯", "Asia", "Mountainous nation in Central Asia"),
            Country("Tuvalu", 0.689, "🇹🇻", "Oceania", "Tiny Pacific island nation"),
            Country("Bangladesh", 0.685, "🇧🇩", "Asia", "Land of rivers and resilient people"),
            Country("India", 0.684, "🇮🇳", "Asia", "Incredible diversity and Bollywood"),
            Country("El Salvador", 0.678, "🇸🇻", "Central America", "Smallest Central American nation"),
            Country("Equatorial Guinea", 0.674, "🇬🇶", "Africa", "Oil-rich nation in Central Africa"),
            Country("Palestine", 0.670, "🇵🇸", "Asia", "Occupied territories seeking statehood"),
            Country("Cape Verde", 0.668, "🇨🇻", "Africa", "Island nation off West Africa's coast"),
            Country("Namibia", 0.665, "🇳🇦", "Africa", "Desert nation in southern Africa"),
            Country("Guatemala", 0.662, "🇬🇹", "Central America", "Heart of the Mayan world"),
            Country("Congo", 0.649, "🇨🇬", "Africa", "Central African nation with oil reserves"),
            Country("Honduras", 0.645, "🇭🇳", "Central America", "Mountainous Central American nation"),
            Country("Kiribati", 0.644, "🇰🇮", "Oceania", "Pacific island nation threatened by sea level rise"),
            Country("São Tomé and Príncipe", 0.637, "🇸🇹", "Africa", "Twin-island nation off Africa's coast"),
            Country("Timor-Leste", 0.634, "🇹🇱", "Asia", "Youngest nation in Asia"),
            Country("Ghana", 0.628, "🇬🇭", "Africa", "Gateway to West Africa"),
            Country("Kenya", 0.627, "🇰🇪", "Africa", "Safari destination and marathon runners"),
            Country("Nepal", 0.622, "🇳🇵", "Asia", "Home to Mount Everest"),
            Country("Vanuatu", 0.621, "🇻🇺", "Oceania", "Y-shaped chain of islands in the Pacific"),
            Country("Laos", 0.617, "🇱🇦", "Asia", "Landlocked nation in Southeast Asia"),
            Country("Angola", 0.616, "🇦🇴", "Africa", "Oil-rich nation in southern Africa"),
            Country("Micronesia", 0.615, "🇫🇲", "Oceania", "Island nation in the Pacific"),
            Country("Myanmar", 0.609, "🇲🇲", "Asia", "Golden Land with ancient temples"),
            Country("Cambodia", 0.606, "🇰🇭", "Asia", "Home to Angkor Wat"),
            Country("Comoros", 0.603, "🇰🇲", "Africa", "Island nation in the Indian Ocean"),
            Country("Zimbabwe", 0.598, "🇿🇼", "Africa", "Southern African nation with Victoria Falls"),
            Country("Zambia", 0.595, "🇿🇲", "Africa", "Copper-rich landlocked nation"),
            Country("Cameroon", 0.588, "🇨🇲", "Africa", "Miniature Africa with diverse landscapes"),
            Country("Solomon Islands", 0.584, "🇸🇧", "Oceania", "Island chain in the Pacific"),
            Country("Ivory Coast", 0.582, "🇨🇮", "Africa", "World's largest cocoa producer"),
            Country("Uganda", 0.581, "🇺🇬", "Africa", "Pearl of Africa"),
            Country("Rwanda", 0.578, "🇷🇼", "Africa", "Land of a thousand hills"),
            Country("Papua New Guinea", 0.576, "🇵🇬", "Oceania", "Island nation with incredible biodiversity"),
            Country("Togo", 0.571, "🇹🇬", "Africa", "Narrow West African nation"),
            Country("Syria", 0.564, "🇸🇾", "Asia", "Ancient civilization facing modern challenges"),
            Country("Mauritania", 0.563, "🇲🇷", "Africa", "Bridge between North and sub-Saharan Africa"),
            Country("Nigeria", 0.560, "🇳🇬", "Africa", "Most populous African nation"),
            Country("Tanzania", 0.555, "🇹🇿", "Africa", "Home to Kilimanjaro and Serengeti"),

            // Low Human Development (below 0.550)
            Country("Haiti", 0.554, "🇭🇹", "Caribbean", "First independent black republic"),
            Country("Lesotho", 0.550, "🇱🇸", "Africa", "Mountain kingdom entirely surrounded by South Africa"),
            Country("Pakistan", 0.544, "🇵🇰", "Asia", "Cricket-loving nation with rich history"),
            Country("Senegal", 0.530, "🇸🇳", "Africa", "Westernmost point of Africa"),
            Country("Gambia", 0.524, "🇬🇲", "Africa", "Smallest country in mainland Africa"),
            Country("DR Congo", 0.522, "🇨🇩", "Africa", "Heart of Africa with vast mineral wealth"),
            Country("Malawi", 0.517, "🇲🇼", "Africa", "Warm heart of Africa"),
            Country("Benin", 0.515, "🇧🇯", "Africa", "Cradle of voodoo"),
            Country("Guinea-Bissau", 0.514, "🇬🇼", "Africa", "Small West African nation"),
            Country("Djibouti", 0.513, "🇩🇯", "Africa", "Strategic location at the Horn of Africa"),
            Country("Sudan", 0.511, "🇸🇩", "Africa", "Ancient Nubian civilization"),
            Country("Liberia", 0.510, "🇱🇷", "Africa", "First African republic"),
            Country("Eritrea", 0.503, "🇪🇷", "Africa", "Red Sea nation with ancient history"),
            Country("Guinea", 0.500, "🇬🇳", "Africa", "Bauxite-rich West African nation"),
            Country("Ethiopia", 0.497, "🇪🇹", "Africa", "Birthplace of coffee and Lucy fossil"),
            Country("Afghanistan", 0.496, "🇦🇫", "Asia", "Crossroads of civilizations"),
            Country("Mozambique", 0.493, "🇲🇿", "Africa", "Coastal nation in southeastern Africa"),
            Country("Madagascar", 0.487, "🇲🇬", "Africa", "Island continent with unique wildlife"),
            Country("Yemen", 0.470, "🇾🇪", "Asia", "Ancient Arabian civilization"),
            Country("Sierra Leone", 0.467, "🇸🇱", "Africa", "West African nation rich in diamonds"),
            Country("Burkina Faso", 0.459, "🇧🇫", "Africa", "Land of upright people"),
            Country("Burundi", 0.439, "🇧🇮", "Africa", "Small landlocked nation in East Africa"),
            Country("Mali", 0.419, "🇲🇱", "Africa", "Ancient empire of Timbuktu"),
            Country("Niger", 0.418, "🇳🇪", "Africa", "Landlocked Sahel nation"),
            Country("Chad", 0.416, "🇹🇩", "Africa", "Landlocked nation in the heart of Africa"),
            Country("Central African Republic", 0.414, "🇨🇫", "Africa", "Landlocked nation in central Africa"),
            Country("Somalia", 0.404, "🇸🇴", "Africa", "Horn of Africa nation"),
            Country("South Sudan", 0.388, "🇸🇸", "Africa", "World's newest country")
        )

        val elements = listOf(
            Element("Hydrogen", "H", 1, "Non-metal", "Most abundant element in the universe"),
            Element("Helium", "He", 2, "Noble Gas", "Makes your voice sound funny when inhaled"),
            Element("Lithium", "Li", 3, "Alkali Metal", "Used in batteries and mood stabilizers"),
            Element("Beryllium", "Be", 4, "Alkaline Earth", "Used in aerospace and nuclear applications"),
            Element("Boron", "B", 5, "Metalloid", "Essential for plant growth and glass production"),
            Element("Carbon", "C", 6, "Non-metal", "The basis of all organic life"),
            Element("Nitrogen", "N", 7, "Non-metal", "Makes up 78% of Earth's atmosphere"),
            Element("Oxygen", "O", 8, "Non-metal", "Essential for breathing and combustion"),
            Element("Fluorine", "F", 9, "Halogen", "Most reactive chemical element"),
            Element("Neon", "Ne", 10, "Noble Gas", "Glows orange-red in electric discharge"),
            Element("Sodium", "Na", 11, "Alkali Metal", "Essential for nerve function"),
            Element("Magnesium", "Mg", 12, "Alkaline Earth", "Burns with brilliant white light"),
            Element("Aluminium", "Al", 13, "Post-transition Metal", "Most abundant metal in Earth's crust"),
            Element("Silicon", "Si", 14, "Metalloid", "Used in computer chips and glass"),
            Element("Phosphorus", "P", 15, "Non-metal", "Essential for DNA and energy storage"),
            Element("Sulfur", "S", 16, "Non-metal", "Known for its distinctive smell"),
            Element("Chlorine", "Cl", 17, "Halogen", "Used to disinfect swimming pools"),
            Element("Argon", "Ar", 18, "Noble Gas", "Used in welding and light bulbs"),
            Element("Potassium", "K", 19, "Alkali Metal", "Essential for muscle and nerve function"),
            Element("Calcium", "Ca", 20, "Alkaline Earth", "Builds strong bones and teeth"),
            Element("Scandium", "Sc", 21, "Transition Metal", "Used in aerospace alloys"),
            Element("Titanium", "Ti", 22, "Transition Metal", "Strong, lightweight, and biocompatible"),
            Element("Vanadium", "V", 23, "Transition Metal", "Strengthens steel alloys"),
            Element("Chromium", "Cr", 24, "Transition Metal", "Makes stainless steel stainless"),
            Element("Manganese", "Mn", 25, "Transition Metal", "Essential for steel production"),
            Element("Iron", "Fe", 26, "Transition Metal", "Essential for blood and very magnetic"),
            Element("Cobalt", "Co", 27, "Transition Metal", "Used in blue pigments and alloys"),
            Element("Nickel", "Ni", 28, "Transition Metal", "Magnetic metal used in coins"),
            Element("Copper", "Cu", 29, "Transition Metal", "Excellent conductor of electricity"),
            Element("Zinc", "Zn", 30, "Transition Metal", "Important for immune system"),
            Element("Gallium", "Ga", 31, "Post-transition Metal", "Melts in your hand"),
            Element("Germanium", "Ge", 32, "Metalloid", "Used in semiconductors"),
            Element("Arsenic", "As", 33, "Metalloid", "Historically used as poison"),
            Element("Selenium", "Se", 34, "Non-metal", "Important antioxidant"),
            Element("Bromine", "Br", 35, "Halogen", "Only liquid non-metal at room temperature"),
            Element("Krypton", "Kr", 36, "Noble Gas", "Used in high-performance light bulbs"),
            Element("Rubidium", "Rb", 37, "Alkali Metal", "Used in atomic clocks"),
            Element("Strontium", "Sr", 38, "Alkaline Earth", "Used in fireworks for red color"),
            Element("Yttrium", "Y", 39, "Transition Metal", "Used in lasers and superconductors"),
            Element("Zirconium", "Zr", 40, "Transition Metal", "Used in nuclear reactors"),
            Element("Niobium", "Nb", 41, "Transition Metal", "Used in superconducting magnets"),
            Element("Molybdenum", "Mo", 42, "Transition Metal", "Strengthens steel at high temperatures"),
            Element("Technetium", "Tc", 43, "Transition Metal", "First artificially produced element"),
            Element("Ruthenium", "Ru", 44, "Transition Metal", "Used in electrical contacts"),
            Element("Rhodium", "Rh", 45, "Transition Metal", "Most expensive precious metal"),
            Element("Palladium", "Pd", 46, "Transition Metal", "Used in catalytic converters"),
            Element("Silver", "Ag", 47, "Transition Metal", "Best electrical conductor"),
            Element("Cadmium", "Cd", 48, "Transition Metal", "Toxic metal used in batteries"),
            Element("Indium", "In", 49, "Post-transition Metal", "Used in touchscreens"),
            Element("Tin", "Sn", 50, "Post-transition Metal", "Used for coating steel cans"),
            Element("Antimony", "Sb", 51, "Metalloid", "Used in flame retardants"),
            Element("Tellurium", "Te", 52, "Metalloid", "Used in solar panels"),
            Element("Iodine", "I", 53, "Halogen", "Essential for thyroid function"),
            Element("Xenon", "Xe", 54, "Noble Gas", "Used in car headlights"),
            Element("Cesium", "Cs", 55, "Alkali Metal", "Used in atomic clocks"),
            Element("Barium", "Ba", 56, "Alkaline Earth", "Used in medical imaging"),
            Element("Lanthanum", "La", 57, "Lanthanide", "Used in camera lenses"),
            Element("Cerium", "Ce", 58, "Lanthanide", "Most abundant rare earth element"),
            Element("Praseodymium", "Pr", 59, "Lanthanide", "Used in aircraft engines"),
            Element("Neodymium", "Nd", 60, "Lanthanide", "Used in powerful magnets"),
            Element("Promethium", "Pm", 61, "Lanthanide", "Radioactive rare earth element"),
            Element("Samarium", "Sm", 62, "Lanthanide", "Used in powerful permanent magnets"),
            Element("Europium", "Eu", 63, "Lanthanide", "Used in euro banknote security"),
            Element("Gadolinium", "Gd", 64, "Lanthanide", "Used in MRI contrast agents"),
            Element("Terbium", "Tb", 65, "Lanthanide", "Used in green phosphors"),
            Element("Dysprosium", "Dy", 66, "Lanthanide", "Used in hard disk drives"),
            Element("Holmium", "Ho", 67, "Lanthanide", "Has the highest magnetic strength"),
            Element("Erbium", "Er", 68, "Lanthanide", "Used in fiber optic amplifiers"),
            Element("Thulium", "Tm", 69, "Lanthanide", "Rarest stable rare earth element"),
            Element("Ytterbium", "Yb", 70, "Lanthanide", "Used in atomic clocks"),
            Element("Lutetium", "Lu", 71, "Lanthanide", "Hardest and densest rare earth element"),
            Element("Hafnium", "Hf", 72, "Transition Metal", "Used in nuclear control rods"),
            Element("Tantalum", "Ta", 73, "Transition Metal", "Used in electronic capacitors"),
            Element("Tungsten", "W", 74, "Transition Metal", "Highest melting point of all elements"),
            Element("Rhenium", "Re", 75, "Transition Metal", "One of the rarest elements"),
            Element("Osmium", "Os", 76, "Transition Metal", "Densest naturally occurring element"),
            Element("Iridium", "Ir", 77, "Transition Metal", "Second densest element"),
            Element("Platinum", "Pt", 78, "Transition Metal", "Precious metal used in catalysts"),
            Element("Gold", "Au", 79, "Transition Metal", "Precious metal that never tarnishes"),
            Element("Mercury", "Hg", 80, "Transition Metal", "Only metal that's liquid at room temperature"),
            Element("Thallium", "Tl", 81, "Post-transition Metal", "Highly toxic heavy metal"),
            Element("Lead", "Pb", 82, "Post-transition Metal", "Dense metal once used in paint and pipes"),
            Element("Bismuth", "Bi", 83, "Post-transition Metal", "Creates beautiful iridescent crystals"),
            Element("Polonium", "Po", 84, "Metalloid", "Highly radioactive and toxic"),
            Element("Astatine", "At", 85, "Halogen", "Rarest naturally occurring element"),
            Element("Radon", "Rn", 86, "Noble Gas", "Radioactive gas found in basements"),
            Element("Francium", "Fr", 87, "Alkali Metal", "Most unstable of the first 101 elements"),
            Element("Radium", "Ra", 88, "Alkaline Earth", "Discovered by Marie Curie, highly radioactive"),
            Element("Actinium", "Ac", 89, "Actinide", "Glows blue in the dark"),
            Element("Thorium", "Th", 90, "Actinide", "Potential nuclear fuel alternative"),
            Element("Protactinium", "Pa", 91, "Actinide", "Extremely rare radioactive metal"),
            Element("Uranium", "U", 92, "Actinide", "Used in nuclear power and weapons"),
            Element("Neptunium", "Np", 93, "Actinide", "First synthetic transuranium element"),
            Element("Plutonium", "Pu", 94, "Actinide", "Man-made element used in nuclear weapons"),
            Element("Americium", "Am", 95, "Actinide", "Used in smoke detectors"),
            Element("Curium", "Cm", 96, "Actinide", "Named after Marie and Pierre Curie"),
            Element("Berkelium", "Bk", 97, "Actinide", "Named after Berkeley, California"),
            Element("Californium", "Cf", 98, "Actinide", "Used in neutron sources"),
            Element("Einsteinium", "Es", 99, "Actinide", "Named after Albert Einstein"),
            Element("Fermium", "Fm", 100, "Actinide", "Named after Enrico Fermi"),
            Element("Mendelevium", "Md", 101, "Actinide", "Named after Dmitri Mendeleev"),
            Element("Nobelium", "No", 102, "Actinide", "Named after Alfred Nobel"),
            Element("Lawrencium", "Lr", 103, "Actinide", "Named after Ernest Lawrence"),
            Element("Rutherfordium", "Rf", 104, "Transactinide", "Named after Ernest Rutherford"),
            Element("Dubnium", "Db", 105, "Transactinide", "Named after Dubna, Russia"),
            Element("Seaborgium", "Sg", 106, "Transactinide", "Named after Glenn T. Seaborg"),
            Element("Bohrium", "Bh", 107, "Transactinide", "Named after Niels Bohr"),
            Element("Hassium", "Hs", 108, "Transactinide", "Named after Hesse, Germany"),
            Element("Meitnerium", "Mt", 109, "Transactinide", "Named after Lise Meitner"),
            Element("Darmstadtium", "Ds", 110, "Transactinide", "Named after Darmstadt, Germany"),
            Element("Roentgenium", "Rg", 111, "Transactinide", "Named after Wilhelm Röntgen"),
            Element("Copernicium", "Cn", 112, "Transactinide", "Named after Nicolaus Copernicus"),
            Element("Nihonium", "Nh", 113, "Transactinide", "Named after Japan (Nihon)"),
            Element("Flerovium", "Fl", 114, "Transactinide", "Named after Soviet physicist Georgy Flyorov"),
            Element("Moscovium", "Mc", 115, "Transactinide", "Named after Moscow, Russia"),
            Element("Livermorium", "Lv", 116, "Transactinide", "Named after Livermore, California"),
            Element("Tennessine", "Ts", 117, "Transactinide", "Named after Tennessee, USA"),
            Element("Oganesson", "Og", 118, "Transactinide", "Named after Yuri Oganessian")
        )

        fun getRandomCountries(count: Int, difficulty: Difficulty): List<Country> {
            return when (difficulty) {
                Difficulty.EASY -> countries.filter { it.hdi > 0.800 }.shuffled().take(count)
                Difficulty.NORMAL -> countries.shuffled().take(count)
                Difficulty.HARD -> countries.filter { it.hdi < 0.700 }.shuffled().take(count)
                Difficulty.EXTREME -> countries.filter { it.hdi < 0.600 }.shuffled().take(count)
            }
        }

        fun getRandomElements(count: Int, difficulty: Difficulty): List<Element> {
            return when (difficulty) {
                Difficulty.EASY -> elements.filter { it.atomicNumber <= 30 }.shuffled().take(count)
                Difficulty.NORMAL -> elements.filter { it.atomicNumber <= 86 }.shuffled().take(count)
                Difficulty.HARD -> elements.shuffled().take(count)
                Difficulty.EXTREME -> elements.filter { it.atomicNumber > 80 }.shuffled().take(count)
            }
        }
    }
}

// Animated Components
@Composable
fun AnimatedScoreCard(
    score: Int,
    maxScore: Int,
    modifier: Modifier = Modifier
) {
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(1000, easing = EaseOutBounce),
        label = "score"
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "$animatedScore",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                "/ $maxScore",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun PulsingIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Icon(
        icon,
        contentDescription = null,
        modifier = modifier.scale(scale),
        tint = tint
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = if (enabled) {
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.outline,
            MaterialTheme.colorScheme.outline
        )
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(colors),
                shape = RoundedCornerShape(20.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

// Main App
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NerdyGamesApp() {
    val navController = rememberNavController()
    var gameSession by remember { mutableStateOf(GameSession()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("setup/{gameType}") { backStackEntry ->
                val gameType = backStackEntry.arguments?.getString("gameType") ?: ""
                GameSetupScreen(
                    navController = navController,
                    gameType = gameType,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("game/hdi") {
                CompetitiveHDIGameScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("game/atomic") {
                AtomicNumberGameScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("results") {
                ResultsScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onNewGame = { gameSession = GameSession() }
                )
            }
            composable("achievements") {
                AchievementsScreen(
                    navController = navController,
                    players = gameSession.players
                )
            }
        }
    }
}

// Enhanced Home Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PulsingIcon(
                            Icons.Default.Psychology,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Nerdy Games",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("achievements") }) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = "Achievements")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* Quick match functionality */ },
                icon = { Icon(Icons.Default.Speed, contentDescription = null) },
                text = { Text("Quick Match!") },
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                // Hero Card with Gradient
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.tertiaryContainer
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🧠", style = MaterialTheme.typography.displayMedium)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("🌍", style = MaterialTheme.typography.displayMedium)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("⚗️", style = MaterialTheme.typography.displayMedium)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Welcome to Nerdy Games!",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Challenge your friends in competitive trivia battles! 🎯",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Choose Your Battle",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("🔥", style = MaterialTheme.typography.headlineMedium)
                }
            }

            item {
                EnhancedGameCard(
                    title = "HDI Battle Royale",
                    subtitle = "Competitive Multiplayer",
                    description = "All players guess simultaneously - closest wins! 🏆",
                    emoji = "🌍",
                    gradient = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    onClick = { navController.navigate("setup/hdi") }
                )
            }

            item {
                EnhancedGameCard(
                    title = "Atomic Showdown",
                    subtitle = "Element Mastery",
                    description = "Test your periodic table knowledge ⚗️",
                    emoji = "🧪",
                    gradient = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = { navController.navigate("setup/atomic") }
                )
            }

            item {
                // Stats Overview
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Your Stats",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatChip("🎮 0", "Games")
                            StatChip("🏆 0", "Wins")
                            StatChip("🔥 0", "Streak")
                            StatChip("⭐ 0", "Achievements")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedGameCard(
    title: String,
    subtitle: String,
    description: String,
    emoji: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardPress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        onClick = {
            isPressed = true
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(gradient)
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            emoji,
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                subtitle,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

@Composable
fun StatChip(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

// Enhanced Game Setup Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(
    navController: NavController,
    gameType: String,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    var newPlayerName by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.NORMAL) }
    var hasTimer by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(30) }

    val updatedSession = gameSession.copy(
        gameType = gameType,
        difficulty = selectedDifficulty,
        hasTimer = hasTimer,
        timerSeconds = timerSeconds
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Game Setup ${if (gameType == "hdi") "🌍" else "⚗️"}",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Game Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            if (gameType == "hdi") "HDI Battle Royale 🌍" else "Atomic Showdown ⚗️",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (gameType == "hdi")
                                "🎯 All players guess simultaneously\n🏆 Closest guess wins the point\n📊 Guess HDI values (0.000-1.000)"
                            else
                                "🧪 Identify chemical elements\n⚡ Exact atomic numbers required\n🔬 Test your chemistry knowledge",
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4
                        )
                    }
                }
            }

            // Difficulty Selection
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Choose Difficulty 🎚️",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(Difficulty.entries.toTypedArray()) { difficulty ->
                                DifficultyChip(
                                    difficulty = difficulty,
                                    isSelected = selectedDifficulty == difficulty,
                                    onClick = { selectedDifficulty = difficulty }
                                )
                            }
                        }
                    }
                }
            }

            // Game Options
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Game Options ⚙️",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable Timer ⏱️")
                            Switch(
                                checked = hasTimer,
                                onCheckedChange = { hasTimer = it }
                            )
                        }

                        if (hasTimer) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Timer: ${timerSeconds}s")
                            Slider(
                                value = timerSeconds.toFloat(),
                                onValueChange = { timerSeconds = it.toInt() },
                                valueRange = 10f..60f,
                                steps = 9
                            )
                        }
                    }
                }
            }

            // Players Section
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Add Players 👥",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newPlayerName,
                                onValueChange = { newPlayerName = it },
                                label = { Text("Player Name") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            FilledTonalButton(
                                onClick = {
                                    if (newPlayerName.isNotBlank()) {
                                        updatedSession.players.add(Player(name = newPlayerName.trim()))
                                        onGameSessionUpdate(updatedSession)
                                        newPlayerName = ""
                                    }
                                },
                                enabled = newPlayerName.isNotBlank()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }

                        if (updatedSession.players.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Players (${updatedSession.players.size}) 🎮",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            updatedSession.players.forEachIndexed { index, player ->
                                PlayerCard(
                                    player = player,
                                    position = index + 1,
                                    onRemove = {
                                        updatedSession.players.remove(player)
                                        onGameSessionUpdate(updatedSession)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Start Game Button
            item {
                GradientButton(
                    text = "Start Battle! 🚀",
                    onClick = {
                        onGameSessionUpdate(updatedSession)
                        navController.navigate("game/$gameType")
                    },
                    enabled = updatedSession.players.size >= 2,
                    modifier = Modifier.fillMaxWidth()
                )

                if (updatedSession.players.size < 2) {
                    Text(
                        "⚠️ Need at least 2 players for competitive mode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DifficultyChip(
    difficulty: Difficulty,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        label = {
            Text("${difficulty.emoji} ${difficulty.displayName}")
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun PlayerCard(
    player: Player,
    position: Int,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            ) {
                Text(
                    "$position",
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                player.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove player",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// Competitive HDI Game Screen (New competitive format)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitiveHDIGameScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {


    var currentCountry by remember { mutableStateOf(
        GameManager.getRandomCountries(1, gameSession.difficulty).first()
    ) }
    var playerGuesses by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var showResults by remember { mutableStateOf(false) }
    var questionResults by remember { mutableStateOf<List<PlayerGuess>>(emptyList()) }
    var questionsAnswered by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(gameSession.timerSeconds) }
    var isTimerRunning by remember { mutableStateOf(false) }


    fun submitAllGuesses() {
        val results = gameSession.players.map { player ->
            val guessText = playerGuesses[player.id] ?: ""
            val guess = guessText.toDoubleOrNull() ?: -1.0
            val difference = if (guess >= 0) abs(guess - currentCountry.hdi) else Double.MAX_VALUE

            PlayerGuess(player, guess, difference)
        }.sortedBy { it.difference }

        // Mark the closest guess as winner (if valid)
        if (results.isNotEmpty() && results.first().guess >= 0) {
            results.first().copy(isClosest = true).also { winner ->
                winner.player.score++
                winner.player.totalCorrect++
            }
        }

        questionResults = results
        showResults = true
        isTimerRunning = false
        onGameSessionUpdate(gameSession)
    }

    // Timer effect
    LaunchedEffect(isTimerRunning) {
        if (gameSession.hasTimer && isTimerRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
            if (timeLeft <= 0) {
                // Auto-submit when timer runs out
                submitAllGuesses()
            }
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HDI Battle Royale 🌍") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress and Timer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Question ${questionsAnswered + 1}/${gameSession.totalQuestions}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LinearProgressIndicator(
                        progress = { questionsAnswered.toFloat() / gameSession.totalQuestions },
                        modifier = Modifier.width(150.dp)
                    )
                }

                if (gameSession.hasTimer && !showResults) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (timeLeft <= 10)
                                MaterialTheme.colorScheme.errorContainer
                            else
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "${timeLeft}s",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Country Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            currentCountry.emoji,
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            currentCountry.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "What's the HDI? 📊",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "(Range: 0.000 - 1.000)",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        if (currentCountry.funFact.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                                )
                            ) {
                                Text(
                                    "💡 ${currentCountry.funFact}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            if (showResults) {
                // Results Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Round Results! 🎯",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Actual HDI: ${String.format("%.3f", currentCountry.hdi)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        questionResults.forEachIndexed { index, result ->
                            PlayerResultCard(
                                result = result,
                                rank = index + 1,
                                isWinner = result.isClosest
                            )
                        }
                    }
                }
            } else {
                // Player Input Section
                Text(
                    "All Players Submit Your Guesses! 🎮",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                gameSession.players.forEach { player ->
                    PlayerGuessCard(
                        player = player,
                        guess = playerGuesses[player.id] ?: "",
                        onGuessChange = { guess ->
                            playerGuesses[player.id] = guess
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                GradientButton(
                    text = "Submit All Guesses! 🚀",
                    onClick = { submitAllGuesses() },
                    enabled = playerGuesses.size == gameSession.players.size &&
                            playerGuesses.values.all { it.isNotBlank() },
                    modifier = Modifier.fillMaxWidth()
                )

                if (!isTimerRunning && gameSession.hasTimer) {
                    LaunchedEffect(Unit) {
                        timeLeft = gameSession.timerSeconds
                        isTimerRunning = true
                    }
                }
            }

            if (showResults) {
                GradientButton(
                    text = if (questionsAnswered >= gameSession.totalQuestions - 1)
                        "View Final Results! 🏆"
                    else
                        "Next Question! ⚡",
                    onClick = {
                        questionsAnswered++
                        if (questionsAnswered >= gameSession.totalQuestions) {
                            navController.navigate("results")
                        } else {
                            currentCountry = GameManager.getRandomCountries(1, gameSession.difficulty).first()
                            playerGuesses.clear()
                            showResults = false
                            timeLeft = gameSession.timerSeconds
                            questionResults = emptyList()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Live Scoreboard
            LiveScoreboard(players = gameSession.players)
        }
    }
}

@Composable
fun PlayerGuessCard(
    player: Player,
    guess: String,
    onGuessChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    player.name.first().uppercase(),
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    player.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = guess,
                    onValueChange = onGuessChange,
                    label = { Text("HDI Guess") },
                    placeholder = { Text("e.g., 0.850") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                    ),
                    singleLine = true
                )
            }
        }
    }
}

@Composable
fun PlayerResultCard(
    result: PlayerGuess,
    rank: Int,
    isWinner: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isWinner)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (isWinner) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(32.dp)
            ) {
                Text(
                    "$rank",
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    color = if (isWinner) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        result.player.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal
                    )
                    if (isWinner) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("🏆", style = MaterialTheme.typography.titleMedium)
                    }
                }
                Text(
                    "Guess: ${if (result.guess >= 0) String.format("%.3f", result.guess) else "Invalid"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    if (result.difference == Double.MAX_VALUE) "❌" else "±${String.format("%.3f", result.difference)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isWinner) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                )
                if (isWinner) {
                    Text(
                        "+1 pt",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun LiveScoreboard(players: List<Player>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Live Scoreboard 📊",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(players.sortedByDescending { it.score }) { index, player ->
                    ScoreChip(
                        player = player,
                        rank = index + 1,
                        isLeader = index == 0
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreChip(
    player: Player,
    rank: Int,
    isLeader: Boolean
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isLeader) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier.animateContentSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLeader) {
                Text("👑", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                "${player.name}: ${player.score}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isLeader) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

// Enhanced Atomic Game (keeping similar but simpler format)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtomicNumberGameScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    var currentElement by remember { mutableStateOf(
        GameManager.getRandomElements(1, gameSession.difficulty).first()
    ) }
    var currentPlayerIndex by remember { mutableStateOf(0) }
    var userGuess by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var lastGuessCorrect by remember { mutableStateOf(false) }
    var questionsAnswered by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atomic Showdown ⚗️") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress
            LinearProgressIndicator(
                progress = { questionsAnswered.toFloat() / gameSession.totalQuestions },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Question ${questionsAnswered + 1} of ${gameSession.totalQuestions}",
                style = MaterialTheme.typography.titleMedium
            )

            // Current Player
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(
                            gameSession.players[currentPlayerIndex].name.first().uppercase(),
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Current Player",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            gameSession.players[currentPlayerIndex].name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Element Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("⚛️", style = MaterialTheme.typography.displayLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            currentElement.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Symbol: ${currentElement.symbol}",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        if (currentElement.category != "Unknown") {
                            Text(
                                "Category: ${currentElement.category}",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "What's the atomic number? 🔢",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "(Range: 1 - 118)",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        if (currentElement.funFact.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                                )
                            ) {
                                Text(
                                    "💡 ${currentElement.funFact}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            if (showResult) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (lastGuessCorrect)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            if (lastGuessCorrect) "Correct! 🎉" else "Not quite! 😅",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Atomic number: ${currentElement.atomicNumber}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Your guess: $userGuess",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            if (!showResult) {
                OutlinedTextField(
                    value = userGuess,
                    onValueChange = { userGuess = it },
                    label = { Text("Atomic number (1-118)") },
                    leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )
                )

                GradientButton(
                    text = "Submit Guess! 🚀",
                    onClick = {
                        val guess = userGuess.toIntOrNull()
                        if (guess != null && guess >= 1 && guess <= 118) {
                            val correct = guess == currentElement.atomicNumber

                            if (correct) {
                                gameSession.players[currentPlayerIndex].score++
                            }

                            lastGuessCorrect = correct
                            showResult = true
                            onGameSessionUpdate(gameSession)
                        }
                    },
                    enabled = userGuess.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                GradientButton(
                    text = if (questionsAnswered >= gameSession.totalQuestions - 1)
                        "View Results! 🏆"
                    else
                        "Next Question! ⚡",
                    onClick = {
                        questionsAnswered++
                        if (questionsAnswered >= gameSession.totalQuestions) {
                            navController.navigate("results")
                        } else {
                            currentPlayerIndex = (currentPlayerIndex + 1) % gameSession.players.size
                            currentElement = GameManager.getRandomElements(1, gameSession.difficulty).first()
                            userGuess = ""
                            showResult = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Score Display
            LiveScoreboard(players = gameSession.players)
        }
    }
}

// Enhanced Results Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavController,
    gameSession: GameSession,
    onNewGame: () -> Unit
) {
    val sortedPlayers = gameSession.players.sortedByDescending { it.score }
    var showConfetti by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        showConfetti = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Battle Results! 🏆") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                // Winner Celebration
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.tertiaryContainer,
                                        MaterialTheme.colorScheme.secondaryContainer
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (showConfetti) {
                                Text("🎉🏆🎉", style = MaterialTheme.typography.displayLarge)
                            } else {
                                Text("🏆", style = MaterialTheme.typography.displayLarge)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "${sortedPlayers.firstOrNull()?.name ?: "Nobody"} Wins!",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "🎮 ${gameSession.totalQuestions} questions • ${gameSession.difficulty.emoji} ${gameSession.difficulty.displayName}",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "Final Rankings 📊",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(sortedPlayers) { player ->
                val position = sortedPlayers.indexOf(player) + 1
                FinalResultCard(
                    player = player,
                    position = position,
                    totalQuestions = gameSession.totalQuestions,
                    isWinner = position == 1
                )
            }

            item {
                // Game Stats
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Game Statistics 📈",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val avgScore = sortedPlayers.map { it.score }.average()
                        val perfectScores = sortedPlayers.count { it.score == gameSession.totalQuestions }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatChip("${String.format("%.1f", avgScore)}", "Avg Score")
                            StatChip("$perfectScores", "Perfect Games")
                            StatChip("${gameSession.difficulty.emoji}", "Difficulty")
                            StatChip(
                                if (gameSession.hasTimer) "${gameSession.timerSeconds}s" else "∞",
                                "Timer"
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onNewGame()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New Game")
                    }

                    GradientButton(
                        text = "Home 🏠",
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun FinalResultCard(
    player: Player,
    position: Int,
    totalQuestions: Int,
    isWinner: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isWinner) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "winnerScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = when (position) {
                1 -> MaterialTheme.colorScheme.tertiaryContainer
                2 -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                3 -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isWinner) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = when (position) {
                        1 -> Color(0xFFFFD700) // Gold
                        2 -> Color(0xFFC0C0C0) // Silver
                        3 -> Color(0xFFCD7F32) // Bronze
                        else -> MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text(
                        when (position) {
                            1 -> "👑"
                            2 -> "🥈"
                            3 -> "🥉"
                            else -> "$position"
                        },
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        player.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        "${String.format("%.1f", (player.score.toDouble() / totalQuestions) * 100)}% accuracy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${player.score}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "/ $totalQuestions",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// Achievements Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    navController: NavController,
    players: List<Player>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Achievements 🏆") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏆", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Achievements",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Unlock achievements by playing games!",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            items(GameManager.achievements) { achievement ->
                AchievementCard(
                    achievement = achievement,
                    isUnlocked = false // Implement achievement logic
                )
            }
        }
    }
}

@Composable
fun AchievementCard(
    achievement: Achievement,
    isUnlocked: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                achievement.icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = if (isUnlocked)
                    MaterialTheme.colorScheme.onTertiaryContainer
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    achievement.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked)
                        MaterialTheme.colorScheme.onTertiaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUnlocked)
                        MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            if (isUnlocked) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Unlocked",
                        modifier = Modifier
                            .size(16.dp)
                            .wrapContentSize(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}