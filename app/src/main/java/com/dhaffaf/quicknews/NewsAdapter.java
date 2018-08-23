package com.dhaffaf.quicknews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by WIN8 on 05/01/18.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private static final String LOCATION_SEPARATOR = " \n ";
    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
        }
        News nowNews = getItem(position);

        TextView ftitle = (TextView) listItemView.findViewById(R.id.news_title_fPart);
        ftitle.setText(nowNews.getTitle());

        TextView type = (TextView) listItemView.findViewById(R.id.type);
        type.setText(nowNews.getSection());

        TextView autherF = (TextView) listItemView.findViewById(R.id.writerF);
        autherF.setText(nowNews.getAuthorF());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(nowNews.getDate());

        TextView tag = (TextView) listItemView.findViewById(R.id.typeTag);
        tag.setText(nowNews.getType());

        return listItemView;
    }

}
