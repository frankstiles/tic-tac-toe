package com.example.frankstiles.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class FiveByFive extends AppCompatActivity {
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseResource();
        }
    };
    //Request Media Player
    private MediaPlayer mediaPlayer;
    private int playerOneScore, playerTwoScore , draws = 0;
    private int playTurn = 0;

    //private boolean hasSound;
    String playMode;
    String compChar;
    String playerOneChar;
    String playerTwoChar;

    private TextView p1_score_text, p2_score_text, draw_text;
    private Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_10,
            btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,btn_17,btn_18,btn_19,
            btn_20,btn_21,btn_22,btn_23,btn_24,btn_25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_by_five);
        playMode = getIntent().getExtras().getString("playType");
        compChar = getIntent().getExtras().getString("playerTwoChar");
        playerOneChar = getIntent().getExtras().getString("playerOneChar");
        playerTwoChar = getIntent().getExtras().getString("playerTwoChar");


        btn_1 =  findViewById(R.id.btn_1);   btn_2 =  findViewById(R.id.btn_2);
        btn_3 =  findViewById(R.id.btn_3);   btn_4 =  findViewById(R.id.btn_4);
        btn_5 =  findViewById(R.id.btn_5);   btn_6 =  findViewById(R.id.btn_6);
        btn_7 =  findViewById(R.id.btn_7);   btn_8 =  findViewById(R.id.btn_8);
        btn_9 =  findViewById(R.id.btn_9);   btn_10 =  findViewById(R.id.btn_10);
        btn_11 =  findViewById(R.id.btn_11); btn_12 =  findViewById(R.id.btn_12);
        btn_13 =  findViewById(R.id.btn_13); btn_14 =  findViewById(R.id.btn_14);
        btn_15 =  findViewById(R.id.btn_15); btn_16 =  findViewById(R.id.btn_16);
        btn_17 =  findViewById(R.id.btn_17);   btn_18 =  findViewById(R.id.btn_18);
        btn_19 =  findViewById(R.id.btn_19); btn_20 =  findViewById(R.id.btn_20);
        btn_21 =  findViewById(R.id.btn_21); btn_22 =  findViewById(R.id.btn_22);
        btn_23 =  findViewById(R.id.btn_23); btn_24 =  findViewById(R.id.btn_24);
        btn_25 =  findViewById(R.id.btn_25);

        Button btn_play =  findViewById(R.id.btn_play);
        Button btn_reset = findViewById(R.id.btn_reset);
        Button btn_main = findViewById(R.id.btn_main);


        p1_score_text =  findViewById(R.id.player_one_score);
        p2_score_text =  findViewById(R.id.player_two_score);
        draw_text     =  findViewById(R.id.draws);

        //Determines Who Starts
        switchPlayersTurn();
        /*
         * OnClick Listeners are Being set Here......
         **/
        setBtnListeners(btn_1);setBtnListeners(btn_2);
        setBtnListeners(btn_3);setBtnListeners(btn_4);
        setBtnListeners(btn_5);setBtnListeners(btn_6);
        setBtnListeners(btn_7);setBtnListeners(btn_8);
        setBtnListeners(btn_9);setBtnListeners(btn_10);
        setBtnListeners(btn_11);setBtnListeners(btn_12);
        setBtnListeners(btn_13);setBtnListeners(btn_14);
        setBtnListeners(btn_15);setBtnListeners(btn_16);
        setBtnListeners(btn_17);setBtnListeners(btn_18);
        setBtnListeners(btn_19);setBtnListeners(btn_20);
        setBtnListeners(btn_21);setBtnListeners(btn_22);
        setBtnListeners(btn_23);setBtnListeners(btn_24);
        setBtnListeners(btn_25);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPlayBoard();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetScore();
            }
        });
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(FiveByFive.this,MainActivity.class);
                startActivity(I);
            }
        });
    }

    /*
     * @param Function Accepts a Btn.. Then Set the
     * Specified Listener on that Button
     * */
    public void setBtnListeners(Button btns){
        final Button btn = btns;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playMode.equals("single")){
                    btn.setText(getPlayerOneChar());
                    computerAI();
                    btn.setEnabled(false);
                }
                //---->Multiple Player mode Handles Player 1 and Two
                if (playMode.equals("multiple")){
                    if(playTurn == 0){
                        btn.setText(getPlayerOneChar());
                        playTurn =1;
                        enableSound(R.raw.playerone);
                        checkPlayerWinAndPaint();
                        if(!scoreBoard()) displayMsg("Player Twos Turn");
                        btn.setEnabled(false);

                    }else if(playTurn == 1){
                        btn.setText(getPlayerTwoChar());
                        playTurn = 0;
                        enableSound(R.raw.playertwo);
                        checkCompWinAndPaint();
                        if(!scoreBoard()) displayMsg("Player One Turn");
                        btn.setEnabled(false);
                    }else{}
                }
            }
        });
    }
    /*
     * @param accepts a resource Audio File
     * */
    public void enableSound(int resourceId){
        boolean hasSound = getIntent().getExtras().getBoolean("hasSound");
        if(hasSound){
            releaseResource();
            mediaPlayer = MediaPlayer.create(FiveByFive.this, resourceId);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(completionListener);
        }
    }

    /**
     * This Part is responsible for computer Intelligence
     * First Computer Tries to Find a Way to Win if False it Proceeds and Tries
     * to Block the Opponent if he wasnt successful....
     * it tries to Check For Available Spot.....
     * */
    public void computerAI(){
        if(playMode.equals("single")){

            if(!createWinningChances()){
                if(!blockWinningChances()){

                    checkAvailableMove();
                }
                createWinningChances();
            }
            scoreBoard();
        }
    }


    /*
     * @ Function ... Checks For Win || Lose || Draw if Found any Executes and returns True
     * Else it returns False... Note This function is to be place at the End of Comp AI
     * */
    public boolean scoreBoard(){
        if(checkPlayerWinAndPaint()){
            playerOneScore += 1;
            p1_score_text.setText((Integer.toString(playerOneScore)));
            enableSound(R.raw.summary);
            displayMsg("You Win!!!");
            disableAllBtn();
            return true;
        }

        if(checkCompWinAndPaint()){
            playerTwoScore += 1;
            p2_score_text.setText((Integer.toString(playerTwoScore)));
            enableSound(R.raw.summary);
            displayMsg("You Lose!!");
            disableAllBtn();
            return true;
        }

        if(checkForDraw()){
            draws += 1;
            draw_text.setText((Integer.toString(draws)));
            enableSound(R.raw.summary);
            displayMsg("Its a Draw!!");
            disableAllBtn();
            return true;
        }
        return  false;
    }

    /**
     * Switches Turns... Determines who plays first at the beginning of the program...
     * */
    public void switchPlayersTurn(){
        int PlayersTurn = selectPlayerAtRand(2);
        switch(PlayersTurn){
            case 0:
                if(playMode.equals("single")){displayMsg("PlayerOne Plays First");}
                if(playMode.equals("multiple")){displayMsg("PlayerOne Plays First");}

                break;
            case 1:
                if(playMode.equals("single")){displayMsg("Player Two Plays First");computerAI();}
                if(playMode.equals("multiple")){displayMsg("Player Two Plays First");}
                break;
            default:
        }
    }


    public boolean checkPlayerWin( ){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        if(moves[0][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[0][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[0][2].getText().toString().equals(getPlayerOneChar())  &&
                moves[0][3].getText().toString().equals(getPlayerOneChar())  &&
                moves[0][4].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[1][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[1][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[1][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[1][4].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[2][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[2][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][4].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[3][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[3][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][4].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[4][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[4][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][4].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[0][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][0].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][0].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][0].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][0].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[0][1].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][1].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[0][2].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][2].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[0][3].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][3].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[0][4].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][4].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][4].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][4].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][4].getText().toString().equals(getPlayerOneChar()))return true;
        if(moves[0][4].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][0].getText().toString().equals(getPlayerOneChar()))return true;

        if(moves[0][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][4].getText().toString().equals(getPlayerOneChar()))return true;

        return false;

    }

    public boolean checkCompWin( ){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        if(moves[0][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[0][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[0][2].getText().toString().equals(getPlayerTwoChar())  &&
                moves[0][3].getText().toString().equals(getPlayerTwoChar())  &&
                moves[0][4].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[1][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[1][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[1][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[1][4].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[2][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[2][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][4].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[3][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[3][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][4].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[4][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[4][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][4].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[0][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][0].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][0].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][0].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][0].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[0][1].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][1].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[0][2].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][2].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[0][3].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][3].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[0][4].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][4].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][4].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][4].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][4].getText().toString().equals(getPlayerTwoChar()))return true;
        if(moves[0][4].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][0].getText().toString().equals(getPlayerTwoChar()))return true;

        if(moves[0][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][4].getText().toString().equals(getPlayerTwoChar()))return true;

        return false;
    }

   /* moves[0][0].setBackgroundColor(color);
    moves[0][1].setBackgroundColor(color);
    moves[0][2].setBackgroundColor(color);
    moves[0][3].setBackgroundColor(color); return true;}*/

    public boolean checkPlayerWinAndPaint( ){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        int color = ContextCompat.getColor(this,R.color.category_beginner);

        if(moves[0][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[0][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[0][2].getText().toString().equals(getPlayerOneChar())  &&
                moves[0][3].getText().toString().equals(getPlayerOneChar())  &&
                moves[0][4].getText().toString().equals(getPlayerOneChar())){

                moves[0][0].setBackgroundColor(color);
                moves[0][1].setBackgroundColor(color);
                moves[0][2].setBackgroundColor(color);
                moves[0][3].setBackgroundColor(color);
                moves[0][4].setBackgroundColor(color) ;return true;
        }
        if(moves[1][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[1][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[1][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[1][4].getText().toString().equals(getPlayerOneChar())) {

            moves[1][0].setBackgroundColor(color);
            moves[1][1].setBackgroundColor(color);
            moves[1][2].setBackgroundColor(color);
            moves[1][3].setBackgroundColor(color);
            moves[1][4].setBackgroundColor(color);
            return true;
        }
        if(moves[2][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[2][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][4].getText().toString().equals(getPlayerOneChar())){

                moves[2][0].setBackgroundColor(color);
                moves[2][1].setBackgroundColor(color);
                moves[2][2].setBackgroundColor(color);
                moves[2][3].setBackgroundColor(color);
                moves[2][4].setBackgroundColor(color);
                return true;
        }
        if(moves[3][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[3][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][4].getText().toString().equals(getPlayerOneChar())){

                moves[3][0].setBackgroundColor(color);
                moves[3][1].setBackgroundColor(color);
                moves[3][2].setBackgroundColor(color);
                moves[3][3].setBackgroundColor(color);
                moves[3][4].setBackgroundColor(color);
                return true;
        }
        if(moves[4][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[4][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][4].getText().toString().equals(getPlayerOneChar())){

                moves[4][0].setBackgroundColor(color);
                moves[4][1].setBackgroundColor(color);
                moves[4][2].setBackgroundColor(color);
                moves[4][3].setBackgroundColor(color);
                moves[4][4].setBackgroundColor(color);
                return true;
        }
        if(moves[0][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][0].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][0].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][0].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][0].getText().toString().equals(getPlayerOneChar())){

                moves[0][0].setBackgroundColor(color);
                moves[1][0].setBackgroundColor(color);
                moves[2][0].setBackgroundColor(color);
                moves[3][0].setBackgroundColor(color);
                moves[4][0].setBackgroundColor(color);
                return true;
        }
        if(moves[0][1].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][1].getText().toString().equals(getPlayerOneChar())){

                moves[0][1].setBackgroundColor(color);
                moves[1][1].setBackgroundColor(color);
                moves[2][1].setBackgroundColor(color);
                moves[3][1].setBackgroundColor(color);
                moves[4][1].setBackgroundColor(color);
                return true;
            }
        if(moves[0][2].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][2].getText().toString().equals(getPlayerOneChar())){

                moves[0][2].setBackgroundColor(color);
                moves[1][2].setBackgroundColor(color);
                moves[2][2].setBackgroundColor(color);
                moves[3][2].setBackgroundColor(color);
                moves[4][2].setBackgroundColor(color);
                return true;
        }
        if(moves[0][3].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][3].getText().toString().equals(getPlayerOneChar())){

                moves[0][3].setBackgroundColor(color);
                moves[1][3].setBackgroundColor(color);
                moves[2][3].setBackgroundColor(color);
                moves[3][3].setBackgroundColor(color);
                moves[4][3].setBackgroundColor(color);
                return true;
        }
        if(moves[0][4].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][4].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][4].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][4].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][4].getText().toString().equals(getPlayerOneChar())){

                moves[0][4].setBackgroundColor(color);
                moves[1][4].setBackgroundColor(color);
                moves[2][4].setBackgroundColor(color);
                moves[3][4].setBackgroundColor(color);
                moves[4][4].setBackgroundColor(color);
                return true;
            }
        if(moves[0][4].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][0].getText().toString().equals(getPlayerOneChar())){

                moves[0][4].setBackgroundColor(color);
                moves[1][3].setBackgroundColor(color);
                moves[2][2].setBackgroundColor(color);
                moves[3][1].setBackgroundColor(color);
                moves[4][0].setBackgroundColor(color);
                return true;
        }

        if(moves[0][0].getText().toString().equals(getPlayerOneChar())      &&
                moves[1][1].getText().toString().equals(getPlayerOneChar()) &&
                moves[2][2].getText().toString().equals(getPlayerOneChar()) &&
                moves[3][3].getText().toString().equals(getPlayerOneChar()) &&
                moves[4][4].getText().toString().equals(getPlayerOneChar())){

                moves[0][0].setBackgroundColor(color);
                moves[1][1].setBackgroundColor(color);
                moves[2][2].setBackgroundColor(color);
                moves[3][3].setBackgroundColor(color);
                moves[4][4].setBackgroundColor(color);
                return true;
        }

        return false;
    }


    public boolean checkCompWinAndPaint( ){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        int color = ContextCompat.getColor(this,R.color.category_advanced);

        if(moves[0][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[0][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[0][2].getText().toString().equals(getPlayerTwoChar())  &&
                moves[0][3].getText().toString().equals(getPlayerTwoChar())  &&
                moves[0][4].getText().toString().equals(getPlayerTwoChar())){

            moves[0][0].setBackgroundColor(color);
            moves[0][1].setBackgroundColor(color);
            moves[0][2].setBackgroundColor(color);
            moves[0][3].setBackgroundColor(color);
            moves[0][4].setBackgroundColor(color) ;return true;
        }
        if(moves[1][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[1][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[1][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[1][4].getText().toString().equals(getPlayerTwoChar())) {

            moves[1][0].setBackgroundColor(color);
            moves[1][1].setBackgroundColor(color);
            moves[1][2].setBackgroundColor(color);
            moves[1][3].setBackgroundColor(color);
            moves[1][4].setBackgroundColor(color);
            return true;
        }
        if(moves[2][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[2][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][4].getText().toString().equals(getPlayerTwoChar())){

            moves[2][0].setBackgroundColor(color);
            moves[2][1].setBackgroundColor(color);
            moves[2][2].setBackgroundColor(color);
            moves[2][3].setBackgroundColor(color);
            moves[2][4].setBackgroundColor(color);
            return true;
        }
        if(moves[3][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[3][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][4].getText().toString().equals(getPlayerTwoChar())){

            moves[3][0].setBackgroundColor(color);
            moves[3][1].setBackgroundColor(color);
            moves[3][2].setBackgroundColor(color);
            moves[3][3].setBackgroundColor(color);
            moves[3][4].setBackgroundColor(color);
            return true;
        }
        if(moves[4][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[4][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][4].getText().toString().equals(getPlayerTwoChar())){

            moves[4][0].setBackgroundColor(color);
            moves[4][1].setBackgroundColor(color);
            moves[4][2].setBackgroundColor(color);
            moves[4][3].setBackgroundColor(color);
            moves[4][4].setBackgroundColor(color);
            return true;
        }
        if(moves[0][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][0].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][0].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][0].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][0].getText().toString().equals(getPlayerTwoChar())){

            moves[0][0].setBackgroundColor(color);
            moves[1][0].setBackgroundColor(color);
            moves[2][0].setBackgroundColor(color);
            moves[3][0].setBackgroundColor(color);
            moves[4][0].setBackgroundColor(color);
            return true;
        }
        if(moves[0][1].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][1].getText().toString().equals(getPlayerTwoChar())){

            moves[0][1].setBackgroundColor(color);
            moves[1][1].setBackgroundColor(color);
            moves[2][1].setBackgroundColor(color);
            moves[3][1].setBackgroundColor(color);
            moves[4][1].setBackgroundColor(color);
            return true;
        }
        if(moves[0][2].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][2].getText().toString().equals(getPlayerTwoChar())){

            moves[0][2].setBackgroundColor(color);
            moves[1][2].setBackgroundColor(color);
            moves[2][2].setBackgroundColor(color);
            moves[3][2].setBackgroundColor(color);
            moves[4][2].setBackgroundColor(color);
            return true;
        }
        if(moves[0][3].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][3].getText().toString().equals(getPlayerTwoChar())){

            moves[0][3].setBackgroundColor(color);
            moves[1][3].setBackgroundColor(color);
            moves[2][3].setBackgroundColor(color);
            moves[3][3].setBackgroundColor(color);
            moves[4][3].setBackgroundColor(color);
            return true;
        }
        if(moves[0][4].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][4].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][4].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][4].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][4].getText().toString().equals(getPlayerTwoChar())){

            moves[0][4].setBackgroundColor(color);
            moves[1][4].setBackgroundColor(color);
            moves[2][4].setBackgroundColor(color);
            moves[3][4].setBackgroundColor(color);
            moves[4][4].setBackgroundColor(color);
            return true;
        }
        if(moves[0][4].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][0].getText().toString().equals(getPlayerTwoChar())){

            moves[0][4].setBackgroundColor(color);
            moves[1][3].setBackgroundColor(color);
            moves[2][2].setBackgroundColor(color);
            moves[3][1].setBackgroundColor(color);
            moves[4][0].setBackgroundColor(color);
            return true;
        }

        if(moves[0][0].getText().toString().equals(getPlayerTwoChar())      &&
                moves[1][1].getText().toString().equals(getPlayerTwoChar()) &&
                moves[2][2].getText().toString().equals(getPlayerTwoChar()) &&
                moves[3][3].getText().toString().equals(getPlayerTwoChar()) &&
                moves[4][4].getText().toString().equals(getPlayerTwoChar())){

            moves[0][0].setBackgroundColor(color);
            moves[1][1].setBackgroundColor(color);
            moves[2][2].setBackgroundColor(color);
            moves[3][3].setBackgroundColor(color);
            moves[4][4].setBackgroundColor(color);
            return true;
        }

        return false;
    }




    //Helps Computer In Figure out Available Play Space before making a move
    public boolean checkAvailableMove() {
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        for(int row=0; row < moves.length; row++) {
            for (int col = 0; col < moves[row].length; col++) {
                if (moves[row][col].getText().toString().equals("")) {
                    moves[row][col].setText(getCompChar());
                    enableSound(R.raw.playertwo);
                    moves[row][col].setEnabled(false);
                    return true;
                }
            }
        }
        return false;
    }


    //Checks if there is a draw|| Tie
    public boolean checkForDraw(){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        for(int row=0; row < moves.length; row++){
            for(int col=0; col < moves[row].length; col++){
                if(moves[row][col].getText().toString().equals("")){
                    return false;
                }
            }
        }
        return true;
    }


    //Computer Blocks Every Winning Chance of Player..............
    public boolean blockWinningChances(){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        for(int row=0; row < moves.length; row++){
            for(int col=0; col < moves[row].length; col++){
                //First Fills the whole board with opponent Character as long as space is availabe
                if(moves[row][col].getText().toString().equals("")){
                    moves[row][col].setText(getPlayerOneChar());
                    //Checks if there is any winning Spot among the filled space then blocks with opposite Character
                    if(checkPlayerWin()){
                        moves[row][col].setText(getCompChar());
                        enableSound(R.raw.playertwo);
                        moves[row][col].setEnabled(false);
                        return true;
                    }
                    //sets space back to null
                    moves[row][col].setText("");
                }
            }
        }

        return false;
    }

    public boolean createWinningChances(){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        for(int row=0; row < moves.length; row++){
            for(int col=0; col < moves[row].length; col++){

                if(moves[row][col].getText().toString().equals("")){
                    moves[row][col].setText(getCompChar());

                    if(checkPlayerWin()){
                        moves[row][col].setText(getCompChar());
                        enableSound(R.raw.playertwo);
                        moves[row][col].setEnabled(false);
                        return true;
                    }
                    moves[row][col].setText("");
                }

            }
        }

        return false;
    }

    //Displays messsage to the User.........
    public void displayMsg(String Text){
        Toast.makeText(FiveByFive.this, Text, Toast.LENGTH_SHORT).show();
    }


    public void resetPlayBoard(){

        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        for(int row=0; row < moves.length; row++){
            for(int col=0; col < moves[row].length; col++){
                moves[row][col].setEnabled(true);
                moves[row][col].setText("");
                moves[row][col].setBackgroundColor(getResources().getColor(R.color.default_btn_color));
            }
        }
    }


    public void resetScore(){
        playerOneScore = 0;
        playerTwoScore = 0;
        draws = 0;

        p1_score_text.setText((Integer.toString(playerOneScore)));
        p2_score_text.setText((Integer.toString(playerTwoScore)));
        draw_text.setText((Integer.toString(draws)));
    }


    public void disableAllBtn(){
        Button[][] moves = {{btn_1,btn_2,btn_3,btn_4,btn_5},{btn_6,btn_7,btn_8,btn_9,btn_10},
                {btn_11,btn_12, btn_13,btn_14,btn_15},{btn_16,btn_17,btn_18,btn_19,btn_20},
                {btn_21,btn_22,btn_23,btn_24,btn_25}};

        for(int row=0; row < moves.length; row++){
            for(int col=0; col < moves[row].length; col++){
                moves[row][col].setEnabled(false);
            }
        }
    }


    // Get PlayerOne Character...
    public String getPlayerOneChar(){
        return playerOneChar;
    }

    // Get Computers Character...
    public String getCompChar(){
        return compChar;
    }

    // Get PlayerTwo Character...
    public String getPlayerTwoChar() {
        return playerTwoChar;
    }

    public void releaseResource(){
        if(this.mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }

    //Determines who plays First
    public int selectPlayerAtRand(int bound){
        Random whoPlaysAtRand = new Random();
        playTurn =  whoPlaysAtRand.nextInt(bound);
        return playTurn;
    }

}
