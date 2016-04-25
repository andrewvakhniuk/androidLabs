package amp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ua.viktor.amp.R;

import amp.fragment.HistoryDetailFragment;
import amp.fragment.HistoryFragment;

public class DetailActivity extends AppCompatActivity {

    private Fragment mCurrentFragment;
    public static String POSI = DetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbart3);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int position = intent.getIntExtra(HistoryFragment.POSITION, 0);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mCurrentFragment = new HistoryDetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(POSI, position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCurrentFragment.setArguments(arg);
        fragmentManager.beginTransaction()
                .replace(R.id.container, mCurrentFragment)
                .commit();
    }
}
