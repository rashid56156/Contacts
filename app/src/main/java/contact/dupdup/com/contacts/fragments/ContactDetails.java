package contact.dupdup.com.contacts.fragments;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import contact.dupdup.com.contacts.R;
import contact.dupdup.com.contacts.databinding.FragmentContactsDetailsBinding;
import contact.dupdup.com.contacts.objects.ContactVO;

public class ContactDetails extends Fragment {


    FragmentContactsDetailsBinding binding;
    static ContactVO contactVO;
    ColorGenerator generator;

    public static ContactDetails newInstance(ContactVO contact) {
        ContactDetails fragment = new ContactDetails();
        contactVO = contact;
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts_details, container, false);

        generator = ColorGenerator.MATERIAL;

        if (contactVO.photo != null && !contactVO.photo.isEmpty() && !contactVO.photo.equals("null")) {
            Uri u = Uri.parse(contactVO.photo);
            binding.ivContactImageDetailCircle.setImageURI(u);
            binding.ivContactImageDetail.setVisibility(View.GONE);
        } else {
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(contactVO.name.substring(0,1), generator.getColor(contactVO.name));
            binding.ivContactImageDetail.setImageDrawable(drawable);
            binding.ivContactImageDetailCircle.setVisibility(View.GONE);
        }

        binding.tvContactNameDetail.setText(contactVO.name);


        if (contactVO.phones != null  && contactVO.phones.size() > 0){

            for(int i = 0; i < contactVO.phones.size(); i++) {
                int key = contactVO.phones.keyAt(i);
                // get the object by the key.
                Object obj = contactVO.phones.get(key);
                binding.tvPhoneNumberDetail.setText(obj.toString());
            }
        } else {
            binding.getRoot().findViewById(R.id.phone_layout).setVisibility(View.GONE);
        }

        if (contactVO.emails != null  && contactVO.emails.size() > 0){
            binding.tvEmailDetail.setText(contactVO.emails.get(0));
            for(int i = 0; i < contactVO.emails.size(); i++) {
                int key = contactVO.emails.keyAt(i);
                // get the object by the key.
                Object obj = contactVO.emails.get(key);
                binding.tvEmailDetail.setText(obj.toString());
            }
        } else {
            binding.getRoot().findViewById(R.id.email_layout).setVisibility(View.GONE);
        }


        return binding.getRoot();
    }

}
