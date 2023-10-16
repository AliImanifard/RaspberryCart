package ali.imanifard.raspberrycart.data.user.repository;

import io.reactivex.rxjava3.core.Completable;

public interface UserRepository {

    Completable login(String username, String password);

    Completable signUp(String email, String username, String password);

    // When the user logs in, the token amount is stored somewhere.
    // The reason for doing this is that we can access it where we need it
    // (i.e. in the request header).
    void loadToken();

    void saveUserName(String username);


    String getUserName();

    void SignOut();
}
