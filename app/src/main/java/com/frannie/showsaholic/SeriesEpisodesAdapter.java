package com.frannie.showsaholic;

/**
 * Created by Francesca on 15/06/2015.
 * see: http://www.vogella.com/tutorials/AndroidListView/article.html#adapterperformance_holder
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

public class SeriesEpisodesAdapter extends BaseExpandableListAdapter {

    private SparseArray<Group> groups;
    public LayoutInflater inflater;
    public Activity activity;
    public Context ctx;

    public SeriesEpisodesAdapter(Activity act, SparseArray<Group> groups) {
        activity = act;
        ctx= act.getApplicationContext();
        this.groups = groups;
        inflater = act.getLayoutInflater();
        Log.v("AdapterSizeGroup",""+groups.size() );
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final EpisodeItem children = (EpisodeItem) getChild(groupPosition, childPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandablelistdetail_episodes, null);
        }
        text = (TextView) convertView.findViewById(R.id.textSeasonEpisode);
        text.setText(children.seasonnum+" - "+children.title);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO: pass a Parcelable Episode Info


                Bundle data = new Bundle();
                EpisodeItem newItem= children;
                data.putParcelable("current_ep", newItem);
                Intent in = new Intent(activity, EpisodeScreen.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtras(data);
                Toast.makeText(activity, children.seasonnum+" - "+children.title,
                        Toast.LENGTH_SHORT).show();
                ctx.startActivity(in);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandablelist_row, null);
        }
        Group group = (Group) getGroup(groupPosition);
        if(group==null){
            Log.e("ERROR", "Group empty");


        }
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
