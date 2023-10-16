package ali.imanifard.raspberrycart.data.order.repository.source;

import ali.imanifard.raspberrycart.feature.checkout.CheckOut;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class OrderRemoteDataSource implements OrderDataSource {

    @Override
    public Completable submit(String firstName, String lastName, String postalCode, String phoneNumber, String address, String paymentMethod) {
        return null;
    }

    @Override
    public Single<CheckOut> checkOut(int orderId) {
        return null;
    }
}
