package edu.zju.huyifeng.sportwear.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import edu.zju.huyifeng.sportwear.R;
import edu.zju.huyifeng.sportwear.adapter.TrainListAdapter;

/**
 * Created by huyifeng on 15/3/10.
 */
public class TrainFragment extends Fragment {
    private View mRootView;
    private ListView mListView;
    private LinearLayout mLlytAddTrain;
    private TrainListAdapter trainListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_train, container, false);
        Bundle bundle = this.getArguments();
        initView(bundle);
        return mRootView;
    }

    private void initView(Bundle bundle) {
        mListView = (ListView) mRootView.findViewById(R.id.lv_train);
        mLlytAddTrain = (LinearLayout) mRootView.findViewById(R.id.llyt_add_train);
        trainListAdapter = new TrainListAdapter(getActivity());
        mListView.setAdapter(trainListAdapter);
        if (trainListAdapter.getCount() > 0) {
            mLlytAddTrain.setVisibility(View.GONE);
        }
        mLlytAddTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddTrainActivity.class);
                startActivity(intent);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TrainDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }
}
