package ali.imanifard.raspberrycart.feature.main.profile;

import android.content.SharedPreferences;
import android.net.Uri;

import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.data.user.TokenContainer_Singleton;
import ali.imanifard.raspberrycart.data.user.repository.UserRepository;

public class ProfileViewModel extends RaspberryCartViewModel {

    private static final String SHARED_PREFERENCES_IMAGE_PROFILE = "image_profile";
    private final SharedPreferences sharedPreferences;
    private UserRepository userRepository;


    public ProfileViewModel(UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }


    public String getUserName() {
        return userRepository.getUserName();
    }

    public boolean isSignedIn() {
        return TokenContainer_Singleton.getInstance().getToken() != null;
    }

    public void signOut() {
        userRepository.SignOut();
    }

    public void saveImageProfileUri(Uri imageUri) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_IMAGE_PROFILE, imageUri.toString()).apply();
    }

    public Uri getProfileImage() {
        String strProfileImageUri = sharedPreferences.getString(SHARED_PREFERENCES_IMAGE_PROFILE, null);
        if (strProfileImageUri == null)
            return null;
        else
            return Uri.parse(strProfileImageUri);
    }
}
