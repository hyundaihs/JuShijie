package com.android.jsj.entity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * ChaYin
 * Created by ${蔡雨峰} on 2019/8/9/009.
 */
public class PickerUtil {
    private static List<ProvInfo> options1Items = new ArrayList<>();
    private static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private static List<ChooseType> singleData = new ArrayList<>();

    public static void initAddress(ArrayList<ProvInfo> provInfoList) {
        options1Items.clear();
        options2Items.clear();
        options3Items.clear();
        for (ProvInfo provInfo : provInfoList) {
            options1Items.add(provInfo);
            ArrayList cityInfoList = new ArrayList<String>();
            ArrayList<ArrayList<String>> areaInfoListList = new ArrayList<>();
            for (CityInfo cityInfo : provInfo.getLists()) {
                cityInfoList.add(cityInfo.getTitle());
                ArrayList<String> areaInfoList = new ArrayList<>();
                if (cityInfo.getLists() != null) {
                    for (AreaInfo areaInfo : cityInfo.getLists()) {
                        areaInfoList.add(areaInfo.getTitle());
                    }
                }
                areaInfoListList.add(areaInfoList);
            }
            options2Items.add(cityInfoList);
            options3Items.add(areaInfoListList);
        }
    }

    public static void showAddress(Context context, OnOptionsSelectListener onOptionsSelectListener) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, onOptionsSelectListener)
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    public static void initChooseType(ArrayList<ChooseType> data) {
        singleData.clear();
        singleData.addAll(data);
    }

    public static void showChooseType(Context context, String title, OnOptionsSelectListener onOptionsSelectListener) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, onOptionsSelectListener)
                .setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(singleData);
        pvOptions.show();
    }
}
