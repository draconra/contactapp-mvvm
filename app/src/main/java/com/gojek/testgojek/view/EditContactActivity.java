package com.gojek.testgojek.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gojek.testgojek.R;
import com.gojek.testgojek.databinding.ActivityEditContactBinding;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.view.base.BaseActivity;
import com.gojek.testgojek.viewmodel.EditContactViewModel;
import com.gojek.testgojek.viewmodel.EditContactViewModelContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * AddContactActivity
 *
 * @author Aditya Sumardi
 */

public class EditContactActivity extends BaseActivity implements EditContactViewModelContract {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.firstname)
    AppCompatEditText etFirstName;
    @BindView(R.id.lastname)
    AppCompatEditText etLastName;
    @BindView(R.id.mobilenumber)
    AppCompatEditText etMobileNumber;
    @BindView(R.id.emailaddress)
    AppCompatEditText etEmailAddress;

    EditContactViewModel editContactViewModel;
    ActivityEditContactBinding activityEditContactBinding;
    private long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactId = getIntent().getLongExtra("id",1);

        Contact myContact =  new Contact();
        myContact.setFirstName(getIntent().getStringExtra("firstname"));
        myContact.setLastName(getIntent().getStringExtra("lastname"));
        myContact.setPhoneNumber(getIntent().getStringExtra("phone"));
        myContact.setEmail(getIntent().getStringExtra("email"));

        activityEditContactBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_contact);
        editContactViewModel = new EditContactViewModel(getApplicationContext(), contactId, myContact);
        activityEditContactBinding.setEditContactViewModel(editContactViewModel);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_new_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.menu_save:
                editContactViewModel.onClickSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Context getContext() {
        return EditContactActivity.this;
    }
}

