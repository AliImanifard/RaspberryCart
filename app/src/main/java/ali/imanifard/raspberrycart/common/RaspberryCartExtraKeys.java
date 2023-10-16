package ali.imanifard.raspberrycart.common;

import android.content.res.Resources;

import androidx.core.content.res.ResourcesCompat;

import ali.imanifard.raspberrycart.R;

public class RaspberryCartExtraKeys {
    public static final String EXTRA_KEY_DATA = "data";

    public static final int VIEW_TYPE_ROUND = 0;   // default ViewType For ProductListAdapter for Home Fragment
    public static final int VIEW_TYPE_SMALL = 1;   // ViewType For ProductListAdapter when category clicked on ProductCatalogActivity
    public static final int VIEW_TYPE_LARGE = 2;   // ViewType For ProductListAdapter when category clicked on ProductCatalogActivity


    public static final int SORT_BEST_SELLING = 0;
    public static final int SORT_POPULAR = 1;
    public static final int SORT_PRICE_LOW_TO_HIGH = 2;
    public static final int SORT_PRICE_HIGH_TO_LOW = 3;

    public static final String apiToken = Resources.getSystem().getString(R.string.one_api_key_for_translation);

    public static final String aliGitHubUrl = "https://github.com/AliImanifard";
    public static final String[] aliEmailAddress = new String[]{"aliimanifard13@gmail.com"};
}
