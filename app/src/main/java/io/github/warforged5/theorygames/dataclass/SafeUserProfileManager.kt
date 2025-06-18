package io.github.warforged5.theorygames.dataclass

class SafeUserProfileManager(private val manager: UserProfileManager) {

    fun safeGetCurrentProfile(): UserProfile? {
        return try {
            manager.getCurrentProfile()?.let { ProfileDataValidator.validateProfile(it) }
        } catch (e: Exception) {
            android.util.Log.e("SafeUserProfileManager", "Error getting current profile", e)
            null
        }
    }

    fun safeSaveCurrentProfile(profile: UserProfile) {
        try {
            val validatedProfile = ProfileDataValidator.validateProfile(profile)
            manager.saveCurrentProfile(validatedProfile)
        } catch (e: Exception) {
            android.util.Log.e("SafeUserProfileManager", "Error saving profile", e)
        }
    }

    fun safeGetAllProfiles(): List<UserProfile> {
        return try {
            manager.getAllProfiles().map { ProfileDataValidator.validateProfile(it) }
        } catch (e: Exception) {
            android.util.Log.e("SafeUserProfileManager", "Error getting all profiles", e)
            emptyList()
        }
    }
}