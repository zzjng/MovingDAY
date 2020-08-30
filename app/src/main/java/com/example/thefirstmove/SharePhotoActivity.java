package com.example.thefirstmove;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thefirstmove.bean.AaaBean;
import com.example.thefirstmove.sqlite.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SharePhotoActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private List<AaaBean> textList=new ArrayList<AaaBean>();
    private EditText d1,a1,a2,a3,a4,a5,u1,u2,u3,u4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_photo);
        TextAdapter adapter=new TextAdapter(SharePhotoActivity.this,R.layout.l,textList);
        ListView listView=findViewById(R.id.lv);
        listView.setAdapter(adapter);
        dbHelper = new MyDatabaseHelper(this, "16204204.db", null, 1);
        dbHelper.getWritableDatabase();
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sql = "select * from aaatable where id=? ";
        Cursor cursor = db.rawQuery(sql, new String[] {"a"});
        if (cursor.moveToFirst()) {
            do {
                String nick = cursor.getString(cursor.getColumnIndex("nick"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                //byte[] img=cursor.getBlob(cursor.getColumnIndex("img"));
                /*Bitmap bmpout = BitmapFactory.decodeByteArray(img, 0, img.length);*/
                AaaBean user = new AaaBean(nick,address,description,id);
                /*BitmapDrawable bd= new BitmapDrawable(getResources(), bmpout);
                ImageView cameraPic = (ImageView) findViewById(R.id.tv_img);
                cameraPic.setImageBitmap(bmpout);*/

                textList.add(user);

            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    public class TextAdapter extends ArrayAdapter<AaaBean>{//适配器
        private int resourceId;
        public TextAdapter(Context context, int textViewResourceId, List<AaaBean> objects){
            super(context,textViewResourceId,objects);
            resourceId=textViewResourceId;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AaaBean t=getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            TextView nick=(TextView)view.findViewById(R.id.tv_nick);
            TextView address=(TextView)view.findViewById(R.id.tv_address);
            TextView description=(TextView)view.findViewById(R.id.tv_description);
            //ImageView cameraPic = (ImageView) findViewById(R.id.tv_img);

           /* ByteArrayInputStream bais = null;
            byte[] img=t.getImg();
                //将字节数组转化为位图
            *//*Object s=simg;
            byte[] img= null;
            img= Base64.decode(simg, Base64.DEFAULT);
            System.out.println(simg+"看这儿String");
            System.out.println(s+"看这儿object");*//*
            System.out.println(img+"看这儿byte");
            //if(img!=null){
                BitmapFactory.Options opts = new BitmapFactory.Options();
                Bitmap imagebitmap = BitmapFactory.decodeByteArray(img, 0, img.length,opts);
            if (imagebitmap!=null) {
                //将位图显示为图片
                cameraPic.setImageBitmap(imagebitmap);
            }else {
                //cameraPic.setImageResource(R.drawable.apple);
            }*/

            //cameraPic.setImageBitmap(bmpout);
            /*if (img != null) {
                bais = new ByteArrayInputStream(img);
                cameraPic.setImageBitmap(BitmapFactory.decodeByteArray(img,0,img.length));//把图片设置到ImageView对象中
            }*/

            nick.setText(t.getNick());
            address.setText(t.getAddress());
            description.setText(t.getDescription());
            return view;
        }
    }

}



    /*//需要适配的数据
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

    }*/


