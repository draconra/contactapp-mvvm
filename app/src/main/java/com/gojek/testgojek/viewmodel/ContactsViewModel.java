package com.gojek.testgojek.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.gojek.testgojek.data.ContactFactory;
import com.gojek.testgojek.data.ContactService;
import com.gojek.testgojek.model.Contact;

import net.redwarp.library.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ContactsViewModel
 *
 * @author Aditya Sumardi
 */

public class ContactsViewModel {

    private Context mContext;
    private ContactsViewModelContract.MainView contactsViewModelContract;
    private DatabaseHelper helper;
    private ArrayList<Contact> contacts;
    private ContactService contactService;
    public boolean isConnected;
    private ProgressDialog progressDialog;

    public ContactsViewModel(ContactsViewModelContract.MainView mainView, Context context) {
        contactsViewModelContract = mainView;
        this.mContext = context;
        initialChecks();
    }

    public void initialChecks()
    {
        progressDialog = new ProgressDialog(mContext);
        //Check Network connection
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //Instantiate DB object
        helper = new DatabaseHelper(mContext);

        if (!isConnected)
            Snackbar.make((((Activity) mContext).findViewById(android.R.id.content)), "Not able to connect to Server", Snackbar.LENGTH_SHORT).show();

        //Check whether there is data in local DB
        if (helper.getCount(Contact.class) > 0) {
            List<Contact> allContacts = helper.getAll(Contact.class);
            for (int i = 0; i < allContacts.size(); i++) {
                contacts = new ArrayList<>(allContacts.size());
                contacts.addAll(allContacts);
                //Set data on the listview
                //setContactsList(contacts);
                contactsViewModelContract.loadData(contacts);
            }
        } else {
            if (isConnected) {
                progressDialog.setMessage("Please Wait");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
                doLoadDataContacts();
            }else {
                showRetryDialog();
            }
        }
    }

    public void doLoadDataContacts() {
        contactService = ContactFactory.getClient().create(ContactService.class);
        //Get all contacts
        Call<List<Contact>> getContactsCall = contactService.getContactsList();
        getContactsCall.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                progressDialog.dismiss();
                //Retrieving from DB
                contacts = new ArrayList<>(response.body().size());
                contacts.addAll(response.body());

                helper.beginTransaction();
                for (int i = 0; i < contacts.size(); i++)
                    helper.save(contacts.get(i));

                helper.setTransactionSuccessful();
                helper.endTransaction();

                contactsViewModelContract.loadData(contacts);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }

    public void showRetryDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(contactsViewModelContract.getContext());
        builder.setCancelable(false);
        builder.setTitle("");
        builder.setCancelable(true);
        builder.setMessage("Do you want to retry?");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                doLoadDataContacts();
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        // Create the AlertDialog object and return it
        builder.create().show();
    }
}
