package com.amap.poisearch.search;

import android.content.Context;

import com.amap.api.services.core.PoiItem;

import java.util.List;

/**
 * Created by liangchao_suxun on 2017/9/5.
 */

public interface IPoisearchDelegate {

    public static interface Callback{
        public void onSucc(List<PoiItem> poiItems);

        public void onFail(int errCode, String errMsg);
    }

    public void doSearch(Context context, String adCode, String keyword, final Callback callback);

}
