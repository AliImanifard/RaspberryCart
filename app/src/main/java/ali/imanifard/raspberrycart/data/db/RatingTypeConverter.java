package ali.imanifard.raspberrycart.data.db;

import androidx.room.TypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;

public class RatingTypeConverter {


    // Store in DB as JSON string and retrieve it ( GSON library optional).

    private static final String COUNT = "count";
    private static final String RATE = "rate";

    @TypeConverter
    public static ProductDTO.Rating toRating(String ratingString) {
        if (ratingString == null)
            return null;
        try {
            JSONObject jsonObject = new JSONObject(ratingString);
            int count = jsonObject.getInt(COUNT);
            double rate = jsonObject.getDouble(RATE);
            return new ProductDTO.Rating(count, rate);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    @TypeConverter
    public static String fromRating(ProductDTO.Rating rating) {
        if (rating == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(COUNT, rating.getCount());
            jsonObject.put(RATE, rating.getRate());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
