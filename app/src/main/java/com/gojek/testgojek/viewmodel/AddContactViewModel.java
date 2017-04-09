package com.gojek.testgojek.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.gojek.testgojek.ContactApplication;
import com.gojek.testgojek.data.ContactService;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.view.MainActivity;

import net.redwarp.library.database.DatabaseHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;

/**
 * AddContactViewModel
 *
 * @author Aditya Sumardi
 */

public class AddContactViewModel extends BaseObservable {

    private Contact contact;
    private Context context;
    private DatabaseHelper helper;

    public AddContactViewModel(Context context, Contact contact) {
        this.contact = contact;
        this.context = context;
    }

    public AddContactViewModel(Contact contact) {
        this.contact = contact;
    }

    public String getFirstName() {
        return contact.firstName;
    }


    public String getLastName() {
        return contact.lastName;
    }

    public String getEmail() {
        return contact.email;
    }

    public String getPhoneNumber() {
        return contact.phoneNumber;
    }

    public void setFirstName(String firstName) {
        contact.firstName = firstName;
    }

    public void setLastName(String lastName) {
        contact.lastName = lastName;
    }

    public void setEmail(String email) {
        contact.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        contact.phoneNumber = phoneNumber;
    }

    public void setProfilePic(String profilePic) {
        contact.profilePic = profilePic;
    }

    public void onClickSave() {
        Contact userContact = new Contact(contact.firstName, contact.lastName, contact.email, contact.phoneNumber, "", false);
        String validationResponse = validateData(contact.firstName, contact.lastName, contact.phoneNumber, contact.email);

        if (validationResponse.equals("Success"))
            setContactDetails(userContact);
        else
            Toast.makeText(context, validationResponse, Toast.LENGTH_SHORT).show();
    }


    private void setContactDetails(Contact user) {
        ContactApplication contactApplication = ContactApplication.create(context);
        ContactService contactsService = contactApplication.getContactsService();


        Call<Contact> setContactCall = contactsService.setContactsList(user);
        setContactCall.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful()) {
                    //Flushing the DB to fetch again
                    helper = new DatabaseHelper(context);
                    helper.beginTransaction();
                    helper.clear(Contact.class);
                    helper.setTransactionSuccessful();
                    helper.endTransaction();

                    //Move back to main activity
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    if (response.code() >= 404)
                        Log.v("Contact", "Error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.v("Kontak", "getContactCall failure response: " + t.toString());
            }
        });
    }

    public String validateData(String fn, String ln, String ph, String eml) {
        if (fn.isEmpty() || fn.length() < 3)
            return "First Name not valid";
        else if (ln.isEmpty() || ln.length() < 3)
            return "Last Name not valid";
        else if(!isValidEmail(eml))
            return "Enter Valid Email";
        else {
            String regEx = "^(\\+91|0)?[0-9]{12}$";
            if (ph.isEmpty() || !ph.matches(regEx))
                return "Mobile Phone Number not valid";
        }
        return "Success";
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
