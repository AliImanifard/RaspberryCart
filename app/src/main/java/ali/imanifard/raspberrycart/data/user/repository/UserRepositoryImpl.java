package ali.imanifard.raspberrycart.data.user.repository;

import ali.imanifard.raspberrycart.data.user.TokenContainer_Singleton;
import ali.imanifard.raspberrycart.data.user.TokenResponse;
import ali.imanifard.raspberrycart.data.user.repository.source.UserLocalDataSource;
import ali.imanifard.raspberrycart.data.user.repository.source.UserRemoteDataSource;
import io.reactivex.rxjava3.core.Completable;

public class UserRepositoryImpl implements UserRepository {

    private final UserRemoteDataSource remoteDataSource;
    private final UserLocalDataSource localDataSource;

    public UserRepositoryImpl(UserRemoteDataSource remoteDataSource,
                              UserLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Completable login(String username, String password) {
        return remoteDataSource.login(username, password)
                .doOnSuccess(this::onSuccessLogin)
                .doFinally(() -> saveUserName(username))
                .ignoreElement();
    }

    @Override
    public Completable signUp(String email, String username, String password) {
        // In Fake Store API, there is no user that the user registers, so we have to call the user that is in Fake Store API users section (according to Fake Store API documentation).
        return remoteDataSource.signUp(email,username,password)
                .flatMapCompletable(signUpResponse -> remoteDataSource.login("mor_2314","83r5^_")
                        .doOnSuccess(this::onSuccessLogin)
                        .doFinally(() -> localDataSource.saveUserName(username))
                        .ignoreElement()
                );
    }

    @Override
    public void loadToken() {
        localDataSource.loadToken();
    }

    @Override
    public void saveUserName(String username) {
        localDataSource.saveUserName(username);
    }

    @Override
    public String getUserName() {
        return localDataSource.getUserName();
    }

    @Override
    public void SignOut() {
        localDataSource.signOut();
        TokenContainer_Singleton.getInstance().update(null);
    }

    private void onSuccessLogin(TokenResponse tokenResponse){
        TokenContainer_Singleton tokenContainer = TokenContainer_Singleton.getInstance();
        tokenContainer.update(tokenResponse.getToken());

        localDataSource.saveToken(tokenResponse.getToken());
    }

}
