package ali.imanifard.raspberrycart.services.image_loading;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

public class FrescoImageLoadingService implements ImageLoadingService {
    @Override
    public void load(SimpleDraweeView imageView, String imageUrl) {
        if (imageView != null){
            imageView.setImageURI(Uri.parse(imageUrl));
        }else
            throw new IllegalStateException("ImageView is null");
    }
}
