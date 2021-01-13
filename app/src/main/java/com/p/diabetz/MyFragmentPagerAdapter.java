package com.p.diabetz;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private int NumOfTabs;

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm,int NumOfTabs) {
        super(fm);
        this.NumOfTabs = NumOfTabs;
    }


    //

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                
                return new Accueil();
            case 1:
                return new Mesures();
            case 2:
                return new Donnees();
            default:
                throw new RuntimeException("Invalid tab position");
        }
    }


    @Override
    public int getCount() {
        return NumOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
