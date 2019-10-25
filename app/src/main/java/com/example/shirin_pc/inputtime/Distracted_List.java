package com.example.shirin_pc.inputtime;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.View;
import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;






public class Distracted_List extends AppCompatActivity {



    private List<AppList> installedApps;
    private AppAdapter installedAppAdapter;
    ListView userInstalledApps;
    ArrayList<String> selectedItems;
    private Button button_back;

    //shirin add
   // private CheckBox checkBox;
    //private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distracted__list);
// introduce adpter
        userInstalledApps = (ListView) findViewById(R.id.installed_app_list);

       userInstalledApps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


    ////set multiple selection mode
        installedApps = getInstalledApps();
        installedAppAdapter = new AppAdapter(Distracted_List.this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);
        selectedItems=new ArrayList<String>();

          //set OnItemClickListener to get list of items clicked

        userInstalledApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                //get item
                // you can get name or get packages
                String selectedItem = installedApps.get(i).getPackages();
                if(selectedItems.contains(selectedItem)){

                    selectedItems.remove(selectedItem); }//remove deselected item from the list of selected items
                else{
                    selectedItems.add(selectedItem); }//add selected item to the list of selected items




            }
        });




        // Total Number of Installed-Apps(i.e. List Size)
        String  abc = userInstalledApps.getCount()+"";
        TextView countApps = (TextView)findViewById(R.id.countApps);
        countApps.setText("Total Installed Apps: "+abc);
       // Toast.makeText(this, abc+" Apps", Toast.LENGTH_SHORT).show();


////////////////////////////////////////////////////




        //back button
        button_back= (Button) findViewById(R.id.button_back_mainpage);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Distracted_List.this, MainActivity.class);
                intent.putExtra("KeyList", selectedItems);
               startActivity(intent);
                //openMainActivity();
            }
        });
        // checkbox avaliable
       // checkBox= (CheckBox) findViewById(R.id.checkbox);
       // submit= (Button) findViewById(R.id.submit_distract_list);
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    public void showSelectedItems(View view){
        String selItems="";
        for(String item:selectedItems){
            if(selItems=="")
                selItems=item;
            else
                selItems+="/"+item;
        }
        Toast.makeText(this, selItems, Toast.LENGTH_LONG).show();
    }


    public void openMainActivity() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }





    List<AppList> TestApps;
    private List<AppList> getInstalledApps() {
        PackageManager pm = getPackageManager();
        List<AppList> apps = new ArrayList<AppList>();
        TestApps = new ArrayList<AppList>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);


        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p))) {

                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                String packages = p.applicationInfo.packageName;
                apps.add(new AppList(appName, icon, packages));
                TestApps.add(new AppList(appName, icon, packages));
            }
        }
        return apps;
    }


    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }


    public class AppAdapter extends BaseAdapter {

        public LayoutInflater layoutInflater;
        public List<AppList> listStorage;

        public AppAdapter(Context context, List<AppList> customizedListView) {
            layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listStorage = customizedListView;
        }


        @Override
        public int getCount() {
            return listStorage.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder listViewHolder;
            if(convertView == null){
                listViewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.installed_app_list, parent, false);

                listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.list_app_name);
                listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.app_icon);
                listViewHolder.packageInListView=(TextView)convertView.findViewById(R.id.app_package);

                convertView.setTag(listViewHolder);

            }else{
                listViewHolder = (ViewHolder)convertView.getTag();
            }
            listViewHolder.textInListView.setText(listStorage.get(position).getName());
            listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
            listViewHolder.packageInListView.setText(listStorage.get(position).getPackages());

            return convertView;
        }

        class ViewHolder{
            TextView textInListView;
            ImageView imageInListView;
            TextView packageInListView;

        }
    }

    public class AppList {
        private String name;
        Drawable icon;
        private String packages;

        public AppList(String name, Drawable icon, String packages) {
            this.name = name;
            this.icon = icon;
            this.packages = packages;
        }
        public String getName() {
            return name;
        }
        public Drawable getIcon() {
            return icon;
        }
        public String getPackages() {
            return packages;
        }

    }


}
//    public void onCheckboxClicked(View view) {
//        boolean checked = ((CheckBox) view).isChecked();
//        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
//        int index = ((CheckBox) view).getId();
//        Integer.toString(index);
//        Toast.makeText(this,Integer.toString(index), Toast.LENGTH_LONG).show();
//        if (checked==true){
//
//            Toast.makeText(this, TestApps.get(0).getName(), Toast.LENGTH_LONG).show();
//        }
//    }


///////////////////////////////////////////////
//    public void onCheckboxClicked(View view) {
//
//        boolean checked = ((CheckBox) view).isChecked();
////        PackageManager pm = getPackageManager();
////        List<AppList> apps = new ArrayList<AppList>();
////
////        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
//        //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
//
//        if (checked == true) {
//            int i=userInstalledApps.getId();
//            String s = Integer.toString(i);
//
//            //Cursor c = (Cursor)mListView.getItemAtPosition(position);
//          // String s=installedApps.get(0).getName();
//
//            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
//        }
//
//    }

//Checklist checked
//    public void onCheckboxClicked(View view) {
//        // Is the view now checked?
//        boolean checked = ((CheckBox) view).isChecked();
//
//
//    }