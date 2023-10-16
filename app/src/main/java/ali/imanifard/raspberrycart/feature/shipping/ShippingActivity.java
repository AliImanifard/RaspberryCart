package ali.imanifard.raspberrycart.feature.shipping;

import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartActivity;
import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.databinding.ActivityShippingBinding;
import ali.imanifard.raspberrycart.feature.checkout.CheckoutActivity;
import ali.imanifard.raspberrycart.feature.main.cart.CartItemAdapter;
import ali.imanifard.raspberrycart.feature.main.cart.PurchaseDetailTotalPrice;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ShippingActivity extends RaspberryCartActivity implements View.OnClickListener {

    private ActivityShippingBinding binding;
    private ShippingViewModel shippingViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private String strEtFirstName, strEtLastName, strEtPostalCode, strEtPhoneNumber, strEtAddress, strPaymentMethod;
    private PurchaseDetailTotalPrice purchaseDetailTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShippingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        shippingViewModel = get(ShippingViewModel.class, named("myShippingViewModel"));


        //List<CartEntity> purchaseDetail = getIntent().getParcelableExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA);
        //List<CartEntity> purchaseDetail = getIntent().getParcelableArrayListExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA);
        purchaseDetailTotalPrice = getIntent().getParcelableExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA);

        CartItemAdapter.PurchaseDetailViewHolder viewHolder = new CartItemAdapter.PurchaseDetailViewHolder(binding.purchaseDetailView.getRoot());
        //double totalPrice = 0.0;
        //for (int i = 0; i < purchaseDetail.size(); i++)
        //    totalPrice += purchaseDetail.get(i).getProductDTO().getPrice() * purchaseDetail.get(i).getQuantity();

        viewHolder.bind(purchaseDetailTotalPrice.getTotalPrice());

        binding.btnCashOnDelivery.setOnClickListener(this::onClick);
        binding.btnOnlinePayment.setOnClickListener(this::onClick);

        binding.btnBack.setOnClickListener(v -> finish());


    }

    @Override
    public void onClick(View v) {

        strEtFirstName = binding.etFirstName.getEditText().getText().toString().trim();
        strEtLastName = binding.etLastName.getEditText().getText().toString().trim();
        strEtPostalCode = binding.etPostalCode.getEditText().getText().toString().trim();
        strEtPhoneNumber = binding.etPhoneNumber.getEditText().getText().toString().trim();
        strEtAddress = binding.etAddress.getEditText().getText().toString().trim();


        if (v.getId() == R.id.btn_cash_on_delivery)
            strPaymentMethod = shippingViewModel.PAYMENT_METHOD_COD;
        else if (v.getId() == R.id.btn_online_payment)
            strPaymentMethod = shippingViewModel.PAYMENT_METHOD_ONLINE;
        shippingViewModel.submitOrder(strEtFirstName, strEtLastName, strEtPostalCode,
                        strEtPhoneNumber, strEtAddress, strPaymentMethod)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(ShippingActivity.this, CheckoutActivity.class);
                        intent.putExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, purchaseDetailTotalPrice.getTotalPrice());
                        startActivity(intent);
                    }
                });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}