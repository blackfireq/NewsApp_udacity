package com.example.android.newsapp_udacity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mikem on 6/6/2017.
 */

public class NewsAdapter  extends ArrayAdapter<News>{

    public NewsAdapter(Activity context, ArrayList<News> News) {super(context, 0, News);}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //getters

        //get the position of current object in the ArrayList
        News currentStory = getItem(position);

        //get the view that will diplay the main title of the current News story
        TextView title = (TextView)convertView.findViewById(R.id.title_view);

        //get the view that will diplay the section of the current News story
        TextView section = (TextView)convertView.findViewById(R.id.section_view);

        //get the view that will diplay the date published of the current News story
        TextView datePublished = (TextView)convertView.findViewById(R.id.date_published_view);


        //setters

        //set the title of the current news story
        title.setText(currentStory.getTitle());

        //set the section of the current news story
        section.setText(currentStory.getSection());

        //set the date published of the current news story
        datePublished.setText(currentStory.getDatePublished());




        return convertView;
    }
}
