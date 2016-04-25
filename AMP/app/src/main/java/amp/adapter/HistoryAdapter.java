package amp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.viktor.amp.R;

import java.util.List;

import amp.activity.MainActivity;
import amp.dao.Question;

/**
 * Created by viktor on 12.10.15.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Question> mData;
    OnItemClickListener mOnClickListener;
    private MainActivity main;

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, viewGroup.getContext());
        return viewHolder;
    }

    public HistoryAdapter(List<Question> questions) {

        this.mData = questions;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.questionText.setText(mData.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView questionText;
        Context contxt;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            contxt = context;
            questionText = (TextView) itemView.findViewById(R.id.question);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, mData.get(getLayoutPosition()).getID());
            }
        }
    }
}

