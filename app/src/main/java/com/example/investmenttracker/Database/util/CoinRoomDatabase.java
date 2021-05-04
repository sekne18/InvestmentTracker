package com.example.investmenttracker.Database.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.investmenttracker.Database.data.CoinDao;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Coin.class}, version=6,exportSchema = false)
public abstract class CoinRoomDatabase extends RoomDatabase {

    public abstract CoinDao coinDao();
    public static final int NUMBER_OF_THREADS = 4;

    private static volatile CoinRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CoinRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CoinRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CoinRoomDatabase.class, "coin_database").build(); // .addCallback(sRoomDatabaseCallback)
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE coin_table "
                    +"ADD COLUMN owned FLOAT");

        }
    };

    private static final Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                CoinDao coinDao = INSTANCE.coinDao();
                coinDao.deleteAll();

                Coin coin = new Coin(R.drawable.btc, "BTC", 63540f, 10f, R.drawable.heart_border_full);
                coinDao.insert(coin);

                coin = new Coin(R.drawable.eth, "ETH", 2500f, 20f, R.drawable.heart_border_empty);
                coinDao.insert(coin);
            });
        }
    };


}
