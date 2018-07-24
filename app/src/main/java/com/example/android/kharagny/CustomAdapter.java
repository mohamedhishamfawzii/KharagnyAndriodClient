package com.example.android.kharagny;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    // an int array containing the ids of images we want to add to the listView -> this approach is taken for future proofing so we could have the ability to allow user defined categories later on
    private int[] images = {R.drawable.cat1 , R.drawable.cat2 , R.drawable.cat3 , R.drawable.cat4 , R.drawable.cat5};
    public CustomAdapter(@NonNull Context context, ArrayList<String> accounts) { //
        // defining the layout of a single list item
        super(context,R.layout.custom_row, accounts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row , parent , false);
        String singleAccount = getItem(position);
        int currentImageID = images[position];
        TextView usernameTxt = (TextView)customView.findViewById(R.id.categoriesTxt);
        ImageView userImage = (ImageView)customView.findViewById(R.id.imageView);
        usernameTxt.setText(singleAccount);
        userImage.setImageResource(currentImageID);
        return customView;

    }
}
