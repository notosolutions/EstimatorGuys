package com.estimatorguys.app.android.Activities;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import com.estimatorguys.app.android.Database.Session_management;
import com.estimatorguys.app.android.R;
import com.estimatorguys.app.android.fragments.Home_fragment;
import com.estimatorguys.app.android.fragments.fragmentprofile;
import com.estimatorguys.app.android.fragments.fragmentsignin;
import com.estimatorguys.app.android.fragments.fragmentsignup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    @BindView(R.id.picture)
    ImageView im_picture;
    @BindView(R.id.cross)
    ImageView im_cross;

    FragmentTransaction transaction;
    private static final int PICK_IMAGE_ID = 6;
    ArrayList<Uri> imagesUriList;
    ArrayList<String> encodedImageList = new ArrayList<String>();
    String imageURI;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap camerabitmap =null;
    private Session_management session_management;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_aboutus, R.id.nav_support,
                R.id.nav_termandcondition, R.id.nav_privacy, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();*/
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

        /*if (savedInstanceState == null) {*/
            /*Fragment fm = new Home_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();*/

            transaction = getSupportFragmentManager().beginTransaction();
            Fragment fm = new Home_fragment();
            transaction.replace(R.id.fragment_containers,fm).commit();
       /* }*/
        session_management = new Session_management(MainActivity.this);
    }

    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fmprofile=null;
        int id = item.getItemId();
        /*android.app.Fragment fm = null;
        Bundle args = new Bundle();*/
        if (id == R.id.nav_profile) {
            fmprofile = new fragmentprofile();
        } else if (id == R.id.nav_aboutus) {
            Uri uri = Uri.parse("http://notosolutions.net/estimator/About-us"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.nav_support) {
            Uri uri = Uri.parse("http://notosolutions.net/estimator/support"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.nav_termandcondition) {
            Uri uri = Uri.parse("http://notosolutions.net/estimator/Terms-Conditions"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.nav_privacy) {
            Uri uri = Uri.parse("http://notosolutions.net/estimator/Privacy"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            session_management.logoutSession();
            finish();

        }
        if (fmprofile != null) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container,fmprofile,"fragmentprofile").addToBackStack(null).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ButterKnife.bind(this);
        switch(requestCode) {
            case PICK_IMAGE_ID:
                try {
                    // When an Image is picked
                    if (requestCode == PICK_IMAGE_ID && resultCode == RESULT_OK
                            && null != data) {
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        imagesUriList = new ArrayList<Uri>();
                        encodedImageList.clear();
                        // Get the Image from data
                        if(data.getData()!=null){

                            Uri mImageUri = data.getData();

                            // Get the cursor
                            Cursor cursor = getContentResolver().query(mImageUri,
                                    filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageURI  = cursor.getString(columnIndex);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                            im_picture.setImageBitmap(bitmap);
                            im_cross.setVisibility(View.VISIBLE);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                            encodedImageList.add(encodedImage);
                            Log.e("first item",""+encodedImageList.get(0));
                            cursor.close();

                        }else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {

                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    mArrayUri.add(uri);
                                    // Get the cursor
                                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                    // Move to first row
                                    cursor.moveToFirst();

                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    imageURI  = cursor.getString(columnIndex);
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                    im_picture.setImageBitmap(bitmap);
                                    im_cross.setVisibility(View.VISIBLE);
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                                    encodedImageList.add(encodedImage);

                                    Log.e("next item",""+encodedImageList.get(i));
                                    cursor.close();

                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "no invoice images selected",
                                Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

                break;

            case CAMERA_REQUEST:
                if (camerabitmap==null){
                    encodedImageList.clear();
                }
                camerabitmap = (Bitmap) data.getExtras().get("data");
                im_picture.setImageBitmap(camerabitmap);
                im_cross.setVisibility(View.VISIBLE);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                camerabitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                encodedImageList.add(encodedImage);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}
