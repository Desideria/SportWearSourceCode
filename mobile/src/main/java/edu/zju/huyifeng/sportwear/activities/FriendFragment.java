package edu.zju.huyifeng.sportwear.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.zju.huyifeng.sportwear.R;
import edu.zju.huyifeng.sportwear.adapter.ViewPagerAdapter;
import edu.zju.huyifeng.sportwear.view.SlidingTabLayout;

/**
 * Created by huyifeng on 15/3/10.
 */
public class FriendFragment extends Fragment {
    private View mRootView;
    private ViewPager pager;
    SlidingTabLayout slidingTabLayout;
    private String titles[] = new String[]{"好友", "SayHi记录"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_friend, container, false);
        Bundle bundle = this.getArguments();
        initView(bundle);
        return mRootView;
    }

    private void initView(Bundle bundle) {
        pager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) mRootView.findViewById(R.id.sliding_tabs);
        pager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), titles));

        slidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);
        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}
