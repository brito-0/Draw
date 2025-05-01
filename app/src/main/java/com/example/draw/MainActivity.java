package com.example.draw;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
import com.example.draw.databinding.ActivityMainBinding;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
//import android.view.Menu;
//import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private CoordinatorLayout cLayout;

    private ColorRotation colorRot;

    private CanvasView cView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        cLayout = binding.aCanvasView;

//        CanvasView cView = new CanvasView(this);
//        CanvasView cView = new CanvasView(this,binding.buttonUndo,binding.buttonRedo);
        cView = new CanvasView(this,binding.buttonUndo,binding.buttonRedo);

        cLayout.addView(cView);

        colorRot = new ColorRotation(this);



//        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });



        binding.buttonUndo.setOnClickListener(v -> cView.undoDrawings());

        binding.buttonRedo.setOnClickListener(v -> cView.redoStDrawings());

        binding.buttonClear.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("clear canvas?")
                        .setMessage("this will clear everything on the canvas.\ryou also wont be able to use undo or redo afterwards.")
                        .setPositiveButton("Confirm",(dialog,which) -> cView.clearDrawings())
                        .setNegativeButton("cancel",(dialog,which) -> {})
                        .show());

        binding.switchErase.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        cView.setEraseModeValue(isChecked));

        binding.buttonColor.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick()
            {
                ColorRotation.CRColor newColor = colorRot.incrementColor();
                cView.setPaintColor(newColor.getColorNum());
                binding.buttonColor.setBackgroundTintList(ColorStateList.valueOf(newColor.getColorNum()));

                Toast.makeText(getApplicationContext(),newColor.getName(),Toast.LENGTH_SHORT).show();
                Log.d("TEST_COLOR_CHANGE","Single Click -> "+"Name: "+newColor.getName()+" | "+"Int: "+newColor.getColorNum());
            }

            @Override
            public void onDoubleClick()
            {
                ColorRotation.CRColor newColor = colorRot.resetColor();
                cView.setPaintColor(newColor.getColorNum());
                binding.buttonColor.setBackgroundTintList(ColorStateList.valueOf(newColor.getColorNum()));

                Toast.makeText(getApplicationContext(),newColor.getName(),Toast.LENGTH_SHORT).show();
                Log.d("TEST_COLOR_CHANGE","Double Click -> "+"Name: "+newColor.getName()+" | "+"Int: "+newColor.getColorNum());
            }
        });

        binding.buttonSave.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("save drawing?")
                        .setMessage("...")
                        .setPositiveButton("Confirm",(dialog,which) ->
                        {
                            if (isWritePermissionGranted()) saveDrawing();
                            else requestWritePermission();
                        })
                        .setNegativeButton("cancel",(dialog,which) -> {})
                        .show());



//      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        Display display = getWindowManager().getDefaultDisplay();
//        Display.Mode[] supported = display.getSupportedModes();
//        for (final Display.Mode m : supported)
//            Log.d("DisplayInfo","supported mode: "+m.getPhysicalWidth()+"x"+m.getPhysicalHeight()+", "+m.getRefreshRate()+"Hz");
//      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private boolean isWritePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return Environment.isExternalStorageManager();
        else
            return ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestWritePermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
//                startActivityForResult(intent,2296);
                startActivityIfNeeded(intent,2296);
            }
            catch (Exception e)
            {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                startActivityForResult(intent,2296);
                startActivityIfNeeded(intent,2296);
            }
        }
        else
            requestPermissionLauncher.launch(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2296)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                if (Environment.isExternalStorageManager()) saveDrawing();
                else Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_SHORT).show();
    }

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),permissions ->
            {
                boolean writePermissionGranted = Boolean.TRUE.equals(permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false));
                if (writePermissionGranted) saveDrawing();
                else Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_SHORT).show();
            });

    private void saveDrawing()
    {
        Bitmap saveBitmap = cView.getBitmap();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
        String fileName = "Drawing-"+sdf.format(new Date())+".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpg");

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File saveFile = new File(dir,fileName);
        values.put(MediaStore.MediaColumns.DATA,saveFile.getAbsolutePath());

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        assert uri != null;

        try (OutputStream out = getContentResolver().openOutputStream(uri))
        {
            assert out != null;
            saveBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

            saveBitmap.recycle();
            out.flush();
            out.close();

            Toast.makeText(getApplicationContext(),"drawing saved",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}
