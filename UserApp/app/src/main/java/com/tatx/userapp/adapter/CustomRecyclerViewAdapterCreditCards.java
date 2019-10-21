package com.tatx.userapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tatx.userapp.R;
import com.tatx.userapp.commonutills.Common;
import com.tatx.userapp.constants.ServiceUrls;
import com.tatx.userapp.customviews.LongClickableRelativeLayout;
import com.tatx.userapp.menuactivity.AccountActivity;
import com.tatx.userapp.network.RetrofitRequester;
import com.tatx.userapp.pojos.Card;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Venkateswarlu SKP on 14-11-2016.
 */
public class CustomRecyclerViewAdapterCreditCards extends RecyclerView.Adapter<CustomRecyclerViewAdapterCreditCards.MyViewHolder>
{

    private AccountActivity accountActivity;
    private List<Card> cardList;


    public CustomRecyclerViewAdapterCreditCards(AccountActivity accountActivity, List<Card> cardList)
    {
        this.accountActivity = accountActivity;
        this.cardList = cardList;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_card_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final Card card = cardList.get(position);

        holder.ivCreditCardImage.setBackgroundResource(Common.getCardBackgroundById(Integer.parseInt(card.brand)).getCardDrawableBig());

        holder.tvCreditCardNumber.setText(card.number);

        holder.rbCreditCardSelector.setChecked(Integer.parseInt(card.primary) == 1);

        holder.rlCcDetailsParent.setTag(card.id);

        holder.rlCcDetailsParent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                HashMap<String, String> requestParams = new HashMap<String, String>();

                requestParams.put(ServiceUrls.RequestParams.CARD_ID, String.valueOf(card.id));

                new RetrofitRequester(accountActivity).sendStringRequest(ServiceUrls.RequestNames.CHANGE_CARD,requestParams);



            }


        });



    }

    @Override
    public int getItemCount()
    {
        return cardList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView ivCreditCardImage;

        TextView tvCreditCardNumber;

        RadioButton rbCreditCardSelector;

        private final LongClickableRelativeLayout rlCcDetailsParent;


        public MyViewHolder(View view)
        {
            super(view);
            ivCreditCardImage = (ImageView) view.findViewById(R.id.iv_credit_card_image);
            tvCreditCardNumber = (TextView) view.findViewById(R.id.tv_credit_card_number);
            rbCreditCardSelector = (RadioButton) view.findViewById(R.id.rb_credit_card_selector);
            rlCcDetailsParent = (LongClickableRelativeLayout) view.findViewById(R.id.rl_cc_details_parent);


        }


    }
}
