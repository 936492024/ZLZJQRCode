package com.example.asus.zlzjqrcode.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.ToastUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by asus on 2018/1/18.
 */

public class MyListViewAdapter extends BaseAdapter {
    private JSONArray jsonArray=new JSONArray();
    private Context context;
    private int flag;
    private int defItem;//声明默认选中的项


    public MyListViewAdapter(Context context,JSONArray jsonArray ,int flag) {
        this.jsonArray = jsonArray;
        this.context=context;
        this.flag=flag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JSONObject item = jsonArray.getJSONObject(position);
        ViewHolder viewHolder;
        if(flag==1){
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.item_left_2, null);
                viewHolder.tv_item_2 = (TextView)convertView.findViewById(R.id.tv_item_2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            if (defItem == 0) {
                convertView.setBackgroundResource(R.color.primary_blue);
            } else {
                convertView.setBackgroundResource(android.R.color.white);
            }
            viewHolder.tv_item_2.setText(item.getString("citys"));
        }else {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.item_left_3, null);
                viewHolder.tv_item_3_text1 = (TextView)convertView.findViewById(R.id.tv_item_3_text1);
                viewHolder.tv_item_3_text2 = (TextView)convertView.findViewById(R.id.tv_item_3_text2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.tv_item_3_text1.setText(item.getString("areas"));
            viewHolder.tv_item_3_text2.setText(item.getString("name")+"("+item.getString("total_count")+"人)");
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return jsonArray.size();

    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        TextView tv_item_2,tv_item_3_text1,tv_item_3_text2;
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }
}
