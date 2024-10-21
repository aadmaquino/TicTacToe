package br.com.aadmaquino.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    public static boolean musicIsPlaying;
    public static MediaPlayer mainmusic;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciar som do dispositivo
        musicIsPlaying = true;

        // Mostrar ícone do aplicativo
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.tictactoe_32);
            getSupportActionBar().setTitle("  " + getText(R.string.app_name));
        }

        // Consultar o AdView (Banner) como um recurso e carregar uma solicitação.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Criação da variável da Música Tema
        mainmusic = MediaPlayer.create(this, R.raw.tictactoe);
        mainmusic.setLooping(true);
    }

    public void OnClickSinglePlayer(View view){
        // Chama a atividade activity_singleplayer.xml
        Intent intentSingle = new Intent(this, SingleplayerActivity.class);
        startActivity(intentSingle);
    }

    public void OnClickMultiPlayer(View view){
        // Chama a atividade activity_multiplayer.xml
        Intent intentMulti = new Intent(this, MultiplayerActivity.class);
        startActivity(intentMulti);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Inicia a música tema
        if(musicIsPlaying) {
            mainmusic.start();
        } else {
            mainmusic.pause();
        }

        MainActivity.this.invalidateOptionsMenu();

        // Cria uma ImageView animada
        ImageView img = findViewById(R.id.ImgTicTacToe);
        img.setBackgroundResource(R.drawable.animation_tictactoe);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Define a posição atual da música tema quando ela foi pausada anteriormente
        if(musicIsPlaying) {
            mainmusic.seekTo(mainmusic.getCurrentPosition());
        } else {
            mainmusic.pause();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Pausa a música tema
        mainmusic.pause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_exit);
        builder.setMessage(R.string.action_exit_message);
        builder.setPositiveButton(R.string.button_yes_message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.button_no_message, null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mute = menu.findItem(R.id.action_mute);
        MenuItem unmute = menu.findItem(R.id.action_unmute);

        // Checar música tocando
        if(musicIsPlaying) {
            mute.setVisible(true);
            unmute.setVisible(false);
        } else {
            mute.setVisible(false);
            unmute.setVisible(true);
        }

        return true;
    }

    // Métodos do Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Mute
        if (id == R.id.action_mute) {
            MainActivity.this.invalidateOptionsMenu();
            mainmusic.pause();
            musicIsPlaying = false;
            if(!mainmusic.isPlaying()) {
                final Toast toast = Toast.makeText(MainActivity.this, getText(R.string.action_mute), Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);
            }
            return true;
        }

        // Unmute
        if (id == R.id.action_unmute) {
            MainActivity.this.invalidateOptionsMenu();
            mainmusic.start();
            musicIsPlaying = true;
            if(mainmusic.isPlaying()) {
                final Toast toast = Toast.makeText(MainActivity.this, getText(R.string.action_unmute), Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);
            }
            return true;
        }

        // Sobre
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final TextView websiteTextView = new TextView(builder.getContext());
            final SpannableString webText = new SpannableString("aadmaquino.com.br");
            Linkify.addLinks(webText, Linkify.WEB_URLS);
            websiteTextView.setText(webText);
            websiteTextView.setMovementMethod(LinkMovementMethod.getInstance());
            websiteTextView.setGravity(Gravity.CENTER_HORIZONTAL);

            builder.setTitle(getText(R.string.action_about).toString());
            if(Build.VERSION.SDK_INT >= 24) {
                builder.setMessage(Html.fromHtml("<br><b>" + getText(R.string.app_name).toString() + "<br><br>" + getText(R.string.action_about_message_version).toString() + "</b> " + BuildConfig.VERSION_NAME + "<br><b>" + getText(R.string.action_about_message_developedby).toString() + "</b> " + getText(R.string.action_about_message_author).toString() + "<br>", Html.FROM_HTML_MODE_COMPACT));
            } else {
                builder.setMessage(Html.fromHtml("<b>" + getText(R.string.app_name).toString() + "<br><br>" + getText(R.string.action_about_message_version).toString() + "</b> " + BuildConfig.VERSION_NAME + "<br><b>" + getText(R.string.action_about_message_developedby).toString() + "</b> " + getText(R.string.action_about_message_author).toString() + "<br>"));
            }

            builder.setView(websiteTextView);
            builder.setPositiveButton(R.string.button_ok_message, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Não faça nada
                }
            });
            builder.show();
            return true;
        }

        // Sair
        if (id == R.id.action_exit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.action_exit);
            builder.setMessage(R.string.action_exit_message);
            builder.setPositiveButton(R.string.button_yes_message, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton(R.string.button_no_message, null);
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
