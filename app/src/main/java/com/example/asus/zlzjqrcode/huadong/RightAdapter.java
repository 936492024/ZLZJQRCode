package com.example.asus.zlzjqrcode.huadong;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.asus.zlzjqrcode.R;

import java.util.List;

/**
 * Created by fangzhu on 2015/4/23.
 */
public class RightAdapter extends ArrayAdapter<Product> {
    List<Product> objects;
    public RightAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_right, null);
            viewHolder.tv_item2 = (TextView)convertView.findViewById(R.id.tv_item2);
            viewHolder.tv_item3 = (TextView)convertView.findViewById(R.id.tv_item3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Product product = objects.get(position);
        viewHolder.tv_item2.setText(product.getMobile());
        viewHolder.tv_item3.setText(product.getCount());//(product.getSell() - product.getLastClose()) * 100 / product.getSell() + ""
        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();

    }

    class ViewHolder {
        TextView tv_item1, tv_item2, tv_item3;
    }
}
