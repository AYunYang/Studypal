package com.sp.mad_studypal;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Scanner_Activity extends AppCompatActivity {
    private BottomNavigationView bottom_menu;
    private String current_email;

    private Button btn_scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        bottom_menu = findViewById(R.id.id_bottomNavigation);
        bottom_menu.setOnItemSelectedListener(bottom_menu_select);

        MenuItem item = bottom_menu.getMenu().findItem(R.id.bottom_item_scanner);   //Set the profile tab selected
        item.setChecked(true);

        holder object = new holder(getApplicationContext());
        current_email = object.getKeyEmail();

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> {scanCode();});
    }

    protected void onStart(){
        bottom_menu.setSelectedItemId(R.id.bottom_item_scanner);   //Default input
        super.onStart();
    }



    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setBeepEnabled(true);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(capture_activity.class);
        barLauncher.launch(options);

    }

    //launch the camera scanning
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() !=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(Scanner_Activity.this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    });

    NavigationBarView.OnItemSelectedListener bottom_menu_select =  new NavigationBarView.OnItemSelectedListener() {      //Bottom Menu selected
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.bottom_item_scanner){    //Scanner Tab
                return false;
            }
            else if (id == R.id.bottom_item_search){   //Search Tab
                Intent intent = new Intent(Scanner_Activity.this, Search_Activity.class);
                startActivity(intent);
                return false;
            }
            else if (id == R.id.bottom_item_profile){   //Profile Tab
                Intent intent = new Intent(Scanner_Activity.this, Profile_Activity.class);
                startActivity(intent);
                return false;
            }

            return false;
            //true to display the item as the selected item.
            //false if the item should not be selected.
        }
    };
}