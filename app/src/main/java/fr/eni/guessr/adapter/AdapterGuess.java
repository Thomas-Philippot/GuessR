package fr.eni.guessr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fr.eni.guessr.R;
import fr.eni.guessr.model.Guess;

public class AdapterGuess extends ArrayAdapter<Guess> {
    private int GuesseRow;

    public AdapterGuess(@NonNull Context context, int resource, @NonNull List<Guess> objects) {
        super(context, resource, objects);
        GuesseRow = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(GuesseRow,parent,false);
        }

        TextView tvTitle = convertView.findViewById(R.id.list_guess_title);
        TextView tvStatus = convertView.findViewById(R.id.list_guess_status);

        Guess guessToShow = getItem(position);

        tvTitle.setText(guessToShow.getAnswer());
        tvStatus.setText(guessToShow.getStatus()    );
        return convertView;
    }
}
