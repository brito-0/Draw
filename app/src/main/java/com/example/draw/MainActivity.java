package com.example.draw;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.draw.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private CoordinatorLayout cLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        cLayout = binding.aCanvasView;

//        CanvasView cView = new CanvasView(this);
        CanvasView cView = new CanvasView(this,binding.buttonUndo,binding.buttonRedo);

        cLayout.addView(cView);


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

//        binding.buttonClear.setOnClickListener(v -> cView.clearDrawings());
        binding.buttonUndo.setOnClickListener(v -> cView.undoDrawings());
//        binding.buttonRedo.setOnClickListener(v -> cView.redoDrawings());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}