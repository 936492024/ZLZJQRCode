package com.example.asus.zlzjqrcode.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;
import com.example.asus.zlzjqrcode.utils.Goods;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/1/22.
 */

public class TableAdapter_2 extends BaseAdapter {

    private ArrayList<String> list;
    private LayoutInflater inflater;
    private int z;
    private String name ;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> all_key = new ArrayList<>();




    public TableAdapter_2(Context context, ArrayList<String>list ,ArrayList<String> all_key,int z){
        this.list = list;
        this.z=z;
        this.all_key=all_key;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
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
        arrayList.clear();
//        Goods product = (Goods) this.getItem(position);
//        jsonObject2 =list.getJSONArray(position);
//        Log.e("用户数据大小",list.toString());
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_suiyi, null);
            viewHolder.goodId = (TextView) convertView.findViewById(R.id.suiyi_item_2);
            viewHolder.goodName = (TextView) convertView.findViewById(R.id.suiyi_item_1);
//            viewHolder.goodCodeBar = (TextView) convertView.findViewById(R.id.suiyi_item_3);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if(z==1){
//            viewHolder.goodId.setText(list.get(position));
//        }else if(z==2){
//            JSONArray jsonArray = JSONArray.fromObject(list.get(position));
//            String num="";
//            for(int i=0;i<jsonArray.size();i++){
//                JSONObject jsonObject =jsonArray.getJSONObject(i);
//                if(jsonArray.size()==1){
//                    num =num+(jsonObject.getString("name")+"("+jsonObject.getString("total_count")+")");
//                }else if((i>0&&i<jsonArray.size()-1)||i==1){
//                    num =num+"、"+(jsonObject.getString("name")+"("+jsonObject.getString("total_count")+")");
//                }else {
//                    num =num+(jsonObject.getString("name")+"("+jsonObject.getString("total_count")+")");
//                }
//                viewHolder.goodId.setText(num);
//            }
//        }else {
//            JSONArray jsonArray = JSONArray.fromObject(list.get(position));
//            int sum=0;
//            String num="";
//            for(int i=0;i<jsonArray.size();i++){
//                JSONObject jsonObject =jsonArray.getJSONObject(i);
//                sum=sum+Integer.valueOf(jsonObject.getString("total_count"));
//                viewHolder.goodId.setText(sum+"");
//            }
//        }
        if(z==4){
            viewHolder.goodName.setText(all_key.get(position));
            JSONArray jsonArray = JSONArray.fromObject(list.get(position));
            String num="";
            for(int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                if(jsonArray.size()==1){
                    num =num+(jsonObject.getString("name")+"("+jsonObject.getString("total_count")+")");
                }else if((i>0&&i<jsonArray.size()-1)||i==1){
                    num =num+"、"+(jsonObject.getString("name")+"("+jsonObject.getString("total_count")+")");
                }else {
                    num =num+(jsonObject.getString("name")+"("+jsonObject.getString("total_count")+")");
                }
                viewHolder.goodId.setText(num);
            }
            int sums=0;
            int sums_2=0;
            int sums_3=0;
            for(int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                sums=sums+Integer.valueOf(jsonObject.getString("subscribe"));
                sums_2=sums_2+Integer.valueOf(jsonObject.getString("download_count"));
                sums_3=sums_3+Integer.valueOf(jsonObject.getString("user_count"));
                viewHolder.goodCodeBar.setText(sums+"/"+sums_2+"/"+sums_3);
            }
        }
        return convertView;
    }

    public static class ViewHolder{
        public TextView goodId;
        public TextView goodName;
        public TextView goodCodeBar;
    }

}
