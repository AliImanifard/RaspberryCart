package ali.imanifard.raspberrycart.feature.introSlider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.common.RaspberryCartActivity;
import ali.imanifard.raspberrycart.databinding.ActivityIntroSliderBinding;
import ali.imanifard.raspberrycart.feature.main.MainActivity;

public class IntroSliderActivity extends RaspberryCartActivity {

    private ViewPager2 viewPager2;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;
    private Button btnNext;
    private TextView btnSkip;
    private PrefManager prefManager;
    private DotsIndicator dotsIndicator;

    private ActivityIntroSliderBinding binding;
    //  viewpager2 change listener
    ViewPager2.OnPageChangeCallback viewPagerPageChangeListener = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                binding.btnNext.setText(getResources().getString(R.string.got_it));
                binding.btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                binding.btnNext.setText(getString(R.string.next));
                binding.btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroSliderBinding.inflate(getLayoutInflater());


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchMainActivity();
            finish();
        }



        // Making notification bar transparent
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // set Content View
        setContentView(binding.getRoot());

        viewPager2 = binding.vp2IntroSlider;
        btnSkip = binding.btnSkip;
        btnNext = binding.btnNext;

        dotsIndicator = binding.layoutDots;


        // layouts of all intro sliders
        layouts = new int[]{
                R.layout.intro_slide_1,
                R.layout.intro_slide_2,
                R.layout.intro_slide_3
        };

        // making notification bar transparent
        changeStatusBarColor();


        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager2.setAdapter(myViewPagerAdapter);
        dotsIndicator.attachTo(viewPager2);
        viewPager2.registerOnPageChangeCallback(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page Main Activity will be launched
                int current = viewPager2.getCurrentItem() + 1;
                if (current < layouts.length) {
                    // move to next screen
                    viewPager2.setCurrentItem(current);
                } else {
                    launchMainActivity();
                }
            }
        });

    }

    private void launchMainActivity() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroSliderActivity.this, MainActivity.class));
        finish();
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private class PrefManager {
        // Shared preferences file name
        private static final String PREF_NAME = "intro_slider";
        private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        Context _context;
        // shared pref mode
        int PRIVATE_MODE = 0;

        public PrefManager(Context context) {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        public boolean isFirstTimeLaunch() {
            return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
        }

        public void setFirstTimeLaunch(boolean isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
            editor.commit();
        }
    }

    //  viewpager2 Adapter
    private class MyViewPagerAdapter extends RecyclerView.Adapter<MyViewPagerAdapter.MyViewHolder> {

        private static final int LAYOUT_INTRO_SLIDE_1 = 0;
        private static final int LAYOUT_INTRO_SLIDE_2 = 1;
        private static final int LAYOUT_INTRO_SLIDE_3 = 2;

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = null;

            switch (viewType) {
                case LAYOUT_INTRO_SLIDE_1 -> itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.intro_slide_1, parent, false);
                case LAYOUT_INTRO_SLIDE_2 -> itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.intro_slide_2, parent, false);
                case LAYOUT_INTRO_SLIDE_3 -> itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.intro_slide_3, parent, false);

            }
            //View itemView = LayoutInflater.from(parent.getContext()).inflate(layouts[viewType],parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return layouts.length;
        }

        @Override
        public int getItemViewType(int position) {
            //return super.getItemViewType(position);

            return switch (position) {
                case 0 -> LAYOUT_INTRO_SLIDE_1;
                case 1 -> LAYOUT_INTRO_SLIDE_2;
                case 2 -> LAYOUT_INTRO_SLIDE_3;
                default -> throw new IllegalStateException("Unexpected value: " + position);
            };

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}