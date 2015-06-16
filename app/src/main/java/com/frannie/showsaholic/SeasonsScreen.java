package com.frannie.showsaholic;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.ExpandableListView;

/**
 * Created by Francesca on 15/06/2015.
 */
public class SeasonsScreen extends Activity {
    // more efficient than HashMap for mapping integers to objects
    SparseArray<Group> groups = new SparseArray<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelistseasons);
        createData();
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.listSeasonsExpandable);
        SeriesEpisodesAdapter adapter = new SeriesEpisodesAdapter(this, groups);
        listView.setAdapter(adapter);
    }

    public void createData() {
        for (int j = 0; j < 5; j++) {
            Group group = new Group("Test " + j);
            for (int i = 0; i < 5; i++) {
                group.children.add("Sub Item" + i);
            }
            groups.append(j, group);
        }
    }

}

