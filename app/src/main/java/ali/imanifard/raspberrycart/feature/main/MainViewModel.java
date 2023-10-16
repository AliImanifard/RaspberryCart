package ali.imanifard.raspberrycart.feature.main;

import org.greenrobot.eventbus.EventBus;

import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.common.RaspberrySingleObserver;
import ali.imanifard.raspberrycart.data.cart.repository.CartRepository;
import ali.imanifard.raspberrycart.data.user.TokenContainer_Singleton;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends RaspberryCartViewModel {

    private CartRepository cartRepository;

    public MainViewModel(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    public void getCartItemsCount() {
        if (TokenContainer_Singleton.getInstance().getToken() != null &&
                !TokenContainer_Singleton.getInstance().getToken().isEmpty()) {


            cartRepository.getCartItemCount()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new RaspberrySingleObserver<Integer>(compositeDisposable) {
                        @Override
                        public void onSuccess(@NonNull Integer integer) {
                            EventBus.getDefault().postSticky(integer);
                        }
                    });


        }
    }
}
