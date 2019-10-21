package com.tatx.userapp.enums;

import java.util.HashMap;

/**
 * Created by Venkateswarlu SKP on 23-09-2016.
 */
public enum Language
{

    ENGLISH(0,"English","en"),
    ARABIC(1, "Arabic","ar");




    private final int languageCode;
    private final String languageName;
    private final String localeCode;


    Language(int languageCode, String languageName, String localeCode)
    {
        this.languageCode = languageCode;
        this.languageName = languageName;
        this.localeCode = localeCode;

    }

    static HashMap<Integer,Language> enumFieldsByLanguageCode = new HashMap<Integer,Language>();

    static HashMap<String,Language> enumFieldsByLocaleCode = new HashMap<String,Language>();

    static
    {

        for (Language language:values())
        {

            enumFieldsByLanguageCode.put(language.languageCode,language);
            enumFieldsByLocaleCode.put(language.localeCode,language);

        }


    }

    public String getLanguageName()
    {
        return languageName;
    }

    public int getLanguageCode()
    {
        return languageCode;
    }

    public String getLocaleCode() { return localeCode; }

    public static Language getEnumFieldByLanguageCode(int languageCode)
    {
        return enumFieldsByLanguageCode.get(languageCode);
    }

    public static Language getEnumFieldByLocaleCode(String localeCode) {
        return enumFieldsByLocaleCode.get(localeCode);

    }


}
