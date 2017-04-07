package com.gojek.testgojek.data;

import com.gojek.testgojek.model.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * ContactService
 *
 * @author Aditya Sumardi
 */

public interface ContactService {

    @GET("contacts.json")
    Call<List<Contact>> getContactsList();

    @POST("contacts.json")
    Call<Contact> setContactsList(@Body Contact contact);

    @GET("contacts/{id}.json")
    Call<Contact> getContact(@Path("id") int contactId);

    @PUT("contacts/{id}.json")
    Call<Contact> putContactsList(@Path("id") long contactId, @Body Contact contact);
}
