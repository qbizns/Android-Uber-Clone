package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.dataset.Document;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Subbu on 6/19/2015.
 */
public class DocumentsListAdapter extends BaseAdapter {
    Context context;
    List<Document> documentsList;
    String code;
    private OnItemClickListener mListener;



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return (null != documentsList ? documentsList.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=new ViewHolder();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_example, parent, false);
        ButterKnife.bind(this, v);
        holder.title.setText(documentsList.get(position).docName);
        Picasso.with(context).load(documentsList.get(position).doc)
                .into(holder.image);
        return null;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public DocumentsListAdapter(Context context,  List<Document> documentsList) {
        this.context = context;
        this.documentsList = documentsList;

    }




    public static class ViewHolder   {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.image)
        ImageView image;


    }


        }



