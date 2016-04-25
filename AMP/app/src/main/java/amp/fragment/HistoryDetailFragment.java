package amp.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ua.viktor.amp.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.communication.IOnItemFocusChangedListener;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import amp.activity.DetailActivity;
import amp.dao.Choice;
import amp.dao.ChoiceDao;
import amp.dao.DaoMaster;
import amp.dao.DaoSession;
import amp.dao.Question;
import amp.dao.QuestionDao;

/**
 * Created by viktor on 19.10.15.
 */
public class HistoryDetailFragment extends ChartFragment {

    private static final String TAG = HistoryDetailFragment.class.getName();
    private PieChart mPieChart;
    private TextView questionText;
    private TextView sumCountTextView;
    private List<Choice> mChoices;
    private int sumCount;

    public HistoryDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        mPieChart = (PieChart) view.findViewById(R.id.piechart);
        sumCountTextView= (TextView) view.findViewById(R.id.textView5);
        Bundle bundle = getArguments();
        int position = 0;
        if (bundle != null) {
            position = bundle.getInt(DetailActivity.POSI);

        }
        getStatistic(position+"");


        questionText = (TextView) view.findViewById(R.id.textQuestion);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();


        ChoiceDao leaseDao = daoSession.getChoiceDao();
        mChoices = leaseDao._queryQuestion_Question((long) position);


        QuestionDao questionDao = daoSession.getQuestionDao();
        Question question = questionDao.loadByRowId((long) position);

        questionText.setText(question.getText());


        for (Choice ch : mChoices) {
            sumCount += ch.getCount();
        }


        sumCountTextView.setText(sumCount + "");

        loadData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPieChart.startAnimation();
    }

    @Override
    public void restartAnimation() {
        mPieChart.startAnimation();
    }

    @Override
    public void onReset() {

    }
    public void getStatistic(final String id) {
        String movieUrl = "http://192.168.1.6:3000/questions/statistics/" + id;
        if (isNetworkOn(getActivity())) {
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
            Toast.makeText(getActivity(), "Network isn't available", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkOn(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void updateQuestionData(String jsonData, String id) throws JSONException {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "lease-db", null);
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


    private void loadData() {


        for (int i = 0; i < mChoices.size(); i++) {

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getRandomColor();
            double procent = (((double) mChoices.get(i).getCount()) / sumCount) * 100.0;
            Log.v("Count", mChoices.get(i).getCount() + "");
            Log.v("sumCount", sumCount + "");
            Log.v("procent", procent + "");
            mPieChart.addPieSlice(new PieModel(mChoices.get(i).getItem(), (float) procent, color));
        }
        mPieChart.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
            @Override
            public void onItemFocusChanged(int _Position) {
                // Log.v("PieChart", "Position: " + _Position);
            }
        });
    }

}



