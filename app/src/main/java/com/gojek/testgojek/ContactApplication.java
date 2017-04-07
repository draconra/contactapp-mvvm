package com.gojek.testgojek;

import android.app.Application;
import android.content.Context;

import com.gojek.testgojek.data.ContactFactory;
import com.gojek.testgojek.data.ContactService;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * ContactApplication
 *
 * @author Aditya Sumardi
 */

public class ContactApplication extends Application {

    public void onCreate() {
        super.onCreate();
    }

    private ContactService contactService;
    private Scheduler scheduler;

    private static ContactApplication get(Context context) {
        return (ContactApplication) context.getApplicationContext();
    }

    public static ContactApplication create(Context context) {
        return ContactApplication.get(context);
    }

    public ContactService getContactsService() {
        if (contactService == null) contactService = ContactFactory.create();
        return contactService;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) scheduler = Schedulers.io();
        return scheduler;
    }

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
