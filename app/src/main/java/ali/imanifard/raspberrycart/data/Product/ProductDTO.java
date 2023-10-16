package ali.imanifard.raspberrycart.data.Product;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import ali.imanifard.raspberrycart.data.db.RatingTypeConverter;


@Entity(tableName = "tb_product")
@TypeConverters({RatingTypeConverter.class})
public class ProductDTO implements Parcelable {

    public static final Creator<ProductDTO> CREATOR = new Creator<ProductDTO>() {
        @Override
        public ProductDTO createFromParcel(Parcel in) {
            return new ProductDTO(in);
        }

        @Override
        public ProductDTO[] newArray(int size) {
            return new ProductDTO[size];
        }
    };
    @SerializedName("rating")
    private Rating rating;
    @SerializedName("image")
    private String image;
    @SerializedName("category")
    private String category;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private double price;
    @SerializedName("title")
    private String title;
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    private int id;
    private boolean isFavorite;

    public ProductDTO() {
        // Default Constructor with no Arguments
    }

    protected ProductDTO(Parcel in) {
        rating = in.readParcelable(Rating.class.getClassLoader());
        image = in.readString();
        category = in.readString();
        description = in.readString();
        price = in.readDouble();
        title = in.readString();
        id = in.readInt();
        isFavorite = in.readByte() != 0;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(rating, flags);
        dest.writeString(image);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    public static class Rating implements Parcelable {
        public static final Creator<Rating> CREATOR = new Creator<Rating>() {
            @Override
            public Rating createFromParcel(Parcel in) {
                return new Rating(in);
            }

            @Override
            public Rating[] newArray(int size) {
                return new Rating[size];
            }
        };
        @SerializedName("count")
        private int count;
        @SerializedName("rate")
        private double rate;

        protected Rating(Parcel in) {
            count = in.readInt();
            rate = in.readDouble();
        }

        public Rating(int count, double rate) {
            this.count = count;
            this.rate = rate;
        }

        public int getCount() {
            return count;
        }

        public double getRate() {
            return rate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(count);
            dest.writeDouble(rate);
        }
    }
}
