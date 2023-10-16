package ali.imanifard.raspberrycart.services.image_loading;

import com.facebook.drawee.view.SimpleDraweeView;

public interface ImageLoadingService {

    void load(SimpleDraweeView imageView, String imageUrl);
}
