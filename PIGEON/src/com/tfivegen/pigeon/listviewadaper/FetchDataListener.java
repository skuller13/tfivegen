package com.tfivegen.pigeon.listviewadaper;

import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<Application> data);
    public void onFetchFailure(String msg);
}
