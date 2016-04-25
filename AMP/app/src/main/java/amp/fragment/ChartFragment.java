package amp.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by viktor on 19.10.15.
 */
public abstract class ChartFragment extends Fragment {
    public abstract void restartAnimation();

    public abstract void onReset();
}
