package fr.eni.guessr.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import fr.eni.guessr.model.Guess;
import fr.eni.guessr.model.Level;

@Database(entities = {Level.class, Guess.class}, version = 1, exportSchema = false)
public abstract class LevelDatabase extends RoomDatabase {

    private static LevelDatabase INSTANCE;
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private static Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateBddAsync().execute(INSTANCE);
        }
    };

    public static synchronized LevelDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, LevelDatabase.class, "levels.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return INSTANCE;
    }

    public abstract LevelDao levelDao();

    public abstract GuessDao guessDao();

    private static class PopulateBddAsync extends AsyncTask<LevelDatabase, Void, Void> {
        @Override
        protected Void doInBackground(LevelDatabase... levelDatabases) {
            final LevelDao levelDao = levelDatabases[0].levelDao();
            final GuessDao guessDao = levelDatabases[0].guessDao();
            Log.d("TOTO", "création de la base");


            firebaseDatabase.getReference("levels").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   for (DataSnapshot levelSnapshot : dataSnapshot.getChildren()) {
                       Level level = new Level(Integer.valueOf(Objects.requireNonNull(levelSnapshot.child("id").getValue()).toString()), levelSnapshot.getKey());
                       Log.d("TOTO", "Level trouvé : " + level.toString());
                       new AsyncLevelInsert(levelDao).execute(level);
                       for (DataSnapshot guessSnapshot : levelSnapshot.getChildren()) {
                           if (!"id".equals(guessSnapshot.getKey())) {
                               Guess guess = guessSnapshot.getValue(Guess.class);
                               if (guess != null) {
                                   guess.setLevelId(level.getId());
                                   guess.setStatus("TODO");
                               }
                               assert guess != null;
                               Log.d("TOTO", "Guess trouvé : " + guess.toString());
                               new AsyncGuessInsert(guessDao).execute(guess);
                           }
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
               }
           }
            );

            return null;
        }
    }

    private static class AsyncLevelInsert extends AsyncTask<Level, Void, Void> {

        private LevelDao levelDao;

        AsyncLevelInsert(LevelDao levelDao) {
            this.levelDao = levelDao;
        }

        @Override
        protected Void doInBackground(Level... levels) {
            levelDao.insert(levels[0]);
            return null;
        }
    }

    private static class AsyncGuessInsert extends AsyncTask<Guess, Void, Void> {

        private GuessDao guessDao;

        AsyncGuessInsert(GuessDao guessDao) {
            this.guessDao = guessDao;
        }

        @Override
        protected Void doInBackground(Guess... guesses) {
            guessDao.insert(guesses[0]);
            return null;
        }
    }


}
