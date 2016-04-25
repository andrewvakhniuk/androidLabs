package com.example.andrew.myapplication4;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
Button button;
    public static final int PICK_CONTACT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                readcontact();
            }
        });
    }

    public void readcontact(){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
      if(reqCode==PICK_CONTACT){
         if(resultCode==RESULT_OK)
         {
             Uri contactUri=data.getData();
             String [] projection={ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

             Cursor cursor=getContentResolver().query(contactUri,projection,null,null,null);
             cursor.moveToFirst();
             int column=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
             int column1=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
             String number=cursor.getString(column);
             String n=cursor.getString(column1);
             Toast.makeText(this,n+"has number " + number, Toast.LENGTH_LONG).show();
         }
      }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
