package com.gojek.testgojek.model;

import android.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.redwarp.library.database.annotation.PrimaryKey;

/**
 * Contact
 *
 * @author Aditya Sumardi
 */


public class Contact extends BaseObservable {

    @PrimaryKey
    public long key;
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone_number")
    @Expose
    public String phoneNumber;
    @SerializedName("profile_pic")
    @Expose
    public String profilePic;
    @SerializedName("favorite")
    @Expose
    public Boolean favorite;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public Contact(){}

    public Contact(long id, String firstName, String lastName, String email, String phoneNumber, String profilePic, Boolean favorite, String createdAt, String updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
        this.favorite = favorite;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Contact(String firstName, String lastName, String email, String phoneNumber, String profilePic, Boolean favorite) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
        this.favorite = favorite;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public long getId() {
        return id;
    }

    public Boolean getFavorite() {
        return favorite;
    }
}
