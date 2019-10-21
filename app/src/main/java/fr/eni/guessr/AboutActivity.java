package fr.eni.guessr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View aboutPage = new AboutPage(this)
                .setDescription(this.getDescription())
                .isRTL(false)
                .addItem(this.getDevelopperElement())
                .addItem(this.getThirdPartyElement())
                .addGroup("liens utiles")
                .addEmail("example@gmail.com")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addTwitter("thomas_ph35")
                .addGitHub("Thomas-Philippot/GuessR")
                .create();
                this.setContentView(aboutPage);
    }

    private CharSequence getDescription() {
        return this.getString(R.string.about_description);
    }

    private Element getDevelopperElement() {
        Element developersElement = new Element();
        developersElement.setTitle(getString(R.string.developers));
        return developersElement;
    }

    public Element getThirdPartyElement() {
        Element thirdPartyLicenses = new Element();
        thirdPartyLicenses.setTitle(getString(R.string.third_party_licenses));
        return thirdPartyLicenses;
    }
}
