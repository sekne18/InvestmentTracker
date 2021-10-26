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
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Coin.class}, version=9,exportSchema = false)
public abstract class CoinRoomDatabase extends RoomDatabase {

    public abstract CoinDao coinDao();
    public static final int NUMBER_OF_THREADS = 4;

    private static volatile CoinRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CoinRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CoinRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CoinRoomDatabase.class, "coin_database").fallbackToDestructiveMigration().build(); // .addCallback(sRoomDatabaseCallback)
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(6, 7) {
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

                Coin coin = new Coin("","BTC", 63540f, 10f, Helper.currency, (byte)1);
                coinDao.insert(coin);

                coin = new Coin("","ETH", 2500f, 20f, Helper.currency, (byte)0);
                coinDao.insert(coin);
            });
        }
    };


}
