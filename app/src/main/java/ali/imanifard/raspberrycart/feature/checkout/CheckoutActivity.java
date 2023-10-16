package ali.imanifard.raspberrycart.feature.checkout;

import static org.koin.java.KoinJavaComponent.inject;

import android.content.Intent;
import android.os.Bundle;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartActivity;
import ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys;
import ali.imanifard.raspberrycart.databinding.ActivityCheckoutBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;
import ali.imanifard.raspberrycart.feature.shipping.ShippingActivity;
import kotlin.Lazy;

public class CheckoutActivity extends RaspberryCartActivity {

    private ActivityCheckoutBinding binding;
    private Lazy<CheckOutViewModel> viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = inject(CheckoutActivity.class);
/*
        viewModel.checkOutLiveData.observe(this,checkOut -> {
            //binding.tvOrderPrice.setText(checkOut.getPayablePrice());
            binding.tvOrderStatus.setText(checkOut.getPaymentStatus());
            @StringRes int strPurchaseState;
            if (checkOut.isPurchaseSuccess())
                strPurchaseState = R.string.successful_purchase;
            else
                strPurchaseState = R.string.unsuccessful_purchase;
            binding.tvPurchaseState.setText(getResources().getString(strPurchaseState));
        });

 */

        double totalPrice = getIntent().getDoubleExtra(RaspberryCartExtraKeys.EXTRA_KEY_DATA, 0.0);
        binding.tvOrderPrice.setText(String.valueOf(totalPrice));

        binding.tvOrderStatus.setText(getResources().getString(R.string.preparing));
        binding.tvPurchaseState.setText(getResources().getString(R.string.successful_purchase));


        binding.btnBack.setOnClickListener(v -> {
            startActivity(new Intent(CheckoutActivity.this, ShippingActivity.class));
            finish();
        });


        binding.btnReturnToHome.setOnClickListener(v -> {
            startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
            finish();
        });

        binding.btnOrderHistory.setOnClickListener(v -> {

        });


    }
}