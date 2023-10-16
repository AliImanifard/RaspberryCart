package ali.imanifard.raspberrycart.data.user.repository.source;

import ali.imanifard.raspberrycart.data.user.SignUpResponse;
import ali.imanifard.raspberrycart.data.user.TokenResponse;
import io.reactivex.rxjava3.core.Single;

public interface UserDataSource {

    Single<TokenResponse> login(String username, String password);

    Single<SignUpResponse> signUp(String email, String username, String password);

    // When the user logs in, the token amount is stored somewhere.
    // The reason for doing this is that we can access it where we need it
    // (i.e. in the request header).
    void loadToken();

    void saveToken(String token);

    void saveUserName(String username);

    String getUserName();

    void signOut();

}
