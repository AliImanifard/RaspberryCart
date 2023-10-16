package ali.imanifard.raspberrycart.feature.main;

import static org.koin.java.KoinJavaComponent.inject;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartActivity;
import ali.imanifard.raspberrycart.data.db.CartEntity;
import ali.imanifard.raspberrycart.databinding.ActivityMainBinding;
import ali.imanifard.raspberrycart.feature.main.cart.CartFragment;
import ali.imanifard.raspberrycart.feature.main.heart.HeartFragment;
import ali.imanifard.raspberrycart.feature.main.home.HomeFragment;
import ali.imanifard.raspberrycart.feature.main.profile.ProfileFragment;
import kotlin.Lazy;

public class MainActivity extends RaspberryCartActivity {

    public BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;
    private Lazy<MainViewModel> viewModelLazy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModelLazy = inject(MainViewModel.class);

        bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationViewOnItemSelectedListener(bottomNavigationView);

        bottomNavigationView.setItemHorizontalTranslationEnabled(false);


    }

    private void bottomNavigationViewOnItemSelectedListener(BottomNavigationView bottomNavigationView) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view_main, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home)
                selectedFragment = new HomeFragment();
            else if (itemId == R.id.nav_heart)
                selectedFragment = new HeartFragment();
            else if (itemId == R.id.nav_profile)
                selectedFragment = new ProfileFragment();
            else if (itemId == R.id.nav_cart)
                selectedFragment = new CartFragment();


            if (selectedFragment != null)
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_view_main, selectedFragment).commit();

            return true;

        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onItemCountChangeEvent(List<CartEntity> cartEntities) {

        BadgeDrawable badge = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_cart);
        badge.setBadgeGravity(BadgeDrawable.BOTTOM_START);
        badge.setBackgroundColor(getColor(R.color.raspberry_secondary_tint_1));

        int quantities = 0;
        for (int i = 0; i < cartEntities.size(); i++) {
            quantities = cartEntities.get(i).getQuantity();
        }

        badge.setNumber(quantities);
        badge.setVerticalOffset((int) (10f * (getResources().getDisplayMetrics().densityDpi / (float) DisplayMetrics.DENSITY_DEFAULT)));
        badge.setVisible(quantities > 0);

    }


    @Override
    protected void onResume() {
        super.onResume();

        viewModelLazy.getValue().getCartItemsCount();
    }
}