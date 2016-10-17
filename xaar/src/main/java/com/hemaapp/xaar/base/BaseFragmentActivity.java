package com.hemaapp.xaar.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

public abstract class BaseFragmentActivity<T extends ViewDelegate, M extends BaseModel> extends BasePresenterActivity<T, M> {


    private FragmentManager mFragmentManager;

    /**
     * 显示或更换Fragment
     *
     * @param c
     */
    protected void toogleFragment(Class<? extends Fragment> c) {
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        String tag = c.getName();
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (fragment == null) {
            try {
                fragment = c.newInstance();
                // 替换时保留Fragment,以便复用
                fragmentTransaction.add(getContentId(), fragment, tag);
            } catch (Exception e) {
                // ignore
            }
        } else {

        }
        // 遍历存在的Fragment,隐藏其他Fragment
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null)
            for (Fragment fm : fragments)
                if (!fm.equals(fragment))
                    fragmentTransaction.hide(fm);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    protected abstract int getContentId();


}
