package br.com.aadmaquino.tictactoe;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Random;

import static br.com.aadmaquino.tictactoe.MainActivity.mainmusic;
import static br.com.aadmaquino.tictactoe.MainActivity.musicIsPlaying;

public class SingleplayerActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    private boolean adIsntOpened = true;
    private final int cruz = 1, bola = 2;
    private TextView Owinstxt, Xwinstxt, turnTextView, turnElement;
    private Button passbutton, newgamebutton;
    int playas = cruz,
        turno = playas,
        rodada = 1,
        vencedor = 0,
        Xwinsint = 0,
        Owinsint = 0,
        difficulty = 1, /* 0= Easy; 1= Medium; 2= Hard; 3= Impossible */
        lockcount = 0,
        num;
    Random random;
    String texto;
    btn[] btn = new btn[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);

        // Mostrar botão 'voltar' no ActionBar do aplicativo
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // AdMob Intersticial
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1749022888312040/3674234210");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                mainmusic.pause();
                adIsntOpened = false;
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                mainmusic.pause();
                adIsntOpened = false;
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                requestNewInterstitial();
                adIsntOpened = true;
                finish();
            }
        });
        requestNewInterstitial();

        // Criação de variáveis de objetos da Activity
        btn[0] = new btn((ImageButton) findViewById(R.id.BtnA1));
        btn[1] = new btn((ImageButton) findViewById(R.id.BtnB1));
        btn[2] = new btn((ImageButton) findViewById(R.id.BtnC1));
        btn[3] = new btn((ImageButton) findViewById(R.id.BtnA2));
        btn[4] = new btn((ImageButton) findViewById(R.id.BtnB2));
        btn[5] = new btn((ImageButton) findViewById(R.id.BtnC2));
        btn[6] = new btn((ImageButton) findViewById(R.id.BtnA3));
        btn[7] = new btn((ImageButton) findViewById(R.id.BtnB3));
        btn[8] = new btn((ImageButton) findViewById(R.id.BtnC3));

        Xwinstxt = findViewById(R.id.TxtXwins);
        Xwinstxt.setText(String.valueOf(Xwinsint), TextView.BufferType.NORMAL);
        Owinstxt = findViewById(R.id.TxtOwins);
        Owinstxt.setText(String.valueOf(Owinsint), TextView.BufferType.NORMAL);

        turnTextView = findViewById(R.id.TxtTurn);
        turnElement = findViewById(R.id.TxtTurnElement);
        turnText();

        passbutton = findViewById(R.id.BtnPassPlayer);
        newgamebutton = findViewById(R.id.BtnNewGame);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    private class btn {
        private final ImageButton btn;
        private int valor;

        private void setValor(int valor) {
            this.valor = valor;
        }

        private ImageButton getBtn() {
            return this.btn;
        }

        private int getValor() {
            return this.valor;
        }

        private btn(ImageButton btn) {
            this.btn = btn;
            this.valor = 0;

            this.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBtn().setEnabled(false);
                    setValor(turno);
                    if(turno == cruz) {
                        getBtn().setImageResource(R.drawable.x_element);
                        turno = bola;
                    } else {
                        getBtn().setImageResource(R.drawable.o_element);
                        turno = cruz;
                    }
                    turnText();
                    rodada++;
                    verificarFim();
                    if(vencedor == 0) {
                        moveThread();
                    }
                }
            });
        }
    }

    public void verificarFim() {
        vencedor = 0;

        if(rodada > 1 && (passbutton.getVisibility() == View.VISIBLE)) {
            passbutton.setVisibility(View.INVISIBLE);
        }

        // Horizontal
        if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[1].getValor()) && (btn[0].getValor() == btn[2].getValor())) {
            vencedor = btn[0].getValor();
            btn[0].getBtn().setBackgroundResource(R.drawable.button_a1_win);
            btn[1].getBtn().setBackgroundResource(R.drawable.button_b1_win);
            btn[2].getBtn().setBackgroundResource(R.drawable.button_c1_win);
        } else if((btn[3].getValor() > 0) && (btn[3].getValor() == btn[4].getValor()) && (btn[3].getValor() == btn[5].getValor())) {
            vencedor = btn[3].getValor();
            btn[3].getBtn().setBackgroundResource(R.drawable.button_a2_win);
            btn[4].getBtn().setBackgroundResource(R.drawable.button_b2_win);
            btn[5].getBtn().setBackgroundResource(R.drawable.button_c2_win);
        } else if((btn[6].getValor() > 0) && (btn[6].getValor() == btn[7].getValor()) && (btn[6].getValor() == btn[8].getValor())) {
            vencedor = btn[6].getValor();
            btn[6].getBtn().setBackgroundResource(R.drawable.button_a3_win);
            btn[7].getBtn().setBackgroundResource(R.drawable.button_b3_win);
            btn[8].getBtn().setBackgroundResource(R.drawable.button_c3_win);
        }

        // Vertical
        else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[3].getValor()) && (btn[0].getValor() == btn[6].getValor())) {
            vencedor = btn[0].getValor();
            btn[0].getBtn().setBackgroundResource(R.drawable.button_a1_win);
            btn[3].getBtn().setBackgroundResource(R.drawable.button_a2_win);
            btn[6].getBtn().setBackgroundResource(R.drawable.button_a3_win);
        } else if((btn[1].getValor() > 0) && (btn[1].getValor() == btn[4].getValor()) && (btn[1].getValor() == btn[7].getValor())) {
            vencedor = btn[1].getValor();
            btn[1].getBtn().setBackgroundResource(R.drawable.button_b1_win);
            btn[4].getBtn().setBackgroundResource(R.drawable.button_b2_win);
            btn[7].getBtn().setBackgroundResource(R.drawable.button_b3_win);
        } else if((btn[2].getValor() > 0) && (btn[2].getValor() == btn[5].getValor()) && (btn[2].getValor() == btn[8].getValor())) {
            vencedor = btn[2].getValor();
            btn[2].getBtn().setBackgroundResource(R.drawable.button_c1_win);
            btn[5].getBtn().setBackgroundResource(R.drawable.button_c2_win);
            btn[8].getBtn().setBackgroundResource(R.drawable.button_c3_win);
        }

        // Diagonal
        else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[4].getValor()) && (btn[0].getValor() == btn[8].getValor())) {
            vencedor = btn[0].getValor();
            btn[0].getBtn().setBackgroundResource(R.drawable.button_a1_win);
            btn[4].getBtn().setBackgroundResource(R.drawable.button_b2_win);
            btn[8].getBtn().setBackgroundResource(R.drawable.button_c3_win);
        } else if((btn[2].getValor() > 0) && (btn[2].getValor() == btn[4].getValor()) && (btn[2].getValor() == btn[6].getValor())) {
            vencedor = btn[2].getValor();
            btn[2].getBtn().setBackgroundResource(R.drawable.button_c1_win);
            btn[4].getBtn().setBackgroundResource(R.drawable.button_b2_win);
            btn[6].getBtn().setBackgroundResource(R.drawable.button_a3_win);
        }

        if((rodada >= 10) && (vencedor == 0)) {
            vencedor = -1;
        }

        if(vencedor != 0) {
            turnElement.setVisibility(View.GONE);
            turnTextView.setText(getText(R.string.gameover));

            for (int i = 0; i < 9; i++) {
                btn[i].getBtn().setEnabled(false);
            }

            if(vencedor == cruz) {
                texto = getText(R.string.winner_X).toString();
                Xwinsint++;
                Xwinstxt.setText(String.valueOf(Xwinsint), TextView.BufferType.NORMAL);
            } else if (vencedor == bola) {
                texto = getText(R.string.winner_O).toString();
                Owinsint++;
                Owinstxt.setText(String.valueOf(Owinsint), TextView.BufferType.NORMAL);
            } else {
                texto = getText(R.string.draw_game).toString();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.gameover);
            builder.setMessage(texto);
            builder.setPositiveButton(R.string.button_ok_message, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Não faça nada
                }
            });
            builder.show();
        }
    }

    public void onClickNewGame(View view) {
        if(view.getId() == R.id.BtnNewGame) {
            newGame();
        }
    }

    public void newGame() {
        for (int i = 0; i < 9; i++) {
            btn[i].getBtn().setEnabled(true);
            btn[i].getBtn().setImageResource(android.R.color.transparent);
            btn[i].setValor(0);
        }
        turno = playas;
        rodada = 1;
        vencedor = 0;

        btn[0].getBtn().setBackgroundResource(R.drawable.button_a1);
        btn[1].getBtn().setBackgroundResource(R.drawable.button_b1);
        btn[2].getBtn().setBackgroundResource(R.drawable.button_c1);
        btn[3].getBtn().setBackgroundResource(R.drawable.button_a2);
        btn[4].getBtn().setBackgroundResource(R.drawable.button_b2);
        btn[5].getBtn().setBackgroundResource(R.drawable.button_c2);
        btn[6].getBtn().setBackgroundResource(R.drawable.button_a3);
        btn[7].getBtn().setBackgroundResource(R.drawable.button_b3);
        btn[8].getBtn().setBackgroundResource(R.drawable.button_c3);

        SingleplayerActivity.this.invalidateOptionsMenu();

        turnTextView.setText(getText(R.string.turn));
        turnElement.setVisibility(View.VISIBLE);
        turnText();

        passbutton.setVisibility(View.VISIBLE);
    }

    public void showConfig() {
        View layout = View.inflate(SingleplayerActivity.this, R.layout.dialog_config_singleplayer, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton(R.string.button_ok_message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Não faça nada
            }
        });

        // Play As
        RadioButton radioX = layout.findViewById(R.id.RdbXelement_singleplayer);
        RadioButton radioO = layout.findViewById(R.id.RdbOelement_singleplayer);

        if (playas == cruz) {
            radioX.setChecked(true);
        } else if (playas == bola) {
            radioO.setChecked(true);
        }

        RadioGroup rg = layout.findViewById(R.id.RdgPlayAs_singleplayer);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId) {
                    case R.id.RdbXelement_singleplayer:
                        playas = cruz;
                        newGame();
                        break;
                    case R.id.RdbOelement_singleplayer:
                        playas = bola;
                        newGame();
                        break;
                }
            }
        });

        // Difficulty
        Spinner spinnerDif = layout.findViewById(R.id.SpinnerDifficulty);
        if (difficulty == 0) {
            spinnerDif.setSelection(0);
        } else if (difficulty == 1) {
            spinnerDif.setSelection(1);
        } else if (difficulty == 2) {
            spinnerDif.setSelection(2);
        }
