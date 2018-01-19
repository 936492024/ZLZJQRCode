package com.example.asus.zlzjqrcode.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by asus on 2017/10/19.
 */

public class Listview_Adapter extends android.widget.BaseAdapter{
    Context context;
    ArrayList<String> list = new ArrayList();
    public Listview_Adapter(Context _context, ArrayList list) {
        this.list = list;
        this.context = _context;
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
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.listview_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView textView;
    }
}
