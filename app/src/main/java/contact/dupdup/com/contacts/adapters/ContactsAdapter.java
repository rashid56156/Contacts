package contact.dupdup.com.contacts.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import contact.dupdup.com.contacts.R;
import contact.dupdup.com.contacts.objects.ContactVO;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<ContactVO> contactVOList;
    private Context mContext;
    ColorGenerator generator;

    public ContactsAdapter(List<ContactVO>  contactVOList, Context mContext) {
        this.contactVOList = contactVOList;
        this.mContext = mContext;
        generator = ColorGenerator.MATERIAL;
    }

    @Override
    public ContactsAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
        ContactsAdapter.ContactViewHolder contactViewHolder = new ContactsAdapter.ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ContactViewHolder holder, int position) {
        ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.name);

        if (contactVO.photo != null && !contactVO.photo.isEmpty() && !contactVO.photo.equals("null")) {
            Uri u = Uri.parse(contactVO.photo);
            holder.ivContactImageCircle.setImageURI(u);
            holder.ivContactImage.setVisibility(View.GONE);
            holder.ivContactImageCircle.setVisibility(View.VISIBLE);

        } else {
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(contactVO.name.substring(0,1), generator.getColor(contactVO.name));
            holder.ivContactImage.setImageDrawable(drawable);
            holder.ivContactImage.setVisibility(View.VISIBLE);
            holder.ivContactImageCircle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage;
        CircularImageView ivContactImageCircle;
        TextView tvContactName;

        ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = itemView.findViewById(R.id.ivContactImage);
            ivContactImageCircle = itemView.findViewById(R.id.ivContactImageCircle);
            tvContactName = itemView.findViewById(R.id.tvContactName);
        }
    }

}

