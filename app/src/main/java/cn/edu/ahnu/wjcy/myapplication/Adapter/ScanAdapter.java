package cn.edu.ahnu.wjcy.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.edu.ahnu.wjcy.myapplication.Model.ScanResult;
import cn.edu.ahnu.wjcy.myapplication.Model.eventBus.ScanDeleteEvent;
import cn.edu.ahnu.wjcy.myapplication.R;

/**
 * 创 建 人： 燕归来兮
 * 电子邮箱：zhoutao_it@126.com
 * 个人博客：http://www.zhoutaotao.xyz
 * 创建信息： 创建于2017/10/1 21:27，文件位于cn.edu.ahnu.wjcy.myapplication.Adapter
 * 文件作用：
 */

public class ScanAdapter extends BaseAdapter {
    private ArrayList<ScanResult> arrayList;
    private Context context;

    public ScanAdapter(ArrayList<ScanResult> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.scan_layout, null);

            holder = new ViewHolder();
            holder.bgLL = (LinearLayout) convertView.findViewById(R.id.bg_ll);
            holder.titleTV = (TextView) convertView.findViewById(R.id.book_title);
            holder.discountIV = (ImageView) convertView.findViewById(R.id.book_discount);
            holder.priceTV = (TextView) convertView.findViewById(R.id.book_price);
            holder.discountPriceTV = (TextView) convertView.findViewById(R.id.book_discount_price);
            holder.deleteRL = (RelativeLayout) convertView.findViewById(R.id.delete_rl);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==arrayList.size()-1){
            holder.bgLL.setBackgroundResource(R.color.scan_blue);
        }else{
            holder.bgLL.setBackgroundResource(R.color.white);
        }

        ScanResult scanResult = arrayList.get(position);
        if(scanResult.isDiscount()){
            holder.discountIV.setVisibility(View.VISIBLE);
        }else{
            holder.discountIV.setVisibility(View.GONE);
        }
        holder.titleTV.setText(scanResult.getItemName());
        holder.discountPriceTV.setText(scanResult.getItemSalePrice()+"");
        holder.priceTV.setText(scanResult.getItemPrice()+"");
        holder.deleteRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(position);
                notifyDataSetChanged();
                EventBus.getDefault().post(new ScanDeleteEvent());
            }
        });


        return convertView;
    }

    class ViewHolder{
        LinearLayout bgLL;
        TextView titleTV;
        TextView priceTV;
        TextView discountPriceTV;
        ImageView discountIV;
        RelativeLayout deleteRL;
    }
}
