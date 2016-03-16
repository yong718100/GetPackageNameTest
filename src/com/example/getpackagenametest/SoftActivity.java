package com.example.getpackagenametest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
  
public class SoftActivity extends Activity implements Runnable ,OnItemClickListener{  
      
    private List<Map<String, Object>> list = null;  
    private ListView softlist = null;  
    private ProgressDialog pd;  
    private Context mContext;  
    private PackageManager mPackageManager;  
    private List<ResolveInfo> mAllApps;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        setContentView(R.layout.software);  
        setTitle("文件管理器");  
          
        mContext = this;  
        mPackageManager = getPackageManager();  
          
        softlist = (ListView) findViewById(R.id.softlist);  
  
         pd = ProgressDialog.show(this, "请稍候..", "正在收集软件信息...", true,false);  
         Thread thread = new Thread(this);  
         thread.start();  
  
        super.onCreate(savedInstanceState);  
    }  
    /** 
     * 检查系统应用程序，添加到应用列表中 
     */  
    private void bindMsg(){  
        //应用过滤条件  
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
        mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);  
        softlist.setAdapter(new MyAdapter(mContext, mAllApps));  
        softlist.setOnItemClickListener(this);  
        //按包名排序  
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));  
          
    }  
      
    @Override  
    public void run() {  
        bindMsg();  
        handler.sendEmptyMessage(0);  
  
    }  
    private Handler handler = new Handler() {  
        public void handleMessage(Message msg) {  
            pd.dismiss();  
        }  
    };  
      
    class MyAdapter extends BaseAdapter{  
  
        private Context context;  
        private List<ResolveInfo> resInfo;  
        private ResolveInfo res;  
        private LayoutInflater infater=null;     
          
        public MyAdapter(Context context, List<ResolveInfo> resInfo) {              
            this.context = context;  
            this.resInfo = resInfo;  
             infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        }  
  
        @Override  
        public int getCount() {  
              
            return resInfo.size();  
        }  
  
        @Override  
        public Object getItem(int arg0) {  
              
            return arg0;  
        }  
  
        @Override  
        public long getItemId(int position) {  
              
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
              
        //  View view = null;    
            ViewHolder holder = null;    
            if (convertView == null || convertView.getTag() == null) {    
                convertView = infater.inflate(R.layout.soft_row, null);    
                holder = new ViewHolder(convertView);    
                convertView.setTag(holder);    
            }     
            else{    
           //     view = convertView ;    
                holder = (ViewHolder) convertView.getTag() ;    
            }    
            //获取应用程序包名，程序名称，程序图标  
            res = resInfo.get(position);  
            holder.appIcon.setImageDrawable(res.loadIcon(mPackageManager));    
            holder.tvAppLabel.setText(res.loadLabel(mPackageManager).toString());  
            holder.tvPkgName.setText(res.activityInfo.packageName+'\n'+res.activityInfo.name);  
            return convertView;  
              
            /*convertView = LayoutInflater.from(context).inflate(R.layout.soft_row, null); 
                     
            app_icon = (ImageView)convertView.findViewById(R.id.img); 
            app_tilte = (TextView)convertView.findViewById(R.id.name); 
            app_des = (TextView)convertView.findViewById(R.id.desc); 
 
            res = resInfo.get(position); 
            app_icon.setImageDrawable(res.loadIcon(mPackageManager)); 
            app_tilte.setText(res.loadLabel(mPackageManager).toString()); 
            app_des.setText(res.activityInfo.packageName+'\n'+res.activityInfo.name); 
 
            return convertView;*/  
        }  
    }  
    //设定界面布局  
    class ViewHolder {    
        ImageView appIcon;    
        TextView tvAppLabel;    
        TextView tvPkgName;    
    
        public ViewHolder(View view) {    
            this.appIcon = (ImageView) view.findViewById(R.id.img);    
            this.tvAppLabel = (TextView) view.findViewById(R.id.name);    
            this.tvPkgName = (TextView) view.findViewById(R.id.desc);    
        }    
    }    
    /** 
     * 单击应用程序后进入系统应用管理界面 
     */  
    @Override  
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {  
          
        ResolveInfo res = mAllApps.get(position);  
        //该应用的包名和主Activity  
        String pkg = res.activityInfo.packageName;  
        String cls = res.activityInfo.name;  
          
        ComponentName componet = new ComponentName(pkg, cls);  
          
        Intent i = new Intent();  
        i.setComponent(componet);  
        startActivity(i);  
          
    }  
  
}  
