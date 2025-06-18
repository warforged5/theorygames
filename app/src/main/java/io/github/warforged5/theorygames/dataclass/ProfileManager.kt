package io.github.warforged5.theorygames.dataclass

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profiles")
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileManager(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    companion object {
        private val PROFILES_KEY = stringPreferencesKey("saved_profiles")
    }

    val profilesFlow: Flow<ProfileCollection> = context.profileDataStore.data
        .map { preferences ->
            val profilesJson = preferences[PROFILES_KEY] ?: ""
            if (profilesJson.isEmpty()) {
                ProfileCollection()
            } else {
                try {
                    json.decodeFromString<ProfileCollection>(profilesJson)
                } catch (e: Exception) {
                    ProfileCollection()
                }
            }
        }

    suspend fun saveProfile(profile: SavedProfile) {
        context.profileDataStore.edit { preferences ->
            val currentProfiles = try {
                val currentJson = preferences[PROFILES_KEY] ?: ""
                if (currentJson.isEmpty()) {
                    ProfileCollection()
                } else {
                    json.decodeFromString<ProfileCollection>(currentJson)
                }
            } catch (e: Exception) {
                ProfileCollection()
            }

            val updatedProfiles = currentProfiles.profiles
                .filter { it.id != profile.id } + profile

            val newCollection = currentProfiles.copy(profiles = updatedProfiles)
            preferences[PROFILES_KEY] = json.encodeToString(newCollection)
        }
    }

    suspend fun deleteProfile(profileId: String) {
        context.profileDataStore.edit { preferences ->
            val currentProfiles = try {
                val currentJson = preferences[PROFILES_KEY] ?: ""
                if (currentJson.isEmpty()) return@edit
                json.decodeFromString<ProfileCollection>(currentJson)
            } catch (e: Exception) {
                return@edit
            }

            val updatedProfiles = currentProfiles.profiles.filter { it.id != profileId }
            val newCollection = currentProfiles.copy(
                profiles = updatedProfiles,
                lastSelectedProfiles = currentProfiles.lastSelectedProfiles.filter { it != profileId }
            )
            preferences[PROFILES_KEY] = json.encodeToString(newCollection)
        }
    }

    suspend fun updateLastSelectedProfiles(profileIds: List<String>) {
        context.profileDataStore.edit { preferences ->
            val currentProfiles = try {
                val currentJson = preferences[PROFILES_KEY] ?: ""
                if (currentJson.isEmpty()) {
                    ProfileCollection()
                } else {
                    json.decodeFromString<ProfileCollection>(currentJson)
                }
            } catch (e: Exception) {
                ProfileCollection()
            }

            val newCollection = currentProfiles.copy(lastSelectedProfiles = profileIds)
            preferences[PROFILES_KEY] = json.encodeToString(newCollection)
        }
    }

    suspend fun updateProfileStats(players: List<Player>) {
        context.profileDataStore.edit { preferences ->
            val currentProfiles = try {
                val currentJson = preferences[PROFILES_KEY] ?: ""
                if (currentJson.isEmpty()) return@edit
                json.decodeFromString<ProfileCollection>(currentJson)
            } catch (e: Exception) {
                return@edit
            }

            val updatedProfiles = currentProfiles.profiles.map { savedProfile ->
                val matchingPlayer = players.find { it.id == savedProfile.id }
                if (matchingPlayer != null) {
                    savedProfile.updateStats(matchingPlayer)
                } else {
                    savedProfile
                }
            }

            val newCollection = currentProfiles.copy(profiles = updatedProfiles)
            preferences[PROFILES_KEY] = json.encodeToString(newCollection)
        }
    }
}

class SettingsManager(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    companion object {
        private val SETTINGS_KEY = stringPreferencesKey("app_settings")
    }

    val settingsFlow: Flow<AppSettings> = context.settingsDataStore.data
        .map { preferences ->
            val settingsJson = preferences[SETTINGS_KEY] ?: ""
            if (settingsJson.isEmpty()) {
                AppSettings()
            } else {
                try {
                    json.decodeFromString<AppSettings>(settingsJson)
                } catch (e: Exception) {
                    AppSettings()
                }
            }
        }

    suspend fun updateSettings(settings: AppSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences[SETTINGS_KEY] = json.encodeToString(settings)
        }
    }

    suspend fun updateTheme(theme: AppTheme) {
        context.settingsDataStore.edit { preferences ->
            val currentSettings = try {
                val currentJson = preferences[SETTINGS_KEY] ?: ""
                if (currentJson.isEmpty()) {
                    AppSettings()
                } else {
                    json.decodeFromString<AppSettings>(currentJson)
                }
            } catch (e: Exception) {
                AppSettings()
            }

            val newSettings = currentSettings.copy(selectedTheme = theme)
            preferences[SETTINGS_KEY] = json.encodeToString(newSettings)
        }
    }

    suspend fun updateDarkMode(isDark: Boolean?) {
        context.settingsDataStore.edit { preferences ->
            val currentSettings = try {
                val currentJson = preferences[SETTINGS_KEY] ?: ""
                if (currentJson.isEmpty()) {
                    AppSettings()
                } else {
                    json.decodeFromString<AppSettings>(currentJson)
                }
            } catch (e: Exception) {
                AppSettings()
            }

            val newSettings = currentSettings.copy(isDarkMode = isDark)
            preferences[SETTINGS_KEY] = json.encodeToString(newSettings)
        }
    }
}