package ali.imanifard.raspberrycart.data.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;

public class ProductDTOTypeConverter {

    @TypeConverter
    public static ProductDTO fromString(String value) {
        return new Gson().fromJson(value, ProductDTO.class);
    }

    @TypeConverter
    public static String fromProductDTO(ProductDTO productDTO) {
        return new Gson().toJson(productDTO);
    }

}
