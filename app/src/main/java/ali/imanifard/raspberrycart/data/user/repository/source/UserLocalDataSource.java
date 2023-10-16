package ali.imanifard.raspberrycart.data.user.repository.source;

import android.content.SharedPreferences;

import ali.imanifard.raspberrycart.data.user.SignUpResponse;
import ali.imanifard.raspberrycart.data.user.TokenContainer_Singleton;
import ali.imanifard.raspberrycart.data.user.TokenResponse;
import io.reactivex.rxjava3.core.Single;

public class UserLocalDataSource implements UserDataSource{

    private final SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCES_TOKEN_KEY = "token";
    private static final String SHARED_PREFERENCES_FULL_NAME = "username";

    public UserLocalDataSource(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Single<TokenResponse> login(String username, String password) {
        // implemented by UserRemoteDataSource
        return null;
    }

    @Override
    public Single<SignUpResponse> signUp(String email, String username, String password) {
        // implemented by UserRemoteDataSource
        return null;
    }

    @Override
    public void loadToken() {
        TokenContainer_Singleton tokenContainer = TokenContainer_Singleton.getInstance();
        tokenContainer.update(sharedPreferences.getString(SHARED_PREFERENCES_TOKEN_KEY,null));
    }

    @Override
    public void saveToken(String token) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_TOKEN_KEY,token).apply();
    }

    @Override
    public void saveUserName(String username) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_FULL_NAME,username).apply();
    }

    @Override
    public String getUserName() {
        return sharedPreferences.getString(SHARED_PREFERENCES_FULL_NAME,"");
    }

    @Override
    public void signOut() {
        sharedPreferences.edit().clear().apply();
    }


}
