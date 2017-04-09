package com.gojek.testgojek.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gojek.testgojek.R;
import com.gojek.testgojek.databinding.ActivityAddContactBinding;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.view.base.BaseActivity;
import com.gojek.testgojek.viewmodel.AddContactViewModel;
import com.gojek.testgojek.viewmodel.AddContactViewModelContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * AddContactActivity
 *
 * @author Aditya Sumardi
 */

public class AddContactActivity extends BaseActivity implements AddContactViewModelContract {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AddContactViewModel addContactViewModel;
    ActivityAddContactBinding activityAddContactBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Contact myContact =  new Contact();
        activityAddContactBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact);
        addContactViewModel = new AddContactViewModel(getApplicationContext(), myContact );
        activityAddContactBinding.setAddContactViewModel(addContactViewModel);
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
                addContactViewModel.onClickSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Context getContext() {
        return AddContactActivity.this;
    }
}

