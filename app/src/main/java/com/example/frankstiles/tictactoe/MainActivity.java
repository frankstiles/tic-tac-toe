package com.example.frankstiles.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Troversing Through Views!! Getting Specific View By ID*/
        RelativeLayout b_category = (RelativeLayout) findViewById(R.id.beginner);
        RelativeLayout i_category = (RelativeLayout) findViewById(R.id.intermediate);
        RelativeLayout a_category = (RelativeLayout) findViewById(R.id.advanced);

       final CheckBox enable_sound = (CheckBox) findViewById(R.id.checkbox_sound);


/*Setting OnClick Listenner after Troversing Through View, Explicit Intent was used move to a
        different activity*/
        b_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasCheckSound = enable_sound.isChecked();
                Intent I = new Intent(MainActivity.this,SelectPlayer.class);
                I.putExtra("mode","beginner");
                I.putExtra("hasSound",hasCheckSound);
                startActivity(I);

            }
        });
        i_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasCheckSound = enable_sound.isChecked();
                Intent I = new Intent(MainActivity.this,SelectPlayer.class);
                I.putExtra("mode","intermediate");
                I.putExtra("hasSound",hasCheckSound);
                startActivity(I);
            }
        });
        a_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasCheckSound = enable_sound.isChecked();
                Intent I = new Intent(MainActivity.this,SelectPlayer.class);
                I.putExtra("mode","advanced");
                I.putExtra("hasSound",hasCheckSound);
                startActivity(I);
            }
        });
    }

}
