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
import com.example.asus.zlzjqrcode.utils.TableAdapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by asus on 2018/1/23.
 */

public class TableAdapter_3 extends BaseAdapter {

    //	private List<Goods> list;
    private LayoutInflater inflater;
    private JSONArray list;
    private int z;


    public TableAdapter_3(Context context, JSONArray list, int z){
        this.list = list;
        this.z=z;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null){
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

//		Goods goods = (Goods) this.getItem(position);
        JSONObject item = list.getJSONObject(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.tableadapter_fina, null);
            viewHolder.goodId = (TextView) convertView.findViewById(R.id.tv_item_2);
            viewHolder.tv_item_3 = (TextView) convertView.findViewById(R.id.tv_item_3);

//			viewHolder.goodName = (TextView) convertView.findViewById(R.id.text_goods_name);
//			viewHolder.goodCodeBar = (TextView) convertView.findViewById(R.id.text_codeBar);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(z==1){

        }else if(z==2){
            if(item.getString("is_show").equals("0")){
                Log.e("名字",item.getString("name"));
//                viewHolder.goodId.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                viewHolder.goodId.setText(item.getString("name")+"（已停用）");
            }else if(item.getString("is_show").equals("1")){
                viewHolder.goodId.setText(item.getString("name"));
            }
            viewHolder.tv_item_3.setText(item.getString("areas"));
        }

        return convertView;
    }

    public static class ViewHolder{
        public TextView goodId;
        public TextView tv_item_3;
        public TextView goodCodeBar;
    }

}
