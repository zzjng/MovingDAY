package com.example.thefirstmove;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SharePhotoActivity extends AppCompatActivity {
    //需要适配的数据
    private String[] titles={"桌子","苹果","蛋糕","线衣","猕猴桃","围巾"};
    private String[] prices={"1800元","10元/kg","300元","350元","10元/kg","280元"};
    //图片集合
    private  int[] icons={R.drawable.table,R.drawable.apple,R.drawable.cake,
            R.drawable.wearclothes,R.drawable.kiwifruit,R.drawable.scarf};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_photo);
        //初始化ListView控件
        ListView listView=findViewById(R.id.lv);
        //创建一个Adapter的实例
        MyBaseAdapter mAdapter=new MyBaseAdapter();
        //设置Adapter
        listView.setAdapter(mAdapter);
    }
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount(){       //得到item的总数
            return titles.length;    //返回ListView Item条目代表的对象
        }

        @Override
        public Object getItem(int position){
            return titles[position]; //返回item的数据对象
        }
        @Override
        public long getItemId(int position){
            return position;         //返回item的id
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){//获取item中的View视图
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(SharePhotoActivity.this,R.layout.l, null);
                holder=new ViewHolder();
                holder.title=convertView.findViewById(R.id.title);
                holder.price=convertView.findViewById(R.id.price);
                holder.iv=convertView.findViewById(R.id.iv);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            holder.title.setText(titles[position]);
            holder.price.setText(prices[position]);
            holder.iv.setImageResource(icons[position]);
            return convertView;
        }
    }
    class ViewHolder{
        TextView title;
        TextView price;
        ImageView iv;

    }
}

