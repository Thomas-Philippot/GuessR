package fr.eni.guessr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity {

    FloatingActionMenu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.actionMenu = this.findViewById(R.id.menu);
    }

    public void resultClicked(View view) {
        Intent itent = new Intent(this, ResultsActivity.class);
        this.actionMenu.close(true);
        this.startActivity(itent);
    }

    public void proposalClicked(View view) {
        Intent itent = new Intent(this, ProposalActivity.class);
        this.actionMenu.close(true);
        this.startActivity(itent);
    }

    public void aboutClicked(View view) {
        Intent itent = new Intent(this, AboutActivity.class);
        this.actionMenu.close(true);
        this.startActivity(itent);
    }
}
