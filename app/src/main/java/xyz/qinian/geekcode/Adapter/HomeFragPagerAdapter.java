package xyz.qinian.geekcode.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import xyz.qinian.geekcode.Activity.HomeActivity;
import xyz.qinian.geekcode.Fragment.ExaminationFragment;
import xyz.qinian.geekcode.Fragment.PersonalFragment;
import xyz.qinian.geekcode.Fragment.RankFragment;
import xyz.qinian.geekcode.Fragment.StatusFragment;

public class HomeFragPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;

    private ExaminationFragment examinationFragment;
    private StatusFragment statusFragment;
    private RankFragment rankFragment;
    private PersonalFragment personalFragment;

    public HomeFragPagerAdapter(FragmentManager fm, String userId) {
        super(fm);
        examinationFragment = new ExaminationFragment(userId);
        statusFragment = new StatusFragment();
        rankFragment = new RankFragment(userId);
        personalFragment = new PersonalFragment(userId);
    }

    public ExaminationFragment getExaminationFragment() {
        return examinationFragment;
    }

    public StatusFragment getStatusFragment() {
        return statusFragment;
    }

    public RankFragment getRankFragment() {
        return rankFragment;
    }

    public PersonalFragment getPersonalFragment() {
        return personalFragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case HomeActivity.PAGE_TWO:
                fragment = statusFragment;
                break;
            case HomeActivity.PAGE_THREE:
                fragment = rankFragment;
                break;
            case HomeActivity.PAGE_FOUR:
                fragment = personalFragment;
                break;
            default:
                fragment = examinationFragment;
                break;
        }
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

}
