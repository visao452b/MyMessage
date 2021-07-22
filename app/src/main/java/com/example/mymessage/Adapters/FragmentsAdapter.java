package com.example.mymessage.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mymessage.Fragments.StoryFragments;
import com.example.mymessage.Fragments.ChatsFragments;
import com.example.mymessage.Fragments.FriendsFragments;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new StoryFragments();
            case 1: return new ChatsFragments();
            case 2: return new FriendsFragments();
            default: return new StoryFragments();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==0){
            title = "STORY";
        }

        if (position==1){
            title = "CHAT";
        }

        if (position==2){
            title = "FRIENDS";
        }

        return title;
    }
}
