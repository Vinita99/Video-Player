package com.example.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView myRecyclerView;
    MyAdapter obj_adapter;
    public static int REQUEST_PERMISSION=1;
    boolean bool_permission;
    File directory;
    public static ArrayList<File> fileArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView=(RecyclerView)findViewById(R.id.listVideoRecycler);
        //phone memory and SD card
        directory=new File("/mnt/");

        //phone memory and SD card
        //directory=new File("/storage/");

        GridLayoutManager manager= new GridLayoutManager(MainActivity.this,2);
        myRecyclerView.setLayoutManager(manager);

        permissionForVideo();
    }

    private void permissionForVideo() {
        if((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)){
            if((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))){

            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        }
        else{
            bool_permission=true;
            getFile(directory);
            obj_adapter=new MyAdapter(getApplicationContext(),fileArrayList);
            myRecyclerView.setAdapter(obj_adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                bool_permission=true;
                getFile(directory);
                obj_adapter=new MyAdapter(getApplicationContext(),fileArrayList);
                myRecyclerView.setAdapter(obj_adapter);
            }
            else{
                Toast.makeText(this,"Please allow the permission",Toast.LENGTH_LONG).show();
            }
        }

    }

    private ArrayList<File> getFile(File directory) {
        File listFile[]=directory.listFiles();
        if(listFile!=null && listFile.length>0){
            for(int i=0;i<listFile.length;i++){
                if(listFile[i].isDirectory()){
                    getFile(listFile[i]);
                }
                else{
                    bool_permission=false;
                    if(listFile[i].getName().endsWith(".mp4")){
                        for(int j=0;j<fileArrayList.size();j++){
                            if(fileArrayList.get(j).getName().equals(listFile[i].getName())){
                                bool_permission=true;
                            }
                            else{

                            }
                        }
                        if(bool_permission){
                            bool_permission=false;
                        }
                        else{
                            fileArrayList.add(listFile[i]);
                        }
                    }
                }
            }
        }
        return fileArrayList;
    }
}
