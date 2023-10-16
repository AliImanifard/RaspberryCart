package ali.imanifard.raspberrycart.feature.main.auth;

import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.data.user.repository.UserRepository;
import io.reactivex.rxjava3.core.Completable;

public class AuthViewModel extends RaspberryCartViewModel {

    private UserRepository userRepository;

    public AuthViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    Completable login(String username, String password) {
        loadingLottie.setValue(true);
        return userRepository.login(username, password).doFinally(() -> loadingLottie.postValue(false));
    }

    Completable signUp(String email, String username, String password) {
        loadingLottie.setValue(true);
        return userRepository.signUp(email, username, password).doFinally(() -> loadingLottie.postValue(false));
    }


}
