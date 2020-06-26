package com.amap.poisearch.search;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

import java.util.List;

/**
 * Created by liangchao_suxun on 2017/4/27.
 * AMapSearch使用的帮助类
 */

class AMapSearchUtil {

    public static interface OnSearchResListener {
        public void onSug(List<PoiItem> pois, long searchId);

        public void onFail(int errCode, String msg);
    }

    /**
     * 默认的页数
     */
    private static final int DEF_PAGE = 0;

    /**
     * 默认的单页数量
     */
    private static final int DEF_PAGE_SIZE =15;

    /**
     * poi检索。isCityLimit默认为true，category采用默认类别，页数和每页数量采取了默认值
     * @param context
     * @param keyWord 关键字
     * @param adCode 城市的adcode
     * @param isDistanceSort 是否是距离排序，如果为true，则必须设置中心点。如果为false或者中心点为非法，则为按照权重排序。
     * @param centerLL 中心点
     * @return
     */
    public static void doPoiSearchAysnc(Context context, final long searchId, String keyWord,
                                        String adCode, boolean isDistanceSort, LatLng centerLL , final OnSearchResListener poiListener) {
         doPoiSearchAysnc(context, searchId, keyWord, getCategory(), adCode, true, isDistanceSort, centerLL,
            DEF_PAGE,
            DEF_PAGE_SIZE, poiListener);
    }

    /**
     * Poi检索
     * @param context
     * @param keyWord 关键字
     * @param category 类别。具体类别信息参见：http://lbs.amap.com/api/webservice/download。传入类别时，请传入编码不要传入类别名字
     * @param adCode 城市的adCode
     * @param isCityLimit 是否限制在当前城市内
     * @param isDistanceSort 是否是距离排序，如果为true，则必须设置中心点。如果为false或者中心点为非法，则为按照权重排序。
     * @param centerLL 中心点
     * @param page 页数
     * @param pageSize 每页数量
     * @param poiListener 回调
     * @return
     */
    public static void doPoiSearchAysnc(Context context, final long searchId, String keyWord, String category,
                                             String adCode, boolean isCityLimit, boolean isDistanceSort,
                                             LatLng centerLL,
                                             int page,
                                             int pageSize, final OnSearchResListener poiListener) {

        PoiSearch.Query query = new PoiSearch.Query(keyWord, category, adCode);
        query.setCityLimit(isCityLimit);
        query.setDistanceSort(isDistanceSort);
        if (centerLL != null) {
            query.setLocation(new LatLonPoint(centerLL.latitude, centerLL.longitude));
        }
        query.setPageSize(pageSize);
        query.setPageNum(page);


        PoiSearch poiSearch = new PoiSearch(context, query);

        poiSearch.setOnPoiSearchListener(new OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if (poiResult == null || poiResult.getPois() == null) {
                    poiListener.onFail(-1, "empty poi list");
                    return;
                }
                poiListener.onSug(poiResult.getPois(), searchId);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                //进行poi id搜索的结果回调 ， 此处忽略
            }
        });

        poiSearch.searchPOIAsyn();

    }

    /**
     * 默认类别。 仅不包含公厕类别
     * @return
     */
    private static String getCategory() {
        String arr[] = new String[] {
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "200000", "200100", "200200", "200400",
            "22",
            "97",
            "99"
        };

        StringBuffer stringBuffer = new StringBuffer();
        for(int ind=0; ind<arr.length;ind++) {
            stringBuffer.append(arr[ind]);

            if (ind < arr.length - 1) {
                stringBuffer.append("|");
            }
        }

        return stringBuffer.toString();
    }
}
