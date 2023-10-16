package ali.imanifard.raspberrycart.data.order.repository;

import ali.imanifard.raspberrycart.data.order.repository.source.OrderLocalDataSource;
import ali.imanifard.raspberrycart.data.order.repository.source.OrderRemoteDataSource;
import ali.imanifard.raspberrycart.feature.checkout.CheckOut;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class OrderRepositoryImpl implements OrderRepository {

    private OrderRemoteDataSource remoteDataSource;
    private OrderLocalDataSource localDataSource;

    public OrderRepositoryImpl(OrderRemoteDataSource remoteDataSource, OrderLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Completable submit(String firstName, String lastName, String postalCode, String phoneNumber, String address, String paymentMethod) {
        return localDataSource.submit(firstName, lastName, postalCode, phoneNumber, address, paymentMethod);
    }

    @Override
    public Single<CheckOut> checkOut(int orderId) {
        return localDataSource.checkOut(orderId);
    }
}
