package ali.imanifard.raspberrycart.feature.main.auth;

import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;

import android.os.Bundle;

import androidx.fragment.app.FragmentContainerView;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartActivity;
import ali.imanifard.raspberrycart.databinding.ActivityAuthBinding;

public class AuthActivity extends RaspberryCartActivity {

    private ActivityAuthBinding binding;
    private AuthViewModel authViewModel;

    public AuthActivity(AuthViewModel authViewModel) {
        this.authViewModel = authViewModel;
    }

    public AuthActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = get(AuthViewModel.class, named("myAuthViewModel"));


        FragmentContainerView fcw = binding.fragmentContainerViewAuth;
        fcw.removeAllViewsInLayout();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view_auth, new LogInFragment())
                .commit();

    }
}