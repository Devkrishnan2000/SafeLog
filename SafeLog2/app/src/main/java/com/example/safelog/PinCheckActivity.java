package com.example.safelog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import net.sqlcipher.database.SQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PinCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isfirstrun = getSharedPreferences("FIRSTRUN",MODE_PRIVATE).getBoolean("isfirstrun",true);
        if(isfirstrun) {
            startActivity(new Intent(PinCheckActivity.this, FirstActivity.class));
            finish();
        }

        setContentView(R.layout.activity_pin_check);
        SQLiteDatabase.loadLibs(this);

        AppCompatButton unlock_btn = findViewById(R.id.unlockbtn);
        EditText passwrd = findViewById(R.id.pin_input);
        passwrd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        unlock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyclass.setKey(passwrd.getText().toString());
                DBClass db = new DBClass(PinCheckActivity.this);
                Boolean check= db.checkpasword();

                if(check) {
                    Keyclass.setKey(passwrd.getText().toString());
                    Toast.makeText(PinCheckActivity.this, "Password Correct", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(PinCheckActivity.this,MainActivity.class));
                     finish();
                }
                else
                {
                    Toast.makeText(PinCheckActivity.this,"Password Incorrect",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}