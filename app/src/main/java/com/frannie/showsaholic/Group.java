package com.frannie.showsaholic;

/**
 * Created by Francesca on 15/06/2015.
 */
import java.util.ArrayList;
import java.util.List;

public class Group {

    public String string;
    //public final List<String> children = new ArrayList<String>();
    public List<EpisodeItem> children;

    public Group(String string) {
        this.string = new String(string);
    }



}
