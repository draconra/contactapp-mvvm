package com.gojek.testgojek;

import com.gojek.testgojek.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aditya Sumardi
 */

public class RandomUserGenerator {

    /**
     * "id": 1,
     "first_name": "Hyun",
     "last_name": "Jung Nim",
     "email": "hyunjung@nim.com",
     "phone_number": "+6285312448905",
     "profile_pic": "http://gojek-contacts-app.herokuapp.com/images/missing.png",
     "favorite": false,
     */

    private static final String CONTACT_FIRST_NAME_TEST = "Hyun";
    private static final String CONTACT_LAST_NAME_TEST = "Jung Nim";
    private static final String CONTACT_EMAIL_TEST = "hyunjung@nim.com";
    private static final String CONTACT_PHONE_NUMBER_TEST = "+6285312448905";
    private static final boolean CONTACT_FAVORITE_TEST = true;

    public static List<Contact> getContactList() {
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 300; i++)
           contacts.add(getContact(i));
        return contacts;
    }

    public static Contact getContact(int id) {
        Contact contact = new Contact();
        contact.id = id;
        contact.firstName = CONTACT_FIRST_NAME_TEST;
        contact.lastName = CONTACT_LAST_NAME_TEST;
        contact.email = CONTACT_EMAIL_TEST;
        contact.phoneNumber = CONTACT_PHONE_NUMBER_TEST;
        contact.favorite = CONTACT_FAVORITE_TEST;
        return contact;
    }

}
