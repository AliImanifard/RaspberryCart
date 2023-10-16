package ali.imanifard.raspberrycart.feature.main.profile;

import static android.app.Activity.RESULT_OK;
import static org.koin.java.KoinJavaComponent.inject;
import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.aliEmailAddress;
import static ali.imanifard.raspberrycart.common.RaspberryCartExtraKeys.aliGitHubUrl;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartFragment;
import ali.imanifard.raspberrycart.databinding.FragmentProfileBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;
import ali.imanifard.raspberrycart.feature.main.auth.AuthActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Lazy;


public class ProfileFragment extends RaspberryCartFragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    protected MainActivity mainActivity;
    protected Context context;
    private FragmentProfileBinding binding;
    private Lazy<ProfileViewModel> profileViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final ActivityResultLauncher<Intent> intentActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    // do operation from here....
                    if (data != null && data.getData() != null) {
                        data.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        data.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        Uri selectedImageUri = data.getData();
                        profileViewModel.getValue().saveImageProfileUri(selectedImageUri);
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        binding.ivProfile.setImageBitmap(selectedImageBitmap);
                    }
                }
            });


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = inject(ProfileViewModel.class);
        //profileViewModel = get(ProfileViewModel.class, named("myProfileViewModel"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bitmap selectedImageBitmap = null;
        Uri imageUri = profileViewModel.getValue().getProfileImage();

        if (imageUri != null) {
            try {
                if (Build.VERSION.SDK_INT < 28){
                    requestPermissions();
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), imageUri);
                    binding.ivProfile.setImageBitmap(selectedImageBitmap);

                }
                else {
                    ImageDecoder.Source source = ImageDecoder.createSource(mainActivity.getContentResolver(),imageUri);

                    Disposable disposable = Observable.fromCallable(() ->ImageDecoder.decodeBitmap(source))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (result) -> binding.ivProfile.setImageBitmap(result),
                                    (error) -> Toast.makeText(mainActivity,getResources()
                                            .getString(R.string.image_unknown),Toast.LENGTH_SHORT).show());
                    compositeDisposable.add(disposable);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            binding.ivProfile.setImageResource(R.mipmap.ic_launcher);

        binding.tvProfileFullName.setText(profileViewModel.getValue().getUserName());

        binding.cvMyFavorites.setOnClickListener(v -> {
            mainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_heart);
        });

        binding.cvOrderHistory.setOnClickListener(v -> {

        });

        binding.cvOrders.setOnClickListener(v -> {
            mainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        });

        binding.cvAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(aliGitHubUrl));
            startActivity(intent);
        });


        binding.cvTellUs.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, aliEmailAddress);  // String[] addresses
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        });


        try {
            binding.tvAppVersion.setText(String.valueOf(mainActivity.getPackageManager()
                    .getPackageInfo(mainActivity.getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }


        binding.btnEditProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intentActivityResultLauncher.launch(intent);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        checkAuthState();
    }

    private void checkAuthState() {
        if (profileViewModel.getValue().isSignedIn()) {
            binding.tvCvSignOutOrLogIn.setText(getResources().getString(R.string.sign_out));
            binding.ivCvSignOutOrLogIn.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
            binding.tvProfileFullName.setText(profileViewModel.getValue().getUserName());
            binding.cvSignOutOrLogIn.setOnClickListener(v -> profileViewModel.getValue().signOut());
        } else {
            binding.tvCvSignOutOrLogIn.setText(getResources().getString(R.string.log_in));
            binding.ivCvSignOutOrLogIn.setImageDrawable(getResources().getDrawable(R.drawable.ic_chevron));
            binding.tvProfileFullName.setText(getResources().getString(R.string.guest_user));
            binding.cvSignOutOrLogIn.setOnClickListener(v -> startActivity(new Intent(mainActivity, AuthActivity.class)));
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof MainActivity)
            this.mainActivity = (MainActivity) context;
    }



    private void requestPermissions() {

        String[] permissions;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_IMAGES};
        else
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};


        List<String> notGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mainActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission);
            }
        }

        if (!notGrantedPermissions.isEmpty()) {
            String[] permissionsArray = notGrantedPermissions.toArray(new String[0]);
            ActivityCompat.requestPermissions(mainActivity, permissionsArray, PERMISSION_REQUEST_CODE);
        } else {
            // Permissions already granted
            Toast.makeText(mainActivity, "Permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                Map<String, Integer> permissionResults = new HashMap<>();
                for (int i = 0; i < permissions.length; i++) {
                    permissionResults.put(permissions[i], grantResults[i]);
                }

                if (permissionResults.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        permissionResults.get(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    // All permissions granted
                    Toast.makeText(mainActivity, "Permissions granted", Toast.LENGTH_SHORT).show();
                    // Start using the permissions here
                } else {
                    // At least one permission denied
                    Toast.makeText(mainActivity, "Permissions denied", Toast.LENGTH_SHORT).show();
                    // Handle denied permissions here
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if ok user selected a file
        if (resultCode == RESULT_OK)
            mainActivity.getContentResolver().takePersistableUriPermission(data.getData(),
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
}