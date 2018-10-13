package com.algorepublic.saman.base;

import android.content.Context;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected Context context;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onDetach() {

        super.onDetach();
        this.context = null;
    }

    public abstract String getName();

}
