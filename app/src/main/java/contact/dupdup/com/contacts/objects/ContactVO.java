package contact.dupdup.com.contacts.objects;

import android.content.res.Resources;
import android.provider.ContactsContract;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;

public class ContactVO {
    public int id;
    public Resources res;
    public String name;
    public String photo;
    public String contact_acc_type;
    public SparseArray<String> emails;
    public SparseArray<String> phones;
    /*  public LongSparseArray<String> emails;
    public LongSparseArray<String> phones;*/
    public String header = "";


    public ContactVO(int id, String name, Resources res, String photo, String contact_acc_type, String header) {
        this.id = id;
        this.name = name;
        this.res = res;
        this.photo = photo;
        this.contact_acc_type = contact_acc_type;
        this.header = header;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean rich) {

        //testing method to check ddata
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (rich) {
            builder.append("id: ").append(Long.toString(id))
                    .append(", name: ").append("\u001b[1m").append(name).append("\u001b[0m");
        } else {
            builder.append(name);
        }

        if (phones != null) {
            builder.append("\n\tphones: ");
            for (int i = 0; i < phones.size(); i++) {
                int type = (int) phones.keyAt(i);
                builder.append(ContactsContract.CommonDataKinds.Phone.getTypeLabel(res, type, ""))
                        .append(": ")
                        .append(phones.valueAt(i));
                if (i + 1 < phones.size()) {
                    builder.append(", ");
                }
            }
        }

        if (emails != null) {
            builder.append("\n\temails: ");
            for (int i = 0; i < emails.size(); i++) {
                int type = (int) emails.keyAt(i);
                builder.append(ContactsContract.CommonDataKinds.Email.getTypeLabel(res, type, ""))
                        .append(": ")
                        .append(emails.valueAt(i));
                if (i + 1 < emails.size()) {
                    builder.append(", ");
                }
            }
        }
        return builder.toString();
    }

    public void addEmail(int type, String address) {
        //this is the array in object class where i am storing contact all emails of perticular contact (single)
        if (emails == null) {
            //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            emails = new SparseArray<String>();
            emails.put(type, address);
        /*} else {
            //add emails to array below Jelly bean //use single array list
        }*/
        }
    }

    public void addPhone(int type, String number) {
        //this is the array in object class where i am storing contact numbers of particular contact
        if (phones == null) {
            //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            phones = new SparseArray<String>();
            phones.put(type, number);
       /* } else {
            //add emails to array below Jelly bean //use single array list
        }*/
        }
    }}
