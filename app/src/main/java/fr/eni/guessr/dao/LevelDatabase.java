package fr.eni.guessr.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import fr.eni.guessr.model.Guess;
import fr.eni.guessr.model.Level;

@Database(entities = {Level.class, Guess.class}, version = 1)
public abstract class LevelDatabase extends RoomDatabase {

    private static LevelDatabase INSTANCE;
    public abstract LevelDao levelDao();

    public static synchronized LevelDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, LevelDatabase.class, "levels.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return INSTANCE;
    }

    private static Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
