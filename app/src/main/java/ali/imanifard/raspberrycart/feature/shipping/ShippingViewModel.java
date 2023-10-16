package ali.imanifard.raspberrycart.feature.shipping;

import ali.imanifard.raspberrycart.common.RaspberryCartViewModel;
import ali.imanifard.raspberrycart.data.order.repository.OrderRepository;
import io.reactivex.rxjava3.core.Completable;

public class ShippingViewModel extends RaspberryCartViewModel {

    public final String PAYMENT_METHOD_COD = "cash_on_delivery";
    public final String PAYMENT_METHOD_ONLINE = "online";
    private OrderRepository orderRepository;


    public ShippingViewModel(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    Completable submitOrder(String firstName, String lastName, String postalCode, String phoneNumber, String address, String paymentMethod) {
        return orderRepository.submit(firstName, lastName, postalCode, phoneNumber, address, paymentMethod);
    }


}
