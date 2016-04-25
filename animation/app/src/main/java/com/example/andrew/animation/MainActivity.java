package com.example.andrew.animation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {
    private Animation animOne, animTwo, animThree;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        animOne = AnimationUtils.loadAnimation(this, R.anim.animation_one);
       animTwo = AnimationUtils.loadAnimation(this, R.anim.anumation_two);
        animThree = AnimationUtils.loadAnimation(this, R.anim.animation_three);
    }

    public void onAnimationImageOne(View v){
        imageView.startAnimation(animOne);
    }
    public void onAnimationImageTwo(View v){
        imageView.startAnimation(animTwo);
    }

    // Анимация №3
    public void onAnimationImageThree(View v){
        imageView.startAnimation(animThree);
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
