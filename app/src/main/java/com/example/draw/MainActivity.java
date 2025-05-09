package com.example.draw;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
import com.example.draw.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

    private Bitmap loadBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setNavigationBarColor(Color.BLACK);

        cLayout = binding.aCanvasView;

        cView = new CanvasView(this,binding.buttonUndo,binding.buttonRedo);

        cLayout.addView(cView);

        colorRot = new ColorRotation(this);



//                setSupportActionBar(binding.toolbar);

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



        binding.buttonSaveLoad.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        binding.buttonUndo.setOnClickListener(v -> cView.undoDrawings());

        binding.buttonRedo.setOnClickListener(v -> cView.redoStDrawings());

        binding.buttonClear.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Clear Canvas?")
                        .setMessage("This will delete anything currently on the canvas.")
                        .setPositiveButton("Confirm",(dialog,which) -> cView.clearDrawings())
                        .setNegativeButton("Cancel",(dialog,which) -> {})
                        .show());

        binding.switchErase.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        cView.setEraseModeValue(isChecked));

        binding.buttonColor.setOnClickListener(new MultipleClickListener() {
            @Override
            public void onSingleClick()
            {
                ColorRotation.CRColor newColor = colorRot.incrementColor();
                cView.setPaintColor(newColor.getColorNum());
                binding.buttonColor.setBackgroundTintList(ColorStateList.valueOf(newColor.getColorNum()));

//                Toast.makeText(MainActivity.this,newColor.getName(),Toast.LENGTH_SHORT).show();
                Log.d("TEST_COLOR_CHANGE","Single Click -> "+"Name: "+newColor.getName()+" | "+"Int: "+newColor.getColorNum());
            }

            @Override
            public void onDoubleClick()
            {
                colorRot.reverseOrder();

                Toast.makeText(MainActivity.this,"Color Order Reversed",Toast.LENGTH_SHORT).show();
                Log.d("TEST_COLOR_CHANGE","Double Click -> "+"Order reversed");
            }

            @Override
            public void onTripleClick()
            {
                ColorRotation.CRColor newColor = colorRot.resetColor();
                cView.setPaintColor(newColor.getColorNum());
                binding.buttonColor.setBackgroundTintList(ColorStateList.valueOf(newColor.getColorNum()));

//                Toast.makeText(MainActivity.this,newColor.getName(),Toast.LENGTH_SHORT).show();
                Log.d("TEST_COLOR_CHANGE","Triple Click -> "+"Name: "+newColor.getName()+" | "+"Int: "+newColor.getColorNum());
            }
        });

        binding.buttonColor.setOnLongClickListener(v ->
        {
            Toast.makeText(MainActivity.this,colorRot.getAllColors(),Toast.LENGTH_LONG).show();
            return true;
        });

        binding.buttonSaveLoad.setOnClickListener(new MultipleClickListener() {
            @Override
            public void onSingleClick()
            {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Save Drawing?")
                        .setMessage("...")
                        .setPositiveButton("Confirm",(dialog,which) ->
                        {
                            if (isWritePermissionGranted()) saveDrawing();
                            else requestWritePermission();
                        })
                        .setNegativeButton("Cancel",(dialog,which) -> {})
                        .show();
            }

            @Override
            public void onDoubleClick()
            {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Load Drawing?")
                        .setMessage("This will delete anything currently on the canvas.")
                        .setPositiveButton("Confirm",(dialog,which) ->
                        {
                            if (isReadPermissionGranted())
                            {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityIfNeeded(intent,2297);
                                }
                                catch (Exception e)
                                {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_PICK);
                                    startActivityIfNeeded(intent,2297);
                                }


//                                if (loadBitmap != null) cView.loadDrawing(loadBitmap);
//                                else Toast.makeText(MainActivity.this,"error selecting image",Toast.LENGTH_SHORT).show();
                            }
                            else requestReadPermission();
                        })
                        .setNegativeButton("Cancel",(dialog,which) -> {})
                        .show();
            }

            @Override
            public void onTripleClick() {}
        });



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

    private boolean isReadPermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return Environment.isExternalStorageManager();
        else
            return ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestReadWritePermissionR()
    {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
            startActivityIfNeeded(intent,2296);
        }
        catch (Exception e)
        {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            startActivityIfNeeded(intent,2296);
        }
    }

    private void requestWritePermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            requestReadWritePermissionR();
        else
            requestWritePermissionLauncher.launch(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE });
    }

    private void requestReadPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            requestReadWritePermissionR();
        else
            requestReadPermissionLauncher.launch(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // read/write permission - version R or above
        if (requestCode == 2296)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                if (Environment.isExternalStorageManager()) saveDrawing();
                else Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();

        // get image
        if (requestCode == 2297)
        {
            try
            {
                if (data == null)
                {
                    Toast.makeText(MainActivity.this,"Error Selecting Drawing",Toast.LENGTH_SHORT).show();
                    return;
                }

                Uri imageUri = data.getData();
                assert imageUri != null;
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                loadBitmap = BitmapFactory.decodeStream(imageStream);

                loadDrawing();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    private final ActivityResultLauncher<String[]> requestWritePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),permissions ->
            {
                boolean writePermissionGranted = Boolean.TRUE.equals(permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false));
                if (writePermissionGranted) saveDrawing();
                else Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<String[]> requestReadPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),permissions ->
            {
                boolean readPermissionGranted = Boolean.TRUE.equals(permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false));
                if (readPermissionGranted) saveDrawing();
                else Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
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

            Toast.makeText(MainActivity.this,"Drawing Saved",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadDrawing()
    {
        if (loadBitmap == null) return;

        cView.loadDrawing(loadBitmap);
        Toast.makeText(MainActivity.this,"Drawing Loaded",Toast.LENGTH_SHORT).show();
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
