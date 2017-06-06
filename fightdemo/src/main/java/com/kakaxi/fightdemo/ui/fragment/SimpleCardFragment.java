package com.kakaxi.fightdemo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kakaxi.fightdemo.R;
import com.kakaxi.fightdemo.network.TestHttpActivity;
import com.kakaxi.fightdemo.ui.acitivity.ScrollingActivity;
import com.kakaxi.fightdemo.ui.acitivity.UploadDownActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SimpleCardFragment extends Fragment {
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.type_tv)
    TextView mTitle;
    private String mTitleStr;


    public static SimpleCardFragment getInstance(String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.mTitleStr = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_simple_card, null);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View v) {
        mTitle.setText(mTitleStr);
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                startActivity(TestHttpActivity.getIntent(getActivity()));
                break;
            case R.id.button2:
                startActivity(ScrollingActivity.getIntent(getActivity()));
                break;
            case R.id.button3:
                startActivity(UploadDownActivity.getIntent(getActivity()));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}