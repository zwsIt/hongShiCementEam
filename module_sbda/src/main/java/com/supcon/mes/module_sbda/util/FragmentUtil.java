package com.supcon.mes.module_sbda.util;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Environment: hongruijun
 * Created by Xushiyun on 2018/5/22.
 */
@SuppressWarnings("unused")
public final class FragmentUtil {
    public static void hideFragment(FragmentManager fm, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    public static void showFragment(FragmentManager fm, Fragment fragment)
    {
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    public static void transFragment(FragmentManager fm, Fragment from, Fragment to) {
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(from);
        fragmentTransaction.show(to);
        fragmentTransaction.commit();
    }

    public static void addFragments(FragmentManager fm, @IdRes int id, Fragment... fragments) {
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        for (Fragment f : fragments) {
            fragmentTransaction.add(id, f);
        }
        fragmentTransaction.commit();
    }

    public static void removeFragment(FragmentManager fm, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
}
