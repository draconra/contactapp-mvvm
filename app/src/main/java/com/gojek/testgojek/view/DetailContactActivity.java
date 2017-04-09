package com.gojek.testgojek.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.gojek.testgojek.ContactApplication;
import com.gojek.testgojek.R;
import com.gojek.testgojek.data.ContactFactory;
import com.gojek.testgojek.data.ContactService;
import com.gojek.testgojek.databinding.ActivityContactDetailBinding;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.view.base.BaseActivity;
import com.gojek.testgojek.viewmodel.ContactDetailViewModel;
import com.gojek.testgojek.viewmodel.ContactDetailViewModelContract;

import net.redwarp.library.database.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * DetailContactActivity
 *
 * @author Aditya Sumardi
 */

public class DetailContactActivity extends BaseActivity implements ContactDetailViewModelContract.MainView {

    @BindView(R.id.iv_contact_avatar)
    ImageView userImage;
    @BindView(R.id.tv_username)
    AppCompatTextView userName;
    @BindView(R.id.tv_mobile_phone)
    AppCompatTextView userPhone;
    @BindView(R.id.tv_email)
    AppCompatTextView userEmail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    final int MY_PERMISSION_REQ_CALL = 1;
    final int MY_PERMISSION_REQ_STORAGE = 2;

    private long userId;
    private Contact contact;
    private MenuItem mMenuDetail;
    private Boolean isFavorite = false;

    private ActivityContactDetailBinding activityContactDetailBinding;

    private ContactDetailViewModel contactDetailViewModel;
    private ContactDetailViewModelContract.MainView mainView = this;
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityContactDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        userId = getIntent().getLongExtra("id", 1);
        isFavorite = Boolean.valueOf(getIntent().getStringExtra("favorite"));

        //Get Individual contact
        ContactService contactService =
                ContactFactory.getClient().create(ContactService.class);

        Call<Contact> getContactCall = contactService.getContact((int) userId);
        getContactCall.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {

                contact = response.body();
                contactDetailViewModel = new ContactDetailViewModel(mainView, getApplicationContext(), contact);
                activityContactDetailBinding.setContactDetailViewModel(contactDetailViewModel);
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Log.v("Contact", "getContactCall failure response: " + t.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        mMenuDetail = menu.findItem(R.id.action_fav);

        if (isFavorite)
            mMenuDetail.setIcon(R.drawable.ic_favourite_filled);
        else
            mMenuDetail.setIcon(R.drawable.ic_favourite);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.action_fav:
                onClickFavorite();
                return true;
            case R.id.action_edit:
                contactDetailViewModel.editData();
                return true;
            case R.id.action_other:
                contactDetailViewModel.onClickShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQ_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + contactDetailViewModel.getPhoneNumber().trim()));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DetailContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQ_CALL);
                        return;
                    }
                    startActivity(i);

                } else {
                    //TODO
                }
            }
            break;
            case MY_PERMISSION_REQ_STORAGE:
                //TODO
                break;
        }
    }

    public void onClickFavorite() {
        ContactApplication contactApplication = ContactApplication.create(getContext());
        ContactService contactsService = contactApplication.getContactsService();

        Call<Contact> putContactCall = contactsService.putContactsList(this.contact.getId(), contactDetailViewModel.updateFavorite());
        putContactCall.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                if (response.isSuccessful()) {
                    isFavorite = true;
                    invalidateOptionsMenu();

                    //Flushing the DB to fetch again
                    helper = new DatabaseHelper(getContext());
                    helper.beginTransaction();
                    helper.clear(Contact.class);
                    helper.setTransactionSuccessful();
                    helper.endTransaction();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
            }
        });
    }

    @Override
    public Context getContext() {
        return DetailContactActivity.this;
    }

    @Override
    public Activity getActivity() {
        return DetailContactActivity.this;
    }
}
