package amp.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.viktor.amp.R;

import java.util.List;

import amp.activity.DetailActivity;
import amp.adapter.HistoryAdapter;
import amp.dao.DaoMaster;
import amp.dao.DaoSession;
import amp.dao.Question;
import amp.dao.QuestionDao;


public class HistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static String POSITION = HistoryFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "lease-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        QuestionDao leaseDao = daoSession.getQuestionDao();
        List<Question> questionsList = leaseDao.loadAll();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        HistoryAdapter historyAdapter = new HistoryAdapter(questionsList);
        mRecyclerView.setAdapter(historyAdapter);
        historyAdapter.SetOnClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //view.setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
                Intent intent = new Intent(getActivity(), DetailActivity.class);
            //    historyAdapter.getText().toString()
                intent.putExtra(POSITION, position);
                startActivity(intent);

                // view.setClickable(false);
            }
        });

        return view;
    }

}
