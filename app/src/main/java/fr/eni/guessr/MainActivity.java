package fr.eni.guessr;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity {

    private FloatingActionMenu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.actionMenu = this.findViewById(R.id.menu);

        this.createCustomAnimation();

    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(this.actionMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(this.actionMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(this.actionMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(this.actionMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                actionMenu.getMenuIconView().setImageResource(actionMenu.isOpened()
                        ? R.drawable.ic_menu : R.drawable.ic_close);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        actionMenu.setIconToggleAnimatorSet(set);
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

    public void playClicked(View view) {
        Intent itent = new Intent(this, PlayActivity.class);
        this.actionMenu.close(true);
        this.startActivity(itent);
    }
}
