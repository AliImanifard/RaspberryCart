package ali.imanifard.raspberrycart.feature.main.auth;

import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.common.RaspberryCartFragment;
import ali.imanifard.raspberrycart.databinding.FragmentLogInBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LogInFragment extends RaspberryCartFragment {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected AuthActivity authActivity;
    protected Context context;
    private FragmentLogInBinding binding;
    private AuthViewModel authViewModel;
    private String strEtUserId, strEtPassword;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = get(AuthViewModel.class, named("myAuthViewModel"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.btnLogIn.setOnClickListener(v -> {

            strEtUserId = binding.etUserId.getEditText().getText().toString().trim();
            strEtPassword = binding.etPassword.getEditText().getText().toString().trim();

            authViewModel.login(
                            strEtUserId,
                            strEtPassword
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                        @Override
                        public void onComplete() {

                            authActivity.finish();

                        }
                    });
        });


        binding.btnSignUp.setOnClickListener(v -> authActivity
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view_auth, new SignUpFragment()).commit());

        // continue with google log in
        //binding.btnContinueWithGoogle.setOnClickListener(v -> );

        // continue with facebook log in
        //binding.btnContinueWithFacebook.setOnClickListener(v -> );

        binding.btnClose.setOnClickListener(v -> startActivity(new Intent(authActivity, MainActivity.class)));

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof AuthActivity)
            this.authActivity = (AuthActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}