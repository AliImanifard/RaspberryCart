package ali.imanifard.raspberrycart.feature.checkout;

import androidx.lifecycle.MutableLiveData;

import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.common.RaspberrySingleObserver;
import ali.imanifard.raspberrycart.data.order.repository.OrderRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CheckOutViewModel extends RaspberryCartViewModel {

    private final int orderId;
    public MutableLiveData<CheckOut> checkOutLiveData = new MutableLiveData<>();
    private OrderRepository orderRepository;

    public CheckOutViewModel(int orderId, OrderRepository orderRepository) {
        this.orderId = orderId;
        this.orderRepository = orderRepository;

        orderRepository.checkOut(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberrySingleObserver<CheckOut>(compositeDisposable) {
                    @Override
                    public void onSuccess(@NonNull CheckOut checkOut) {
                        checkOutLiveData.postValue(checkOut);
                    }
                });
    }
}
