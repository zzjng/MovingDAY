package com.amap.poisearch.search;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.search.AMapSearchUtil.OnSearchResListener;

import java.util.List;

/**
 * Created by liangchao_suxun on 2017/9/5.
 */

public class poiSearchDelegateImpl implements IPoisearchDelegate {

    /**
     * 只对最新的请求返回的数据进行回调
     */
    private long mCurrSearchId = 0;

    @Override
    public void doSearch(Context context, String adCode, String keyword, final Callback callback) {
        mCurrSearchId = System.currentTimeMillis();

        if (callback == null) {
            return;
        }

        AMapSearchUtil.doPoiSearchAysnc(context, mCurrSearchId, keyword, adCode, false, null, new OnSearchResListener() {
            @Override
            public void onSug(final List<PoiItem> pois, final long searchId) {
                // 只取最新的结果
                if (searchId < mCurrSearchId) {
                    return;
                }

                if (callback == null) {
                    return;
                }

                callback.onSucc(pois);
            }

            @Override
            public void onFail(final int errCode, final String msg) {
                if (callback == null) {
                    return;
                }

                callback.onFail(errCode, msg);
            }
        });
    }
}
