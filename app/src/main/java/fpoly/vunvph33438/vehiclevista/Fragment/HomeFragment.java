package fpoly.vunvph33438.vehiclevista.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fpoly.vunvph33438.vehiclevista.Adapter.PhotoViewPager2Adapter;
import fpoly.vunvph33438.vehiclevista.Model.Photo;
import fpoly.vunvph33438.vehiclevista.R;
import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    View view;
    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<Photo> mListPhotos;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mViewPager2.getCurrentItem() == mListPhotos.size() - 1) {
                mViewPager2.setCurrentItem(0);
            }
            mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager2 = view.findViewById(R.id.viewPager2);
        mCircleIndicator3 = view.findViewById(R.id.circleIndicator3);

        mListPhotos = getListPhoto();
        PhotoViewPager2Adapter adapter = new PhotoViewPager2Adapter(mListPhotos);
        mViewPager2.setAdapter(adapter);

        mCircleIndicator3.setViewPager(mViewPager2);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }
        });

        return view;
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.avatar_default));
        list.add(new Photo(R.drawable.car));
        list.add(new Photo(R.drawable.logo));
        list.add(new Photo(R.drawable.reset_password));

        return list;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 5000);
    }
}