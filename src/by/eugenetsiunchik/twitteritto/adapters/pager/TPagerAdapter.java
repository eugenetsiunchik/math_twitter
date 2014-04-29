package by.eugenetsiunchik.twitteritto.adapters.pager;

import by.eugenetsiunchik.twitteritto.fragments.FeedFragment;
import by.eugenetsiunchik.twitteritto.fragments.MessageFragment;
import by.eugenetsiunchik.twitteritto.fragments.UserProfileFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TPagerAdapter extends FragmentStatePagerAdapter {

	public TPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {

		switch (arg0) {
		case 2:
		{
			return new UserProfileFragment();
		}
		case 1:
		{
			return new MessageFragment();
		}
		case 0:
		{
			return new FeedFragment();
		}

		default:
			
			return null;
		}
		
	}

	@Override
	public int getCount() {

		return 3;
	}

}
