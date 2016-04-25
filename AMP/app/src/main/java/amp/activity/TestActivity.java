package amp.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.ua.viktor.amp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import amp.adapter.TestItemAdapter;
import amp.dao.Choice;
import amp.dao.ChoiceDao;
import amp.dao.DaoMaster;
import amp.dao.DaoSession;
import amp.dao.Question;
import amp.dao.QuestionDao;
import amp.fragment.HistoryFragment;
import amp.fragment.HomeFragment;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = TestActivity.class.getName();
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView questionText;
    public static String POSITION = HistoryFragment.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        questionText = (TextView) findViewById(R.id.textView3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbart);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        ChoiceDao leaseDao = daoSession.getChoiceDao();
        final String position = intent.getStringExtra(HomeFragment.TAG);
        List<Choice> choiceList = leaseDao._queryQuestion_Question(Long.parseLong(position));

        QuestionDao questionDao = daoSession.getQuestionDao();
        Question question = questionDao.loadByRowId(Long.parseLong(position));


        questionText.setText(question.getText());


        TestItemAdapter testItemAdapter = new TestItemAdapter(choiceList);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getApplication(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(testItemAdapter);
        testItemAdapter.SetOnClickListener(new TestItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemPosition) {
                //view.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
                // view.setClickable(false);

                postAnswer(position, "" + itemPosition);
                Log.v("position", "" + position);
                Intent intent = new Intent(getApplication(), DetailActivity.class);
                intent.putExtra(POSITION, Integer.parseInt(position));
                startActivity(intent);
            }
        });

    }

    public void postAnswer(final String question_id, String choice_id) {

        if (isNetworkOn(getApplicationContext())) {
            OkHttpClient client = new OkHttpClient();

            String requestBinURL = "http://192.168.1.6:3000/answers";
            String dataToPost = "{\"question_id\": \"" + question_id + "\", \"choice_id\": \"" + choice_id + "\"}";

            Log.v(TAG, dataToPost);

            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, dataToPost);
            Request request = new Request.Builder()
                    .url(requestBinURL)
                    .post(requestBody)
                    .build();
            client.networkInterceptors().add(new StethoInterceptor());
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(TAG, "(onFailure) The request was not successful");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {
                            Log.v(TAG, "Was successful!");
                            getStatistic(question_id);
                        } else {
                            Log.v(TAG, "(onResponse) Was not successful");
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }
            });
            Log.d(TAG, "Main UI code is running");
        }
    }

    public void getStatistic(final String id) {
        String movieUrl = "http://192.168.1.6:3000/questions/statistics/" + id;
        if (isNetworkOn(getApplicationContext())) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(movieUrl)
                    .addHeader("Accept", "application/json")
                    .build();
            client.networkInterceptors().add(new StethoInterceptor());
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                             @Override
                             public void onFailure(Request request, IOException e) {

                             }

                             @Override
                             public void onResponse(Response response) throws IOException {
                                 try {
                                     if (response.isSuccessful()) {
                                         final String json = response.body().string();
                                         if (!json.isEmpty()) {
                                             updateQuestionData(json, id);
                                         }

                                     }
                                 } catch (IOException e) {
                                     //   Log.e(TAG, "EROR", e);
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }


                             }
                         }

            );
        } else {
            Toast.makeText(getApplicationContext(), "Network isn't available", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkOn(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void updateQuestionData(String jsonData, String id) throws JSONException {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplication(), "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        ChoiceDao leaseDao = daoSession.getChoiceDao();

        JSONArray jsonObject = new JSONArray(jsonData);


        ArrayList<Choice> notes = new ArrayList<Choice>();

        for (int i = 0; i < jsonObject.length(); i++) {
            JSONObject jsonChoices = jsonObject.getJSONObject(i);
            List<Choice> ch = leaseDao.queryBuilder().where(ChoiceDao.Properties.ID.eq(jsonChoices.getInt("choice_id")), ChoiceDao.Properties.QuestionId.eq(id)).limit(1).list();
            Choice choice = ch.get(0);
            choice.setCount(jsonChoices.getInt("count"));
            notes.add(choice);
        }

        ChoiceDao choiceDao = daoSession.getChoiceDao();
        choiceDao.updateInTx(notes);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // savedInstanceState.putString(STATE_USER, mUser);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
