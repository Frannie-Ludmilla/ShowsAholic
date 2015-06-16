package com.frannie.showsaholic;

/**
 * Created by Francesca on 15/06/2015.
 */
import org.apache.http.client.methods.HttpUriRequest;
import com.frannie.showsaholic.HTTPDownloadTask;

import java.net.URL;

public abstract class HTTPHandler {

    public abstract HttpUriRequest getHttpRequestMethod();
    //public abstract URL getHttpRequestMethod();

    public abstract void onResponse(String result);

    public void execute(){
        new HTTPDownloadTask(this).execute();
    }
}
