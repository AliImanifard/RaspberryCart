package ali.imanifard.raspberrycart.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ali.imanifard.raspberrycart.data.Product.ProductDTO;

@Database(entities = {ProductDTO.class, CartEntity.class},exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    @TypeConverters({RatingTypeConverter.class, ProductDTOTypeConverter.class})
    public abstract DbDao productDAO();



}
