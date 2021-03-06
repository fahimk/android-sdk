package com.yesgraph.android.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yesgraph.android.R;
import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.models.HeaderContact;
import com.yesgraph.android.models.RegularContact;
import com.yesgraph.android.utils.FontManager;
import com.yesgraph.android.utils.Visual;

import java.util.ArrayList;

/**
 * Created by marko on 16/11/15.
 */
public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> items;
    private Context context;
    private OnItemClickListener itemClickListener;
    private YesGraph application;
    private FontManager fontManager;

    private static final int TYPE_DATA = 1;
    private static final int TYPE_HEADER = 2;

    public ContactsAdapter(ArrayList<Object> items, Context context, YesGraph application) {
        this.items = items;
        this.application = application;
        this.context = context;
        fontManager = FontManager.getInstance();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        return (items.get(position) instanceof RegularContact) ? TYPE_DATA : TYPE_HEADER;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder.getItemViewType() ==TYPE_DATA)
        {
            ContactsViewHolder contactsViewHolder=(ContactsViewHolder)viewHolder;
            RegularContact contact=(RegularContact)items.get(i);
            contactsViewHolder.name.setText(contact.getName());
            contactsViewHolder.contact.setText(contact.getContact());

            if(!application.getCustomTheme().getFont().isEmpty()){
                fontManager.setFont(contactsViewHolder.name,application.getCustomTheme().getFont());
                fontManager.setFont(contactsViewHolder.contact,application.getCustomTheme().getFont());
            }

            if(contact.getSelected())
            {
                contactsViewHolder.check.setBackgroundResource(R.drawable.circle);
                GradientDrawable drawable = (GradientDrawable) contactsViewHolder.check.getBackground();
                drawable.setColor(application.getCustomTheme().getMainForegroundColor());
                drawable.setStroke(Visual.getPixelsFromDp(context, 1), application.getCustomTheme().getMainForegroundColor());
                contactsViewHolder.background.setBackgroundColor(application.getCustomTheme().getRowSelectedColor());
            }
            else
            {
                contactsViewHolder.check.setBackgroundResource(R.drawable.circle);
                GradientDrawable drawable = (GradientDrawable) contactsViewHolder.check.getBackground();
                drawable.setColor(context.getResources().getColor(android.R.color.white));
                drawable.setStroke(Visual.getPixelsFromDp(context, 1), context.getResources().getColor(android.R.color.darker_gray));
                contactsViewHolder.background.setBackgroundColor(application.getCustomTheme().getRowUnselectedColor());
            }
        }
        else
        {
            HeaderViewHolder headerViewHolder=(HeaderViewHolder)viewHolder;
            HeaderContact header=(HeaderContact)items.get(i);
            headerViewHolder.sign.setText(header.getSign());
            if(!application.getCustomTheme().getFont().isEmpty()){
                fontManager.setFont(headerViewHolder.sign,application.getCustomTheme().getFont());
            }
            headerViewHolder.view.setBackgroundColor(application.getCustomTheme().getRowSelectedColor());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i==TYPE_DATA)
        {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.item_contact_data, viewGroup, false);
            return new ContactsViewHolder(itemView);
        }
        else
        {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.item_contact_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        }
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name;
        protected TextView contact;
        protected LinearLayout check;
        protected LinearLayout background;

        public ContactsViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            contact = (TextView) v.findViewById(R.id.contact);
            check = (LinearLayout) v.findViewById(R.id.check);
            background = (LinearLayout) v.findViewById(R.id.background);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.itemClickListener = mItemClickListener;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        protected TextView sign;
        protected View view;

        public HeaderViewHolder(View v) {
            super(v);
            sign = (TextView) v.findViewById(R.id.sign);
            view = v;
        }
    }

}