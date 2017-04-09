package com.gojek.testgojek;

import android.app.Activity;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;

import com.gojek.testgojek.RandomUserGenerator;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.view.AddContactActivity;
import com.gojek.testgojek.viewmodel.AddContactViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * AddContactViewModelTest
 *
 * @author Aditya Sumardi
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddContactViewModelTest {

    private AddContactViewModel addContactViewModel;
    private Contact contact;
    AppCompatEditText firstNameET;
    AppCompatEditText lastNameET;
    AppCompatEditText emailET;
    AppCompatEditText phoneNumberET;

    @Before
    public void setUpDetailViewModelTest() {
        contact = RandomUserGenerator.getContact(randInt(1, 500));
        addContactViewModel = new AddContactViewModel(contact);
    }


    @Test public void shouldGetContactName() throws Exception {
        assertEquals(contact.firstName, addContactViewModel.getFirstName());
    }

    @Before
    public void enterData() throws Exception{
        Activity activity = Robolectric.setupActivity(AddContactActivity.class);
        firstNameET = (AppCompatEditText) activity.findViewById(R.id.firstname);
        lastNameET = (AppCompatEditText) activity.findViewById(R.id.lastname);
        emailET = (AppCompatEditText) activity.findViewById(R.id.emailaddress);
        firstNameET.setText(contact.getFirstName());
        phoneNumberET = (AppCompatEditText) activity.findViewById(R.id.mobilenumber);
        phoneNumberET.setText(contact.phoneNumber);
    }


    @Test
    public void shouldValidateWrongMobileNumberData() throws Exception {
        assertEquals(addContactViewModel.validateData(contact.firstName, contact.lastName, "0123456789", contact.email), "Mobile Phone Number not valid");
    }

    @Test
    public void shouldValidateWrongFirstName() throws Exception {
        assertEquals(addContactViewModel.validateData("si", contact.lastName, contact.phoneNumber, contact.email), "First Name not valid");
    }

    @Test
    public void shouldValidateCorrectData() throws Exception {
        assertEquals(addContactViewModel.validateData(contact.firstName, contact.lastName, contact.phoneNumber, contact.email), "Success");
    }

    @Test
    public void shouldClickButtonToSave() throws Exception {
        Activity activity = Robolectric.setupActivity(AddContactActivity.class);
        firstNameET.setText(contact.getFirstName());
        lastNameET.setText(contact.getLastName());
        emailET.setText(contact.email);
        phoneNumberET.setText(contact.phoneNumber);
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
