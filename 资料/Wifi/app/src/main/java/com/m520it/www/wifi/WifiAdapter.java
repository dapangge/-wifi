package com.m520it.www.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xmg on 2017/3/6.
 */

public class WifiAdapter extends BaseAdapter {
    private Context mContext;

    private List<ScanResult> dates;

    public List<ScanResult> getDates() {
        return dates;
    }

    public WifiAdapter(Context context, List<ScanResult> dates) {
        mContext = context;
        this.dates=dates;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder=null;
        if(convertView==null){
            mViewHolder=new ViewHolder();
            convertView= View.inflate(mContext, R.layout.item_main, null);
            mViewHolder.wifiName = (TextView) convertView.findViewById(R.id.tv_wifiName);
            mViewHolder.wifiLeve = (TextView) convertView.findViewById(R.id.tv_wifiLeve);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        /**
         * 数据的展示
         */
        ScanResult scanResult = dates.get(position);
        if(scanResult!=null){
            mViewHolder.wifiName.setText(scanResult.SSID+"");
            mViewHolder.wifiLeve.setText(scanResult.level+"");
        }

        return convertView;
    }

    static class ViewHolder{

        TextView wifiName;
        /**
         * wifi信号
         */
        TextView wifiLeve;

    }


}
