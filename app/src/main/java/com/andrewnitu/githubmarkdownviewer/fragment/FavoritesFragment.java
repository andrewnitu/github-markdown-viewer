package com.andrewnitu.githubmarkdownviewer.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewnitu.githubmarkdownviewer.R;
import com.andrewnitu.githubmarkdownviewer.fragment.favorites.FavoritesFileFragment;
import com.andrewnitu.githubmarkdownviewer.fragment.favorites.FavoritesRepoFragment;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    View rootView;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) rootView.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        FavoritesTabAdapter favoritesTabAdapter = new FavoritesTabAdapter(getChildFragmentManager());

        favoritesTabAdapter.addFragment(new FavoritesRepoFragment(), "Repos");
        favoritesTabAdapter.addFragment(new FavoritesFileFragment(), "Files");

        viewPager.setAdapter(favoritesTabAdapter);
    }

    public class FavoritesTabAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        private ArrayList<String> titleList = new ArrayList<String>();

        public FavoritesTabAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
