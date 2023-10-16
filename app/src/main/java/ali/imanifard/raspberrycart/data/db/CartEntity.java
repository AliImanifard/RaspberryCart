package ali.imanifard.raspberrycart.data.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;

@Entity(tableName = "tb_cart")
@TypeConverters({ProductDTOTypeConverter.class})
public class CartEntity implements Parcelable {

    public static final Creator<CartEntity> CREATOR = new Creator<CartEntity>() {
        @Override
        public CartEntity createFromParcel(Parcel in) {
            return new CartEntity(in);
        }

        @Override
        public CartEntity[] newArray(int size) {
            return new CartEntity[size];
        }
    };
    @PrimaryKey(autoGenerate = false)
    private Integer id;
    private Integer quantity;
    private Boolean changeCountProgressBarIsVisible = false;
    private ProductDTO productDTO;

    protected CartEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        byte tmpChangeCountProgressBarIsVisible = in.readByte();
        changeCountProgressBarIsVisible = tmpChangeCountProgressBarIsVisible == 0 ? null : tmpChangeCountProgressBarIsVisible == 1;
        productDTO = in.readParcelable(ProductDTO.class.getClassLoader());
    }

    public CartEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getChangeCountProgressBarIsVisible() {
        return changeCountProgressBarIsVisible;
    }

    public void setChangeCountProgressBarIsVisible(Boolean changeCountProgressBarIsVisible) {
        this.changeCountProgressBarIsVisible = changeCountProgressBarIsVisible;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        dest.writeByte((byte) (changeCountProgressBarIsVisible == null ? 0 : changeCountProgressBarIsVisible ? 1 : 2));
        dest.writeParcelable(productDTO, flags);
    }
}
