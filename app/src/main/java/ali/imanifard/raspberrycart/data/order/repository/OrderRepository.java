package ali.imanifard.raspberrycart.data.order.repository;

import ali.imanifard.raspberrycart.feature.checkout.CheckOut;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface OrderRepository {

    Completable submit(String firstName, String lastName, String postalCode, String phoneNumber, String address, String paymentMethod);

    Single<CheckOut> checkOut(int orderId);

}