//        } else if (difficulty == 3) {
//            spinnerDif.setSelection(3);
//        }
        spinnerDif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals(getText(R.string.difficulty_easy).toString())) {
                    if(difficulty != 0) {
                        newGame();
                    }
                    difficulty = 0;
                } else if(selectedItem.equals(getText(R.string.difficulty_medium).toString())) {
                    if(difficulty != 1) {
                        newGame();
                    }
                    difficulty = 1;
                } else if(selectedItem.equals(getText(R.string.difficulty_hard).toString())) {
                    if (difficulty != 2) {
                        newGame();
                    }
                    difficulty = 2;
                }
//                } else if(selectedItem.equals(getText(R.string.difficulty_impossible).toString())) {
//                    if(difficulty != 3) {
//                        newGame();
//                    }
//                    difficulty = 3;
//                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não faça nada
            }
        });

        // Reset Score
        Button btnreset = layout.findViewById(R.id.BtnResetScore_singleplayer);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetScore();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void resetScore() {
        Xwinsint = 0;
        Xwinstxt.setText(String.valueOf(Xwinsint), TextView.BufferType.NORMAL);
        Owinsint = 0;
        Owinstxt.setText(String.valueOf(Owinsint), TextView.BufferType.NORMAL);
    }

    public void turnText() {
        if(turno == cruz) {
            turnElement.setTextColor(Color.parseColor("#FF3333"));
            turnElement.setText(getText(R.string.Xelement));
        } else {
            turnElement.setTextColor(Color.parseColor("#3333FF"));
            turnElement.setText(getText(R.string.Oelement));
        }
    }

    public void onClickPassPlayer(View view) {
        if(view.getId() == R.id.BtnPassPlayer) {
            passPlayer();
        }
    }

    public void passPlayer() {
        passbutton.setVisibility(View.INVISIBLE);
        if(turno == cruz) {
            turno = bola;
        } else {
            turno = cruz;
        }
        turnText();
        moveThread();
    }

    public void lockGame() {
        // Lock the board game
        for (lockcount = 0; lockcount < 9; lockcount++) {
            if (btn[lockcount].getValor() == 0) {
                btn[lockcount].getBtn().setEnabled(false);
            }
        }
        newgamebutton.setEnabled(false);
    }

    public void unlockGame() {
        // Unlock the board game and let the human to play
        for (lockcount = 0; lockcount < 9; lockcount++) {
            if ((btn[lockcount].getValor() == 0) && (vencedor == 0)){
                btn[lockcount].getBtn().setEnabled(true);
            }
        }
        newgamebutton.setEnabled(true);
    }

    public void moveThread() {
        lockGame();

        // Thread for CPU move
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SingleplayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        moveFromCPU();
                        unlockGame();
                    }
                });
            }
        }).start();
    }

    public void moveFromCPU() {
        // Easy
        if(difficulty == 0) {
            random = new Random();
            num = random.nextInt(9);
            while(btn[num].getValor() != 0) {
                if((btn[0].getValor() != 0) && (btn[1].getValor() != 0) && (btn[2].getValor() != 0) && (btn[3].getValor() != 0) && (btn[4].getValor() != 0) && (btn[5].getValor() != 0) && (btn[6].getValor() != 0) && (btn[7].getValor() != 0) && (btn[8].getValor() != 0)) {
                    break;
                }
                num++;
                if(num >= 9) {
                    num = 0;
                }
            }

            btn[num].getBtn().setEnabled(false);
            btn[num].setValor(turno);
            if (turno == cruz) {
                btn[num].getBtn().setImageResource(R.drawable.x_element);
                turno = bola;
            } else {
                btn[num].getBtn().setImageResource(R.drawable.o_element);
                turno = cruz;
            }
            turnText();

            rodada++;
            if (vencedor == 0) {
                verificarFim();
            }
        }

        // Medium
        else if(difficulty == 1) {
            random = new Random();
            num = random.nextInt(9);
            while(btn[num].getValor() != 0) {
                if((btn[0].getValor() != 0) && (btn[1].getValor() != 0) && (btn[2].getValor() != 0) && (btn[3].getValor() != 0) && (btn[4].getValor() != 0) && (btn[5].getValor() != 0) && (btn[6].getValor() != 0) && (btn[7].getValor() != 0) && (btn[8].getValor() != 0)) {
                    break;
                }
                num++;
                if(num >= 9) {
                    num = 0;
                }
            }

            if((btn[1].getValor() > 0) && (btn[1].getValor() == btn[2].getValor()) && (btn[2].getValor() > 0) && (btn[0].getValor() == 0)) {
                num = 0;
            } else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[2].getValor()) && (btn[2].getValor() > 0) && (btn[1].getValor() == 0)) {
                num = 1;
            } else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[1].getValor()) && (btn[1].getValor() > 0) && (btn[2].getValor() == 0)) {
                num = 2;
            } else if((btn[4].getValor() > 0) && (btn[4].getValor() == btn[5].getValor()) && (btn[5].getValor() > 0) && (btn[3].getValor() == 0)) {
                num = 3;
            } else if((btn[3].getValor() > 0) && (btn[3].getValor() == btn[5].getValor()) && (btn[5].getValor() > 0) && (btn[4].getValor() == 0)) {
                num = 4;
            } else if((btn[3].getValor() > 0) && (btn[3].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[5].getValor() == 0)) {
                num = 5;
            } else if((btn[7].getValor() > 0) && (btn[7].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[6].getValor() == 0)) {
                num = 6;
            } else if((btn[6].getValor() > 0) && (btn[6].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[7].getValor() == 0)) {
                num = 7;
            } else if((btn[6].getValor() > 0) && (btn[6].getValor() == btn[7].getValor()) && (btn[7].getValor() > 0) && (btn[8].getValor() == 0)) {
                num = 8;
            } else if((btn[3].getValor() > 0) && (btn[3].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[0].getValor() == 0)) {
                num = 0;
            } else if((btn[4].getValor() > 0) && (btn[4].getValor() == btn[7].getValor()) && (btn[7].getValor() > 0) && (btn[1].getValor() == 0)) {
                num = 1;
            } else if((btn[5].getValor() > 0) && (btn[5].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[2].getValor() == 0)) {
                num = 2;
            } else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[3].getValor() == 0)) {
                num = 3;
            } else if((btn[1].getValor() > 0) && (btn[1].getValor() == btn[7].getValor()) && (btn[7].getValor() > 0) && (btn[4].getValor() == 0)) {
                num = 4;
            } else if((btn[2].getValor() > 0) && (btn[2].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[5].getValor() == 0)) {
                num = 5;
            } else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[3].getValor()) && (btn[3].getValor() > 0) && (btn[6].getValor() == 0)) {
                num = 6;
            } else if((btn[1].getValor() > 0) && (btn[1].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[7].getValor() == 0)) {
                num = 7;
            } else if((btn[2].getValor() > 0) && (btn[2].getValor() == btn[5].getValor()) && (btn[5].getValor() > 0) && (btn[8].getValor() == 0)) {
                num = 8;
            } else if((btn[4].getValor() > 0) && (btn[4].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[0].getValor() == 0)) {
                num = 0;
            } else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[4].getValor() == 0)) {
                num = 4;
            } else if((btn[0].getValor() > 0) && (btn[0].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[8].getValor() == 0)) {
                num = 8;
            } else if((btn[4].getValor() > 0) && (btn[4].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[2].getValor() == 0)) {
                num = 2;
            } else if((btn[2].getValor() > 0) && (btn[2].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[4].getValor() == 0)) {
                num = 4;
            } else if((btn[2].getValor() > 0) && (btn[2].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[6].getValor() == 0)) {
                num = 6;
            }

            btn[num].getBtn().setEnabled(false);
            btn[num].setValor(turno);
            if (turno == cruz) {
                btn[num].getBtn().setImageResource(R.drawable.x_element);
                turno = bola;
            } else {
                btn[num].getBtn().setImageResource(R.drawable.o_element);
                turno = cruz;
            }
            turnText();

            rodada++;
            if (vencedor == 0) {
                verificarFim();
            }
        }

        // Hard
        else if(difficulty == 2) {
            random = new Random();
            num = random.nextInt(9);
            while(btn[num].getValor() != 0) {
                if((btn[0].getValor() != 0) && (btn[1].getValor() != 0) && (btn[2].getValor() != 0) && (btn[3].getValor() != 0) && (btn[4].getValor() != 0) && (btn[5].getValor() != 0) && (btn[6].getValor() != 0) && (btn[7].getValor() != 0) && (btn[8].getValor() != 0)) {
                    break;
                }
                num++;
                if(num >= 9) {
                    num = 0;
                }
            }

            if ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) {
                num = 4;
            } else {
                if ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() != 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) {
                    random = new Random();
                    num = random.nextInt(3);
                    if(num == 0) {
                        num = 0;
                    } else if (num == 1) {
                        num = 2;
                    } else if (num == 2) {
                        num = 6;
                    } else {
                        num = 8;
                    }
                } else if ((btn[0].getValor() == 0) && (btn[1].getValor() != 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) {
                    random = new Random();
                    num = random.nextInt(5);
                    if(num == 0) {
                        num = 0;
                    } else if (num == 1) {
                        num = 2;
                    } else if (num == 2) {
                        num = 4;
                    } else if (num == 3) {
                        num = 6;
                    } else if (num == 4) {
                        num = 7;
                    } else {
                        num = 8;
                    }
                } else if ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() != 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) {
                    random = new Random();
                    num = random.nextInt(5);
                    if(num == 0) {
                        num = 0;
                    } else if (num == 1) {
                        num = 2;
                    } else if (num == 2) {
                        num = 4;
                    } else if (num == 3) {
                        num = 5;
                    } else if (num == 4) {
                        num = 6;
                    } else {
                        num = 8;
                    }
                } else if ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() != 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) {
                    random = new Random();
                    num = random.nextInt(5);
                    if(num == 0) {
                        num = 0;
                    } else if (num == 1) {
                        num = 2;
                    } else if (num == 2) {
                        num = 3;
                    } else if (num == 3) {
                        num = 4;
                    } else if (num == 4) {
                        num = 6;
                    } else {
                        num = 8;
                    }
                } else if ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() != 0) && (btn[8].getValor() == 0)) {
                    random = new Random();
                    num = random.nextInt(5);
                    if(num == 0) {
                        num = 0;
                    } else if (num == 1) {
                        num = 1;
                    } else if (num == 2) {
                        num = 2;
                    } else if (num == 3) {
                        num = 4;
                    } else if (num == 4) {
                        num = 6;
                    } else {
                        num = 8;
                    }
                } else if (((btn[0].getValor() != 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) || ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() != 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) || ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() != 0) && (btn[7].getValor() == 0) && (btn[8].getValor() == 0)) || ((btn[0].getValor() == 0) && (btn[1].getValor() == 0) && (btn[2].getValor() == 0) && (btn[3].getValor() == 0) && (btn[4].getValor() == 0) && (btn[5].getValor() == 0) && (btn[6].getValor() == 0) && (btn[7].getValor() == 0) && (btn[8].getValor() != 0))) {
                    num = 4;
                }

                if ((btn[1].getValor() > 0) && (btn[1].getValor() == btn[2].getValor()) && (btn[2].getValor() > 0) && (btn[0].getValor() == 0)) {
                    num = 0;
                } else if ((btn[0].getValor() > 0) && (btn[0].getValor() == btn[2].getValor()) && (btn[2].getValor() > 0) && (btn[1].getValor() == 0)) {
                    num = 1;
                } else if ((btn[0].getValor() > 0) && (btn[0].getValor() == btn[1].getValor()) && (btn[1].getValor() > 0) && (btn[2].getValor() == 0)) {
                    num = 2;
                } else if ((btn[4].getValor() > 0) && (btn[4].getValor() == btn[5].getValor()) && (btn[5].getValor() > 0) && (btn[3].getValor() == 0)) {
                    num = 3;
                } else if ((btn[3].getValor() > 0) && (btn[3].getValor() == btn[5].getValor()) && (btn[5].getValor() > 0) && (btn[4].getValor() == 0)) {
                    num = 4;
                } else if ((btn[3].getValor() > 0) && (btn[3].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[5].getValor() == 0)) {
                    num = 5;
                } else if ((btn[7].getValor() > 0) && (btn[7].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[6].getValor() == 0)) {
                    num = 6;
                } else if ((btn[6].getValor() > 0) && (btn[6].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[7].getValor() == 0)) {
                    num = 7;
                } else if ((btn[6].getValor() > 0) && (btn[6].getValor() == btn[7].getValor()) && (btn[7].getValor() > 0) && (btn[8].getValor() == 0)) {
                    num = 8;
                } else if ((btn[3].getValor() > 0) && (btn[3].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[0].getValor() == 0)) {
                    num = 0;
                } else if ((btn[4].getValor() > 0) && (btn[4].getValor() == btn[7].getValor()) && (btn[7].getValor() > 0) && (btn[1].getValor() == 0)) {
                    num = 1;
                } else if ((btn[5].getValor() > 0) && (btn[5].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[2].getValor() == 0)) {
                    num = 2;
                } else if ((btn[0].getValor() > 0) && (btn[0].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[3].getValor() == 0)) {
                    num = 3;
                } else if ((btn[1].getValor() > 0) && (btn[1].getValor() == btn[7].getValor()) && (btn[7].getValor() > 0) && (btn[4].getValor() == 0)) {
                    num = 4;
                } else if ((btn[2].getValor() > 0) && (btn[2].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[5].getValor() == 0)) {
                    num = 5;
                } else if ((btn[0].getValor() > 0) && (btn[0].getValor() == btn[3].getValor()) && (btn[3].getValor() > 0) && (btn[6].getValor() == 0)) {
                    num = 6;
                } else if ((btn[1].getValor() > 0) && (btn[1].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[7].getValor() == 0)) {
                    num = 7;
                } else if ((btn[2].getValor() > 0) && (btn[2].getValor() == btn[5].getValor()) && (btn[5].getValor() > 0) && (btn[8].getValor() == 0)) {
                    num = 8;
                } else if ((btn[4].getValor() > 0) && (btn[4].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[0].getValor() == 0)) {
                    num = 0;
                } else if ((btn[0].getValor() > 0) && (btn[0].getValor() == btn[8].getValor()) && (btn[8].getValor() > 0) && (btn[4].getValor() == 0)) {
                    num = 4;
                } else if ((btn[0].getValor() > 0) && (btn[0].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[8].getValor() == 0)) {
                    num = 8;
                } else if ((btn[4].getValor() > 0) && (btn[4].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[2].getValor() == 0)) {
                    num = 2;
                } else if ((btn[2].getValor() > 0) && (btn[2].getValor() == btn[6].getValor()) && (btn[6].getValor() > 0) && (btn[4].getValor() == 0)) {
                    num = 4;
                } else if ((btn[2].getValor() > 0) && (btn[2].getValor() == btn[4].getValor()) && (btn[4].getValor() > 0) && (btn[6].getValor() == 0)) {
                    num = 6;
                }

                if (turno == cruz) {
                    if ((btn[1].getValor() == cruz) && (btn[2].getValor() == cruz) && (btn[0].getValor() == 0)) {
                        num = 0;
                    } else if ((btn[0].getValor() == cruz) && (btn[2].getValor() == cruz) && (btn[1].getValor() == 0)) {
                        num = 1;
                    } else if ((btn[0].getValor() == cruz) && (btn[1].getValor() == cruz) && (btn[2].getValor() == 0)) {
                        num = 2;
                    } else if ((btn[4].getValor() == cruz) && (btn[5].getValor() == cruz) && (btn[3].getValor() == 0)) {
                        num = 3;
                    } else if ((btn[3].getValor() == cruz) && (btn[5].getValor() == cruz) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[3].getValor() == cruz) && (btn[4].getValor() == cruz) && (btn[5].getValor() == 0)) {
                        num = 5;
                    } else if ((btn[7].getValor() == cruz) && (btn[8].getValor() == cruz) && (btn[6].getValor() == 0)) {
                        num = 6;
                    } else if ((btn[6].getValor() == cruz) && (btn[8].getValor() == cruz) && (btn[7].getValor() == 0)) {
                        num = 7;
                    } else if ((btn[6].getValor() == cruz) && (btn[7].getValor() == cruz) && (btn[8].getValor() == 0)) {
                        num = 8;
                    } else if ((btn[3].getValor() == cruz) && (btn[6].getValor() == cruz) && (btn[0].getValor() == 0)) {
                        num = 0;
                    } else if ((btn[4].getValor() == cruz) && (btn[7].getValor() == cruz) && (btn[1].getValor() == 0)) {
                        num = 1;
                    } else if ((btn[5].getValor() == cruz) && (btn[8].getValor() == cruz) && (btn[2].getValor() == 0)) {
                        num = 2;
                    } else if ((btn[0].getValor() == cruz) && (btn[6].getValor() == cruz) && (btn[3].getValor() == 0)) {
                        num = 3;
                    } else if ((btn[1].getValor() == cruz) && (btn[7].getValor() == cruz) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[2].getValor() == cruz) && (btn[8].getValor() == cruz) && (btn[5].getValor() == 0)) {
                        num = 5;
                    } else if ((btn[0].getValor() == cruz) && (btn[3].getValor() == cruz) && (btn[6].getValor() == 0)) {
                        num = 6;
                    } else if ((btn[1].getValor() == cruz) && (btn[4].getValor() == cruz) && (btn[7].getValor() == 0)) {
                        num = 7;
                    } else if ((btn[2].getValor() == cruz) && (btn[5].getValor() == cruz) && (btn[8].getValor() == 0)) {
                        num = 8;
                    } else if ((btn[4].getValor() == cruz) && (btn[8].getValor() == cruz) && (btn[0].getValor() == 0)) {
                        num = 0;
                    } else if ((btn[0].getValor() == cruz) && (btn[8].getValor() == cruz) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[0].getValor() == cruz) && (btn[4].getValor() == cruz) && (btn[8].getValor() == 0)) {
                        num = 8;
                    } else if ((btn[4].getValor() == cruz) && (btn[6].getValor() == cruz) && (btn[2].getValor() == 0)) {
                        num = 2;
                    } else if ((btn[2].getValor() == cruz) && (btn[6].getValor() == cruz) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[2].getValor() == cruz) && (btn[4].getValor() == cruz) && (btn[6].getValor() == 0)) {
                        num = 6;
                    }
                } else if (turno == bola) {
                    if ((btn[1].getValor() == bola) && (btn[2].getValor() == bola) && (btn[0].getValor() == 0)) {
                        num = 0;
                    } else if ((btn[0].getValor() == bola) && (btn[2].getValor() == bola) && (btn[1].getValor() == 0)) {
                        num = 1;
                    } else if ((btn[0].getValor() == bola) && (btn[1].getValor() == bola) && (btn[2].getValor() == 0)) {
                        num = 2;
                    } else if ((btn[4].getValor() == bola) && (btn[5].getValor() == bola) && (btn[3].getValor() == 0)) {
                        num = 3;
                    } else if ((btn[3].getValor() == bola) && (btn[5].getValor() == bola) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[3].getValor() == bola) && (btn[4].getValor() == bola) && (btn[5].getValor() == 0)) {
                        num = 5;
                    } else if ((btn[7].getValor() == bola) && (btn[8].getValor() == bola) && (btn[6].getValor() == 0)) {
                        num = 6;
                    } else if ((btn[6].getValor() == bola) && (btn[8].getValor() == bola) && (btn[7].getValor() == 0)) {
                        num = 7;
                    } else if ((btn[6].getValor() == bola) && (btn[7].getValor() == bola) && (btn[8].getValor() == 0)) {
                        num = 8;
                    } else if ((btn[3].getValor() == bola) && (btn[6].getValor() == bola) && (btn[0].getValor() == 0)) {
                        num = 0;
                    } else if ((btn[4].getValor() == bola) && (btn[7].getValor() == bola) && (btn[1].getValor() == 0)) {
                        num = 1;
                    } else if ((btn[5].getValor() == bola) && (btn[8].getValor() == bola) && (btn[2].getValor() == 0)) {
                        num = 2;
                    } else if ((btn[0].getValor() == bola) && (btn[6].getValor() == bola) && (btn[3].getValor() == 0)) {
                        num = 3;
                    } else if ((btn[1].getValor() == bola) && (btn[7].getValor() == bola) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[2].getValor() == bola) && (btn[8].getValor() == bola) && (btn[5].getValor() == 0)) {
                        num = 5;
                    } else if ((btn[0].getValor() == bola) && (btn[3].getValor() == bola) && (btn[6].getValor() == 0)) {
                        num = 6;
                    } else if ((btn[1].getValor() == bola) && (btn[4].getValor() == bola) && (btn[7].getValor() == 0)) {
                        num = 7;
                    } else if ((btn[2].getValor() == bola) && (btn[5].getValor() == bola) && (btn[8].getValor() == 0)) {
                        num = 8;
                    } else if ((btn[4].getValor() == bola) && (btn[8].getValor() == bola) && (btn[0].getValor() == 0)) {
                        num = 0;
                    } else if ((btn[0].getValor() == bola) && (btn[8].getValor() == bola) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[0].getValor() == bola) && (btn[4].getValor() == bola) && (btn[8].getValor() == 0)) {
                        num = 8;
                    } else if ((btn[4].getValor() == bola) && (btn[6].getValor() == bola) && (btn[2].getValor() == 0)) {
                        num = 2;
                    } else if ((btn[2].getValor() == bola) && (btn[6].getValor() == bola) && (btn[4].getValor() == 0)) {
                        num = 4;
                    } else if ((btn[2].getValor() == bola) && (btn[4].getValor() == bola) && (btn[6].getValor() == 0)) {
                        num = 6;
                    }
                }
            }

            btn[num].getBtn().setEnabled(false);
            btn[num].setValor(turno);
            if (turno == cruz) {
                btn[num].getBtn().setImageResource(R.drawable.x_element);
                turno = bola;
            } else {
                btn[num].getBtn().setImageResource(R.drawable.o_element);
                turno = cruz;
            }
            turnText();

            rodada++;
            if (vencedor == 0) {
                verificarFim();
            }
        }

        // Impossible
