package com.gojek.testgojek.viewmodel;

import android.app.Activity;
import android.content.Context;

/**
 * ContactDetailViewModelContract
 *
 * @author Aditya Sumardi
 */

public interface ContactDetailViewModelContract {
    interface MainView {
        Context getContext();
        Activity getActivity();
    }
}
