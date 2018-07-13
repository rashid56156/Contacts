package contact.dupdup.com.contacts.fragments;


import android.Manifest;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import contact.dupdup.com.contacts.R;
import contact.dupdup.com.contacts.activities.MainActivity;
import contact.dupdup.com.contacts.adapters.ContactsAdapter;
import contact.dupdup.com.contacts.databinding.FragmentContactsListBinding;
import contact.dupdup.com.contacts.helpers.ItemClickSupport;
import contact.dupdup.com.contacts.objects.ContactVO;

public class ContactsList extends Fragment {


    FragmentContactsListBinding binding;
    List<ContactVO> contactsList;
    private static final String BACK_STACK_ROOT_TAG = "list_fragment";
    ProgressBar progressBar;

    public static ContactsList newInstance() {
        ContactsList fragment = new ContactsList();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts_list, container, false);

        progressBar = binding.getRoot().findViewById(R.id.contacts_progress);
        contactsList = new LinkedList<ContactVO>();

        Permissions.check(getActivity(), Manifest.permission.READ_CONTACTS, null,
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        //do your task.
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO your background code
                              //  getAllContacts();
                                fetchContacts();
                            }
                        });

                    }
                });


        ItemClickSupport.addTo(binding.contactsView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

               ((MainActivity) getActivity()).addFragment(ContactDetails.newInstance(contactsList.get(position)), BACK_STACK_ROOT_TAG);

            }
        });

        return binding.getRoot();
    }

    public void fetchContacts() {

        SparseArray<ContactVO> addressbook_array = null;
        {
            addressbook_array = new SparseArray<ContactVO>();

            long start = System.currentTimeMillis();

            String[] projection = {
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.Data.CONTACT_ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_URI,
                    ContactsContract.Contacts.STARRED,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.RawContacts.ACCOUNT_TYPE,
                    ContactsContract.CommonDataKinds.Contactables.DATA,
                    ContactsContract.CommonDataKinds.Contactables.TYPE
            };

            String selection = ContactsContract.Data.MIMETYPE + " in (?, ?)" + " AND " /*+ ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + 1 + "' AND "*/ +
                    ContactsContract.Data.HAS_PHONE_NUMBER + " = '" + 1 + "'";

            String[] selectionArgs = {
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
            };

            String sortOrder = ContactsContract.Contacts.SORT_KEY_ALTERNATIVE;

            Uri uri = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;
            } else {
                uri = ContactsContract.Data.CONTENT_URI;

            }
            // we could also use Uri uri = ContactsContract.Data.CONTENT_URI;
            // we could also use Uri uri = ContactsContract.Contact.CONTENT_URI;

            Cursor cursor = getActivity().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);


            final int mimeTypeIdx = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
            final int idIdx = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
            final int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            final int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int dataIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA);
            final int photo = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.PHOTO_URI);
            final int typeIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE);
            final int account_type = cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE);
            while (cursor.moveToNext()) {
                int contact_id = cursor.getInt(idIdx);
                String photo_uri = cursor.getString(photo);
                String contact_name = cursor.getString(nameIdx);
                String contact_phone = cursor.getString(phoneIdx);
                String contact_acc_type = cursor.getString(account_type);
                int contact_type = cursor.getInt(typeIdx);
                String contact_data = cursor.getString(dataIdx);
                ContactVO contactBook = addressbook_array.get(contact_id);



            if (contactBook == null) {
                //list  contact add to avoid duplication
                //load All contacts fro device
                //to add contacts number with name add one extra veriable in ContactVO as number and pass contact_data this give number to you (contact_data is PHONE NUMBER)
                contactBook = new ContactVO(contact_id, contact_name, getResources(), photo_uri, contact_acc_type, contact_phone);
                addressbook_array.put(contact_id, contactBook);
                contactsList.add(contactBook);

            }

                String Contact_mimeType = cursor.getString(mimeTypeIdx);
                //here am checking Contact_mimeType to get mobile number asociated with perticular contact and email adderess asociated
                if (Contact_mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                    if (contactBook != null) {
                        contactBook.addEmail(contact_type, contact_data);
                    }

                } else {
                    if (contactBook == null) {
                        //list  contact add to avoid duplication
                        //load All contacts fro device
                        //to add contacts number with name add one extra veriable in ContactVO as number and pass contact_data this give number to you (contact_data is PHONE NUMBER)
                        contactBook = new ContactVO(contact_id, contact_name, getResources(), photo_uri, contact_acc_type, "phone number");
                        addressbook_array.put(contact_id, contactBook);
                        contactsList.add(contactBook);

                    }
                     contactBook.addPhone(contact_type, contact_data);


                }


            }

            cursor.close();


            try {
                Collections.sort(contactsList, new Comparator<ContactVO>() {
                    @Override
                    public int compare(ContactVO lhs, ContactVO rhs) {
                        return lhs.name.toUpperCase().compareTo(rhs.name.toUpperCase());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Setting up the recyclerview adapter

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                ContactsAdapter contactAdapter = new ContactsAdapter(contactsList, getActivity());
                binding.contactsView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.contactsView.setAdapter(contactAdapter);
                progressBar.setVisibility(View.GONE);

                }
            });

        }

    }



}