//        else if(difficulty == 3) {
//            num = minimax();
//
//            btn[num].getBtn().setEnabled(false);
//            btn[num].setValor(turno);
//            if (turno == cruz) {
//                btn[num].getBtn().setImageResource(R.drawable.x_element);
//                turno = bola;
//            } else {
//                btn[num].getBtn().setImageResource(R.drawable.o_element);
//                turno = cruz;
//            }
//            turnText();
//
//            rodada++;
//            if (vencedor == 0) {
//                verificarFim();
//            }
//        }
    }

//    public int minimax() {
//        int aux = 0;
//        int[] vetor = new int[9];
//
//        for(int x = 0; x <= 8; x++) {
//            if (btn[x].getValor() == cruz) {
//                vetor[x] = 1;
//            } else if (btn[x].getValor() == bola) {
//                vetor[x] = 2;
//            } else {
//                vetor[x] = 0;
//            }
//        }
//
//        aux++;
//
//        return aux;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        SingleplayerActivity.this.invalidateOptionsMenu();

        // Inicia a música tema
        if(musicIsPlaying && adIsntOpened) {
            mainmusic.start();
        } else {
            mainmusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SingleplayerActivity.this.invalidateOptionsMenu();

        // Define a posição atual da música tema quando ela foi pausada anteriormente
        if(musicIsPlaying && adIsntOpened) {
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
        builder.setTitle(R.string.action_back);
        builder.setMessage(R.string.action_back_message);
        builder.setPositiveButton(R.string.button_yes_message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    finish();
                }
            }
        });
        builder.setNegativeButton(R.string.button_no_message, null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_singleplayer, menu);
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

        // Back Home
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.action_back);
            builder.setMessage(R.string.action_back_message);
            builder.setPositiveButton(R.string.button_yes_message, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        finish();
                    }
                }
            });
            builder.setNegativeButton(R.string.button_no_message, null);
            builder.show();
            return true;
        }

        // Mute
        if (id == R.id.action_mute) {
            SingleplayerActivity.this.invalidateOptionsMenu();
            mainmusic.pause();
            musicIsPlaying = false;
            if(!mainmusic.isPlaying()) {
                final Toast toast = Toast.makeText(SingleplayerActivity.this, getText(R.string.action_mute), Toast.LENGTH_SHORT);
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
            SingleplayerActivity.this.invalidateOptionsMenu();
            mainmusic.start();
            musicIsPlaying = true;
            if(mainmusic.isPlaying()) {
                final Toast toast = Toast.makeText(SingleplayerActivity.this, getText(R.string.action_unmute), Toast.LENGTH_SHORT);
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

        // Configurações
        if (id == R.id.action_config) {
            showConfig();
            return true;
        }

        // Voltar ao menu
        if (id == R.id.action_back) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.action_back);
            builder.setMessage(R.string.action_back_message);
            builder.setPositiveButton(R.string.button_yes_message, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        finish();
                    }
                }
            });
            builder.setNegativeButton(R.string.button_no_message, null);
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
