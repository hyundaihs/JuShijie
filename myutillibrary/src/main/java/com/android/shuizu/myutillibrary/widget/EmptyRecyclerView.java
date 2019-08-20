package com.android.shuizu.myutillibrary.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * JuShijie
 * Created by 蔡雨峰 on 2019/8/20.
 */
public class EmptyRecyclerView extends RecyclerView {
    private static final String TAG = "EmptyRecyclerView";
    private View emptyView;
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            Log.i(TAG, "onItemRangeInserted" + itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };
    private Context context;

    public EmptyRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    //设置没有内容时，提示用户的空布局
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    //设置没有内容时，提示用户的空布局
    public void setEmptyViewRes(int layoutId) {
        this.emptyView = LayoutInflater.from(context).inflate(layoutId, null, false);
        checkIfEmpty();
    }

    //设置没有内容时，提示用户的空布局
    public void setEmptyViewRes(int layoutId, int viewId) {
        View root = LayoutInflater.from(context).inflate(layoutId, null, false);
        this.emptyView = root.findViewById(viewId);
        checkIfEmpty();
    }
}
