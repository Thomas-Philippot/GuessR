package fr.eni.guessr.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@IgnoreExtraProperties
@Entity
public class Guess {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int levelId;

    private String answer;
    private String image;
    private String status;

    @Ignore
    public Guess() {
    }

    public Guess(String answer, String image) {
        this.answer = answer;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Guess{" +
                "id=" + id +
                ", levelId=" + levelId +
                ", answer='" + answer + '\'' +
                ", image='" + image + '\'' +
                ", status=" + status +
                '}';
    }

    public String getHint() {
        List<String> letters = Arrays.asList(this.answer.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled.toUpperCase();
    }

    public String getLevel() {
        return String.valueOf(this.answer.length() - 3);
    }
}
