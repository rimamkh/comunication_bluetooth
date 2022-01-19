package com.example.bl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ///static to enable bl
    private static final int REQUEST_ENABLE_BT=0;
    ///static to enable discover bl
    private static final int REQUEST_DISCOVER_BT=1;
    TextView mStatues,mPairedtv,devicename;
    ImageView mblueiv;
    Button mOnBtn,mOffBtn,mDiscoverBtn,mPairedBtn;
    BluetoothAdapter mbluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declare
        mStatues=findViewById(R.id.status);
        mPairedtv=findViewById(R.id.pairTv);
        mblueiv=findViewById(R.id.image);
        mOnBtn=findViewById(R.id.onBtn);
        mOffBtn=findViewById(R.id.offBtn);
        mDiscoverBtn=findViewById(R.id.discoverableBtn);
        mPairedBtn=findViewById(R.id.pairBtn);
        devicename=findViewById(R.id.textView2);

        //initialize the bluttoth adapter
        mbluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        //check if bl is availabe or not
         if(mbluetoothAdapter == null){
             mblueiv.setBackgroundResource(R.drawable.ic_action_off);
             mStatues.setText("Bluetooth is not available");
         }else
         {
             mblueiv.setBackgroundResource(R.drawable.ic_action_on);
             mStatues.setText("Bluetooth is  available");
         }
         devicename.setText(getlocalBluttothname());
         //set image according to the bl statues
         ///we can check if bl is on of off by using isEnable
          /*if(mbluetoothAdapter.isEnabled()){
              mblueiv.setBackgroundResource(R.drawable.ic_action_on);
          }else
          {
              mblueiv.setBackgroundResource(R.drawable.ic_action_off);
          }*/
        ///click to turn on
        mOnBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ///first we check if its on or no
                if(!mbluetoothAdapter.isEnabled()){
                    showToast("Turnning on ...");
                    ///intent to on blutooth
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }else {
                    showToast("Bluttoth is already on");
                }

            }
        });
          //discover button action
          mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
              @SuppressLint("MissingPermission")
              @Override
              public void onClick(View view) {
                if(!mbluetoothAdapter.isDiscovering()){
                    showToast("make it discover the devices");
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent,REQUEST_DISCOVER_BT);
                }
              }
          });
        ////turn off button
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
          if(mbluetoothAdapter.isEnabled()){
              mbluetoothAdapter.disable();
              showToast("Turning off");
              mblueiv.setImageResource(R.drawable.ic_action_off);
          }else
          {
              showToast("already off");
          }
            }
        });
        ///paried button
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
              if(mbluetoothAdapter.isEnabled()){
                  mPairedtv.setText("paired devices");
                  Set<BluetoothDevice> devices=mbluetoothAdapter.getBondedDevices();
                  for(BluetoothDevice  device:devices){
                      mPairedtv.append("\nDevice"+ device.getName()+ ","+device);
                  }

              }else
                  showToast("bl is off so you cant ");
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       switch (requestCode){
           case REQUEST_ENABLE_BT:
               if(resultCode ==RESULT_OK){
                   mblueiv.setBackgroundResource(R.drawable.ic_action_on);
                   showToast("bl is on ");
               }else
               {
                   showToast("could not open it ");
               }
               break;
       }


        super.onActivityResult(requestCode, resultCode, data);
    }

    ///toast message show statues
    private  void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT ).show();
    }

    //get device name
    public String getlocalBluttothname(){
        if(mbluetoothAdapter ==null){
            mbluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        }
        String name=mbluetoothAdapter.getName();
        if(name == null){
            name =mbluetoothAdapter.getAddress();
        }
        return name;

    }

}