package com.gojek.testgojek.viewmodel;

import android.content.Context;

import com.gojek.testgojek.model.Contact;

import java.util.ArrayList;

/**
 * ContactsViewModelContract
 *
 * @author Aditya Sumardi
 */

public interface ContactsViewModelContract {

    interface MainView {
        Context getContext();
        void loadData(final ArrayList<Contact> contacts);
    }

    interface ViewModel {
        void destroy();
    }
}

