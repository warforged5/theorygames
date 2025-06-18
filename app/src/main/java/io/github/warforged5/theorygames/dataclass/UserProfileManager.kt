package io.github.warforged5.theorygames.dataclass

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class UserProfileManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theory_games_prefs", Context.MODE_PRIVATE)

    // Configure JSON with more lenient settings
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
    }

    companion object {
        private const val TAG = "UserProfileManager"
        private const val KEY_CURRENT_PROFILE = "current_profile"
        private const val KEY_PROFILES = "all_profiles"
        private const val KEY_SETTINGS = "app_settings"
        private const val KEY_GAME_HISTORY = "game_history"
    }

    fun getCurrentProfile(): UserProfile? {
        return try {
            val profileJson = prefs.getString(KEY_CURRENT_PROFILE, null)
            profileJson?.let {
                json.decodeFromString<UserProfile>(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading current profile", e)
            // Clear corrupted data
            prefs.edit().remove(KEY_CURRENT_PROFILE).apply()
            null
        }
    }

    fun saveCurrentProfile(profile: UserProfile) {
        try {
            val profileJson = json.encodeToString(profile)
            prefs.edit().putString(KEY_CURRENT_PROFILE, profileJson).apply()

            // Also save to all profiles list
            val allProfiles = getAllProfiles().toMutableList()
            val existingIndex = allProfiles.indexOfFirst { it.id == profile.id }
            if (existingIndex >= 0) {
                allProfiles[existingIndex] = profile
            } else {
                allProfiles.add(profile)
            }
            saveAllProfiles(allProfiles)

            Log.d(TAG, "Profile saved successfully: ${profile.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving profile", e)
        }
    }

    fun getAllProfiles(): List<UserProfile> {
        return try {
            val profilesJson = prefs.getString(KEY_PROFILES, null)
            profilesJson?.let {
                json.decodeFromString<List<UserProfile>>(it)
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading all profiles", e)
            // Clear corrupted data
            prefs.edit().remove(KEY_PROFILES).apply()
            emptyList()
        }
    }

    private fun saveAllProfiles(profiles: List<UserProfile>) {
        try {
            val profilesJson = json.encodeToString(profiles)
            prefs.edit().putString(KEY_PROFILES, profilesJson).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving all profiles", e)
        }
    }

    fun getSettings(): AppSettings {
        return try {
            val settingsJson = prefs.getString(KEY_SETTINGS, null)
            settingsJson?.let {
                json.decodeFromString<AppSettings>(it)
            } ?: AppSettings()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading settings", e)
            AppSettings()
        }
    }

    fun saveSettings(settings: AppSettings) {
        try {
            val settingsJson = json.encodeToString(settings)
            prefs.edit().putString(KEY_SETTINGS, settingsJson).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving settings", e)
        }
    }

    fun addGameToHistory(entry: GameHistoryEntry) {
        try {
            val history = getGameHistory().toMutableList()
            history.add(0, entry) // Add to beginning
            // Keep only last 100 games
            if (history.size > 100) {
                history.removeAt(history.size - 1)
            }
            saveGameHistory(history)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding game to history", e)
        }
    }

    fun getGameHistory(): List<GameHistoryEntry> {
        return try {
            val historyJson = prefs.getString(KEY_GAME_HISTORY, null)
            historyJson?.let {
                json.decodeFromString<List<GameHistoryEntry>>(it)
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading game history", e)
            // Clear corrupted data
            prefs.edit().remove(KEY_GAME_HISTORY).apply()
            emptyList()
        }
    }

    private fun saveGameHistory(history: List<GameHistoryEntry>) {
        try {
            val historyJson = json.encodeToString(history)
            prefs.edit().putString(KEY_GAME_HISTORY, historyJson).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving game history", e)
        }
    }

    fun createNewProfile(name: String, avatar: PlayerAvatar): UserProfile {
        return UserProfile(
            id = "profile_${System.currentTimeMillis()}",
            name = name,
            preferredAvatar = avatar
        )
    }

    fun deleteProfile(profileId: String) {
        try {
            val allProfiles = getAllProfiles().filter { it.id != profileId }
            saveAllProfiles(allProfiles)

            // If deleting current profile, clear it
            val currentProfile = getCurrentProfile()
            if (currentProfile?.id == profileId) {
                prefs.edit().remove(KEY_CURRENT_PROFILE).apply()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting profile", e)
        }
    }

    // Debug function to clear all data if needed
    fun clearAllData() {
        try {
            prefs.edit().clear().apply()
            Log.d(TAG, "All data cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing data", e)
        }
    }
}
