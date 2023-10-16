package ali.imanifard.raspberrycart.feature.main.auth;

import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartCompletableObserver;
import ali.imanifard.raspberrycart.common.RaspberryCartException;
import ali.imanifard.raspberrycart.common.RaspberryCartFragment;
import ali.imanifard.raspberrycart.databinding.FragmentSignUpBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpFragment extends RaspberryCartFragment {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected AuthActivity authActivity;
    protected Context context;
    FragmentSignUpBinding binding;
    String strEtEmail, strEtUserId, strEtPassword, strEtPasswordRepeat;
    private AuthViewModel authViewModel;


    public SignUpFragment() {
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
        binding = FragmentSignUpBinding.inflate(inflater, container, false);


        binding.btnGetStarted.setOnClickListener(v -> {

            strEtEmail = binding.etEmail.getEditText().getText().toString().trim();
            strEtUserId = binding.etUserId.getEditText().getText().toString().trim();
            strEtPassword = binding.etPassword.getEditText().getText().toString().trim();
            strEtPasswordRepeat = binding.etPasswordRepeat.getEditText().getText().toString().trim();


            if (isValidEmail() && isValidPassword() && isValidUserId())
                authViewModel.signUp(strEtEmail, strEtUserId, strEtPassword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> showError(new RaspberryCartException(RaspberryCartException.TYPE.AUTH, 0, throwable.getMessage())))
                        .subscribe(new RaspberryCartCompletableObserver(compositeDisposable) {
                            @Override
                            public void onComplete() {
                                authActivity.finish();
                            }
                        });
            else if (isValidEmail() && !isValidPassword() && isValidUserId())
                showSnackbar(getResources().getString(R.string.password_mismatch_and_repetition));
            else if (!isValidEmail() && isValidPassword() && isValidUserId())
                showSnackbar(getResources().getString(R.string.email_is_not_valid));
            else if (isValidEmail() && isValidPassword() && !isValidUserId())
                showSnackbar(getResources().getString(R.string.user_id_is_not_valid));
            else
                showSnackbar(getResources().getString(R.string.please_fill_in_all_required_information_correctly));
        });


        binding.btnLogIn.setOnClickListener(v -> authActivity
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view_auth, new LogInFragment()).commit());

        // continue with google sign up
        //binding.btnContinueWithGoogle.setOnClickListener(v -> );

        // continue with facebook sign up
        //binding.btnContinueWithFacebook.setOnClickListener(v -> );

        binding.btnClose.setOnClickListener(v -> startActivity(new Intent(authActivity, MainActivity.class)));


        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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

    private boolean isValidPassword() {
        return strEtPassword.equals(strEtPasswordRepeat) && !TextUtils.isEmpty(strEtPassword) && !TextUtils.isEmpty(strEtPasswordRepeat);
    }

    private boolean isValidEmail() {
        return (!TextUtils.isEmpty(strEtEmail) && Patterns.EMAIL_ADDRESS.matcher(strEtEmail).matches());
    }

    private boolean isValidUserId() {
        // checking it's not an empty edit text.
        return !TextUtils.isEmpty(strEtUserId);
    }
}