package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tatx.partnerapp.R;
import com.tatx.partnerapp.commonutills.Common;
import com.tatx.partnerapp.dataset.Document;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Subbu on 6/19/2015.
 */
public class DocumentsListAdapterCopy extends RecyclerView.Adapter<DocumentsListAdapterCopy.ViewHolder> {
    Context context;
    List<Document> documentsList;
    String code;
    private OnItemClickListener mListener;



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public DocumentsListAdapterCopy(Context context, List<Document> documentsList) {
        this.context = context;
        this.documentsList = documentsList;

    }

    @Override
    public int getItemCount() {
        // return productDetailses.size();
        return (null != documentsList ? documentsList.size() : 0);
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_example, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.document_image)
        ImageView image;
        @BindView(R.id.iv_edit_documents)
        ImageView ivEditDocuments;

        public ViewHolder(final View vi) {
            super(vi);
            ButterKnife.bind(this,vi);
            vi.setOnClickListener(this);
            ivEditDocuments.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

                if (mListener != null) {
                    mListener.onItemClick(v, getPosition());
                }
        }
    }
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.title.setText(documentsList.get(position).docName);
            Picasso.with(context).load(documentsList.get(position).doc).resize((int)Common.getDimensionResourceValue(R.dimen._150sdp),(int)Common.getDimensionResourceValue(R.dimen._150sdp))
                                 .into(holder.image);
  //          holder.image.setBackgroundResource(R.drawable.image_background);
           holder.image.setPadding(3,3,3,3);



        }

        }



