package com.example.frankstiles.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class SelectPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);


        //Traversing through Views to find wanted Button
        ImageView imgSingle =    findViewById(R.id.img_single_player);
        ImageView imgMultiple =  findViewById(R.id.img_multiple_player);

       imgSingle.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                //Troversing through Views to find wanted radio Button
                RadioButton radioBtnX = (RadioButton) findViewById(R.id.radio_x);
                RadioButton radioBtnY = (RadioButton) findViewById(R.id.radio_o);

                //returns true|| false if radio button is checked
                boolean hasCheckedX = radioBtnX.isChecked();
                boolean hasCheckedY = radioBtnY.isChecked();

                String mode =  getIntent().getExtras().getString("mode");
                boolean hasSoundFromMenu = getIntent().getExtras().getBoolean("hasSound");
                validateAndRedirect( mode, hasCheckedX, hasCheckedY, hasSoundFromMenu,"single");
           }
        });

        imgMultiple.setOnClickListener(new View.OnClickListener() {
             @Override
               public void onClick(View view) {
                    //Troversing through Views to find wanted radio Button
                    RadioButton radioBtnX = (RadioButton) findViewById(R.id.radio_x);
                    RadioButton radioBtnO = (RadioButton) findViewById(R.id.radio_o);

                    //returns true|| false if radio button is checked
                    boolean hasCheckedX = radioBtnX.isChecked();
                    boolean hasCheckedY = radioBtnO.isChecked();

                    String mode =  getIntent().getExtras().getString("mode");
                    boolean hasSoundFromMenu = getIntent().getExtras().getBoolean("hasSound");
                 validateAndRedirect( mode, hasCheckedX, hasCheckedY, hasSoundFromMenu,"multiple");
               }
            });
    }

    public void displayMessage(String msg){
        Toast.makeText(SelectPlayer.this, msg, Toast.LENGTH_SHORT).show();
    }


    /*@param mode {selected mode passed through via Intent in MainActivity
    @playValue either Single/Multiple
    @param sound {checks if the user selected sound passed through via Intent in MainActivity
    @param radioX, radioY are two radioButton States with Boolean Data type
    @function--> validates Users choice via two Radio Button,sound and mode then redirects to appriopriate play dash board
     */
    private void validateAndRedirect(String mode,boolean radioX, boolean radioO,boolean hasSound, String playValue){
        switch (mode){
            case "beginner":
                if(radioX){
                    //redirect user to 3/3 activity
                    Intent I = new Intent(SelectPlayer.this,ThreeByThree.class);
                    I.putExtra("playerOneChar","X");
                    I.putExtra("playerTwoChar","O");
                    I.putExtra("hasSound", hasSound);
                    I.putExtra( "playType", playValue);
                    startActivity(I);
                }else if(radioO){
                    //redirect user to 3/3 activity
                    Intent I = new Intent(SelectPlayer.this,ThreeByThree.class);
                    I.putExtra("playerOneChar","O");
                    I.putExtra("playerTwoChar","X");
                    I.putExtra("hasSound", hasSound);
                    I.putExtra( "playType", playValue);
                    startActivity(I);
                }else{
                    displayMessage("Please Select a Character to proceed");
                }

                break;

            case "intermediate":
                if(radioX){
                    //redirect user to 4/4 activity
                    Intent I = new Intent(SelectPlayer.this,FourByFour.class);
                    I.putExtra("playerOneChar","X");
                    I.putExtra("playerTwoChar","O");
                    I.putExtra("hasSound", hasSound);
                    I.putExtra( "playType", playValue);
                    startActivity(I);
                }else if(radioO){
                    //redirect user to 4/4 activity
                    Intent I = new Intent(SelectPlayer.this,FourByFour.class);
                    I.putExtra("playerOneChar","O");
                    I.putExtra("playerTwoChar","X");
                    I.putExtra("hasSound", hasSound);
                    I.putExtra( "playType", playValue);
                    startActivity(I);
                }else{
                    displayMessage("Please Select a Character to proceed");
                }
                break;
            case "advanced":
                if(radioX){
                    //redirect user to 5/5 activity
                    Intent I = new Intent(SelectPlayer.this,FiveByFive.class);
                    I.putExtra("playerOneChar","X");
                    I.putExtra("playerTwoChar","O");
                    I.putExtra("hasSound", hasSound);
                    I.putExtra( "playType", playValue);
                    startActivity(I);
                }else if(radioO){
                    //redirect user to 5/5 activity
                    Intent I = new Intent(SelectPlayer.this,FiveByFive.class);
                    I.putExtra("playerOneChar","O");
                    I.putExtra("playerTwoChar","X");
                    I.putExtra("hasSound", hasSound);
                    I.putExtra( "playType", playValue);
                    startActivity(I);
                }else{
                    displayMessage("Please Select a Character to proceed");
                }
                break;
            default:
        }
    }

}
