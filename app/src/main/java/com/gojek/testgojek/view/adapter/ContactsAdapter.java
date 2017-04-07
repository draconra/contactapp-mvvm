package com.gojek.testgojek.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gojek.testgojek.R;
import com.gojek.testgojek.data.ContactFactory;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.utils.CircularContactView;
import com.gojek.testgojek.utils.SearchablePinnedHeaderListViewAdapter;
import com.gojek.testgojek.utils.StringArrayAlphabetIndexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ContactsAdapter
 *
 * @author Aditya Sumardi
 */

public class ContactsAdapter extends SearchablePinnedHeaderListViewAdapter<Contact> {
    private ArrayList<Contact> mContacts;
    private final int[] PHOTO_TEXT_BACKGROUND_COLORS;
    private Context mContext;

    @Override
    public CharSequence getSectionTitle(int sectionIndex) {
        return ((StringArrayAlphabetIndexer.AlphaBetSection) getSections()[sectionIndex]).getName();
    }

    public ContactsAdapter(Context context,final ArrayList<Contact> contacts) {
        this.mContext = context;
        setData(contacts);
        PHOTO_TEXT_BACKGROUND_COLORS = mContext.getResources().getIntArray(R.array.contacts_text_background_colors);
    }

    public void setData(final ArrayList<Contact> contacts) {
        this.mContacts = contacts;
        final String[] generatedContactNames = generateContactNames(contacts);
        setSectionIndexer(new StringArrayAlphabetIndexer(generatedContactNames, true));
    }

    private String[] generateContactNames(final List<Contact> contacts) {
        final ArrayList<String> contactNames = new ArrayList<String>();
        if (contacts != null)
            for (final Contact contactEntity : contacts)
                contactNames.add(contactEntity.getFirstName());
        return contactNames.toArray(new String[contactNames.size()]);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        final View rv;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rv = inflater.inflate(R.layout.contact_listview_item, parent, false);
            holder.profileCircularContactView = (CircularContactView) rv.findViewById(R.id.cv_profile_picture);
            holder.profileCircularContactView.getTextView().setTextColor(0xFFffffff);
            holder.userName = (TextView) rv.findViewById(R.id.listview_item__name);
            holder.headerView = (TextView) rv.findViewById(R.id.header_text);
            rv.setTag(holder);
        } else {
            rv = convertView;
            holder = (ViewHolder) rv.getTag();
        }
        final Contact contact = getItem(position);
        final String displayName = contact.getFirstName()+" "+contact.getLastName();
        holder.userName.setText(displayName);

        final int backgroundColorToUse = PHOTO_TEXT_BACKGROUND_COLORS[position
                % PHOTO_TEXT_BACKGROUND_COLORS.length];
        if (TextUtils.isEmpty(displayName))
            holder.profileCircularContactView.setImageResource(R.drawable.ic_profile,
                    backgroundColorToUse);
        else {
            Glide.with(mContext)
                    .load(ContactFactory.BASE_URL+contact.getProfilePic())
                    .into(holder.profileCircularContactView.getImageView());
        }
        bindSectionHeader(holder.headerView, null, position);
        return rv;
    }

    @Override
    public boolean doFilter(final Contact item, final CharSequence constraint) {
        if (TextUtils.isEmpty(constraint))
            return true;
        final String displayName = item.getFirstName();
        return !TextUtils.isEmpty(displayName) && displayName.toLowerCase(Locale.getDefault())
                .contains(constraint.toString().toLowerCase(Locale.getDefault()));
    }

    @Override
    public ArrayList<Contact> getOriginalList() {
        return mContacts;
    }

    private static class ViewHolder {
        public CircularContactView profileCircularContactView;
        private TextView userName, headerView;
    }
}
