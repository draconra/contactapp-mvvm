package com.gojek.testgojek.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.gojek.testgojek.R;
import com.gojek.testgojek.databinding.ActivityMainBinding;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.utils.PinnedHeaderListView;
import com.gojek.testgojek.view.adapter.ContactsAdapter;
import com.gojek.testgojek.view.base.BaseActivity;
import com.gojek.testgojek.viewmodel.ContactsViewModel;
import com.gojek.testgojek.viewmodel.ContactsViewModelContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.list;

public class MainActivity extends BaseActivity implements ContactsViewModelContract.MainView{

    @BindView(R.id.no_contacts)
    AppCompatTextView noContacts;
    @BindView(R.id.layout_view)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private LayoutInflater inflater;
    private PinnedHeaderListView listView;
    private ContactsAdapter contactsAdapter;

    private ActivityMainBinding activityMainBinding;
    private ContactsViewModelContract.MainView mainView = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(MainActivity.this);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);
        ContactsViewModel contactsViewModel = new ContactsViewModel(mainView, this);
        activityMainBinding.setContactsViewModel(contactsViewModel);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setTitle("Contacts");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.menu_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    @Override
    public void loadData(final ArrayList<Contact> contacts) {
//        ArrayList<Contact> favContact = new ArrayList<>();
//        ArrayList<Contact> nonFavContact = new ArrayList<>();
//        for (int i = 0; i < contacts.size() ; i++) {
//            if(contacts.get(i).getFavorite()){
//                favContact.add(contacts.get(i));
//            }else {
//                nonFavContact.add(contacts.get(i));
//            }
//        }

//        Log.e("contact", "contact"+nonFavContact.size()+" fav"+favContact.size());

//        Collections.sort(favContact, new Comparator<Contact>() {
//            @Override
//            public int compare(Contact lhs, Contact rhs) {
//                char lhsFirstLetter = TextUtils.isEmpty(lhs.getFirstName()) ? ' ' : lhs.getFirstName().charAt(0);
//                char rhsFirstLetter = TextUtils.isEmpty(rhs.getFirstName()) ? ' ' : rhs.getFirstName().charAt(0);
//                int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
//                if (firstLetterComparison == 0)
//                    return lhs.getFirstName().compareTo(rhs.getFirstName());
//                return firstLetterComparison;
//            }
//        });

        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                char lhsFirstLetter = TextUtils.isEmpty(lhs.getFirstName()) ? ' ' : lhs.getFirstName().charAt(0);
                char rhsFirstLetter = TextUtils.isEmpty(rhs.getFirstName()) ? ' ' : rhs.getFirstName().charAt(0);
                int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
                if (firstLetterComparison == 0)
                    return lhs.getFirstName().compareTo(rhs.getFirstName());
                return firstLetterComparison;
            }
        });

        listView = (PinnedHeaderListView) findViewById(list);
        contactsAdapter = new ContactsAdapter(this,contacts);

        int pinnedHeaderBackgroundColor = getResources().getColor(R.color.pinned_header_text_bg);
        contactsAdapter.setPinnedHeaderBackgroundColor(pinnedHeaderBackgroundColor);
        contactsAdapter.setPinnedHeaderTextColor(getResources().getColor(R.color.pinned_header_text));
        listView.setPinnedHeaderView(inflater.inflate(R.layout.pinned_listview_side_header, listView, false));
        listView.setAdapter(contactsAdapter);
        listView.setOnScrollListener(contactsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailContactActivity.class);
                intent.putExtra("favorite", contacts.get(i).getFavorite().toString());
                intent.putExtra("id", contacts.get(i).getId());
                startActivity(intent);
//                Log.v("ContactId", "listview item click: " + i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });
    }
}
