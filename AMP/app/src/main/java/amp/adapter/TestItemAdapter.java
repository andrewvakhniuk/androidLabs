package amp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ua.viktor.amp.R;

import java.util.List;

import amp.dao.Choice;

/**
 * Created by viktor on 03.10.15.
 */
public class TestItemAdapter extends RecyclerView.Adapter<TestItemAdapter.ViewHolder> {
    private List<Choice> mData;
    private OnItemClickListener mOnClickListener;


    @Override
    public TestItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, viewGroup.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tvspecies.setText(mData.get(i).getItem().toString());
        //   Context context=viewHolder.imgThumbnail.getContext();

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(mData.get(i).getItem().toString().charAt(0) + "", color);
        viewHolder.imgThumbnail.setImageDrawable(drawable);
    }

    public TestItemAdapter(List<Choice> choices) {

        this.mData = choices;
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

        public ImageView imgThumbnail;
        public TextView tvspecies;
        Context contxt;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            contxt = context;
            imgThumbnail = (ImageView) itemView.findViewById(R.id.imageView);
            tvspecies = (TextView) itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, mData.get(getLayoutPosition()).getID());

                //  v.setClickable(false);

            }
        }
    }
}




