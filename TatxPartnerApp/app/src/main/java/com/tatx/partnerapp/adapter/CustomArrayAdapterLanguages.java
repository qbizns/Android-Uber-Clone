package com.tatx.partnerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tatx.partnerapp.R;
import com.tatx.partnerapp.enums.Language;


/**
 * Created by Venkateswarlu SKP on 23-09-2016.
 */
public class CustomArrayAdapterLanguages extends ArrayAdapter<Language>
{

    private final Context context;
    private final int resource;
    private final Language[] languages;

    public CustomArrayAdapterLanguages(Context context, int resource, Language[] languages)
    {
        super(context, resource, R.id.tv_language,languages);
        this.context = context;
        this.resource = resource;
        this.languages = languages;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View inflatedView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvLanguage = (TextView) inflatedView.findViewById(R.id.tv_language);

        tvLanguage.setText(languages[position].getLanguageName());

        return inflatedView;
    }
}
