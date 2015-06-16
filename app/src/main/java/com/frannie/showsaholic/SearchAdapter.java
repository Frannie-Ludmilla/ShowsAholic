package com.frannie.showsaholic;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Francesca on 14/06/2015.
 */
public class SearchAdapter extends ArrayAdapter<SearchItem> {

        private Activity myContext;
        private SearchItem[] datas;

        static class ViewHolder {
            public TextView title;
            public TextView season;
            public TextView date;
        }

        public SearchAdapter(Context context, int textViewResourceId,
                             SearchItem[] objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            myContext = (Activity) context;
            datas = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = myContext.getLayoutInflater();
                rowView = inflater.inflate(R.layout.searchitem, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title=(TextView)rowView.findViewById(R.id.searchShowTitle);
                viewHolder.date=(TextView)rowView.findViewById(R.id.searchShowDate);
                viewHolder.season=(TextView)rowView.findViewById(R.id.searchShowSeason);
                rowView.setTag(viewHolder);
            }

            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.title.setText(datas[position].nameSearchItem);
            holder.date.setText(datas[position].dateSearchItem);
            holder.season.setText(datas[position].seasonsSearchItem);



            return rowView;
        }
}
