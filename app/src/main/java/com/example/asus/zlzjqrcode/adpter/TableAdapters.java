package com.example.asus.zlzjqrcode.adpter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.huadong.Product;
import com.example.asus.zlzjqrcode.utils.Goods;
import com.example.asus.zlzjqrcode.utils.TableAdapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;



/**
 * Created by asus on 2018/1/22.
 */

public class TableAdapters extends BaseAdapter {

    private JSONArray list;
    private LayoutInflater inflater;


    public TableAdapters(Context context, JSONArray list){
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null){
            Log.e("大小",list.size()+"");
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        Goods product = (Goods) this.getItem(position);

        JSONObject item = list.getJSONObject(position);
        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.layout_suiyi, null);
            viewHolder.goodId = (TextView) convertView.findViewById(R.id.suiyi_item_1);
			viewHolder.goodName = (TextView) convertView.findViewById(R.id.suiyi_item_2);
			viewHolder.goodCodeBar_1 = (TextView) convertView.findViewById(R.id.suiyi_item_3_1);
            viewHolder.goodCodeBar_2 = (TextView) convertView.findViewById(R.id.suiyi_item_3_2);
            viewHolder.goodCodeBar_3 = (TextView) convertView.findViewById(R.id.suiyi_item_3_3);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
         viewHolder.goodId.setText(item.getString("areas"));
         viewHolder.goodName.setText(item.getString("name")+" ("+item.getString("total_count")+")");
         viewHolder.goodCodeBar_1.setText(item.getString("subscribe")+"/");
         viewHolder.goodCodeBar_2.setText(item.getString("download_count")+"/");
         viewHolder.goodCodeBar_3.setText(item.getString("user_count"));
         return convertView;
    }

    public static class ViewHolder{
        public TextView goodId;
        public TextView goodName;
        public TextView goodCodeBar_1;
        public TextView goodCodeBar_2;
        public TextView goodCodeBar_3;



    }

}
