package com.example.android.kharagny;

import java.util.HashMap;

public class User
{
    private String displayName;
    private String email;
    private String photoUrl;
    private HashMap<String,String> preferences;

    public User()
    {

    }

    public User(String displayName, String email, String photoUrl, HashMap<String, String> preferences)
    {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.preferences = preferences;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhotoUrl()
    {
        return photoUrl;
    }

    public HashMap<String, String> getPreferences()
    {
        return preferences;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl)
    {
        this.photoUrl = photoUrl;
    }

    public void setPreferences(HashMap<String, String> preferences)
    {
        this.preferences = preferences;
    }
}
