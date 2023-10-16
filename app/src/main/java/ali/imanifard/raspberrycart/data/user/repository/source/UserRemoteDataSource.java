package ali.imanifard.raspberrycart.data.user.repository.source;

import com.google.gson.JsonObject;

import ali.imanifard.raspberrycart.data.user.SignUpResponse;
import ali.imanifard.raspberrycart.data.user.TokenResponse;
import ali.imanifard.raspberrycart.services.retrofit.ApiService;
import io.reactivex.rxjava3.core.Single;

public class UserRemoteDataSource implements UserDataSource {

    private final ApiService apiService;

    public UserRemoteDataSource(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<TokenResponse> login(String username, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        return apiService.login(jsonObject);
    }

    @Override
    public Single<SignUpResponse> signUp(String email, String username, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        return apiService.signUp(jsonObject);
    }

    @Override
    public void loadToken() {
        // implemented by UserLocalDataSource
    }

    @Override
    public void saveToken(String token) {
        // implemented by UserLocalDataSource
    }

    @Override
    public void saveUserName(String username) {
        // implemented by UserLocalDataSource
    }

    @Override
    public String getUserName() {
        // implemented by UserLocalDataSource
        return null;
    }

    @Override
    public void signOut() {
        // implemented by UserLocalDataSource
    }
}
