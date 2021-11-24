package com.example.investmenttracker.SlidePage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.investmenttracker.SlidePage.Fragments.MoneyAllocFragment;
import com.example.investmenttracker.SlidePage.Fragments.PercentFragment;
import com.example.investmenttracker.SlidePage.Fragments.PortfolioProfitFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private Fragment currFragment;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new PortfolioProfitFragment();
                break;
            case 1:
                fragment = new MoneyAllocFragment();
                break;
            case 2:
                fragment = new PercentFragment();
                break;
        }
        currFragment = fragment;
        return fragment;
    }

    public Fragment getCurrFragment() {
        return currFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
