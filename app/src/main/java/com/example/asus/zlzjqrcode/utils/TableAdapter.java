package com.example.asus.zlzjqrcode.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.zlzjqrcode.R;

import java.util.List;


public class TableAdapter extends BaseAdapter {
	
	private List<Goods> list;
	private LayoutInflater inflater;
	
	public TableAdapter(Context context, List<Goods> list){
		this.list = list;
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
		
		Goods goods = (Goods) this.getItem(position);
		
		ViewHolder viewHolder;
		
		if(convertView == null){
			
			viewHolder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.list_item, null);
			viewHolder.goodId = (TextView) convertView.findViewById(R.id.text_id);
			viewHolder.goodName = (TextView) convertView.findViewById(R.id.text_goods_name);
			viewHolder.goodCodeBar = (TextView) convertView.findViewById(R.id.text_codeBar);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.goodId.setText(goods.getAreas());
		viewHolder.goodId.setTextSize(13);
		viewHolder.goodName.setText(goods.getCount());
		viewHolder.goodName.setTextSize(13);
		viewHolder.goodCodeBar.setText(goods.getYonghu());
		viewHolder.goodCodeBar.setTextSize(13);
		
		return convertView;
	}
	
	public static class ViewHolder{
		public TextView goodId;
		public TextView goodName;
		public TextView goodCodeBar;
	}
	
}
