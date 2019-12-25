package com.estimatorguys.app.android.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.estimatorguys.app.android.Activities.MainActivity;
import com.estimatorguys.app.android.Activities.ThankyouActivity;
import com.estimatorguys.app.android.Database.Session_management;
import com.estimatorguys.app.android.R;
import com.estimatorguys.app.android.utils.AppController;
import com.estimatorguys.app.android.utils.BaseURL;
import com.estimatorguys.app.android.utils.VolleyMultipartRequest;
import com.estimatorguys.app.android.utils.progressdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.estimatorguys.app.android.utils.Config.KEY_ID;

public class Home_fragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.year)
    TextView et_year;
    @BindView(R.id.make)
    EditText et_make;
    @BindView(R.id.model)
    EditText et_model;
    @BindView(R.id.vin)
    EditText et_vin;
    @BindView(R.id.note)
    EditText et_note;
    @BindView(R.id.picture)
    ImageView im_picture;
    @BindView(R.id.cross)
    ImageView im_cross;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.tv_scan)
    TextView tv_scan;
    @BindView(R.id.homeheading)
    TextView tv_homeheading;
    @BindView(R.id.imagecount)
    TextView tv_imageCount;

    ArrayList<String> encodedImageList = new ArrayList<String>();


    ArrayList<Uri> imagesUriList;
    String imageURI;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap camerabitmap =null;


    boolean check=false;
    private static final int PICK_IMAGE_ID = 6;
    int year = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        tv_imageCount.setVisibility(View.GONE);

        /*view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    //((MainActivity) getActivity()).finish();
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                    } else {
                        ((MainActivity) getActivity()).finish();
                    }
                    return true;
                }
                return false;
            }
        });*/

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Helvetica-Neue-Bold.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "AvenirNext-DemiBold.ttf");
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");

        tv_submit.setTypeface(font1);
        tv_scan.setTypeface(font);
        tv_imageCount.setTypeface(font);
        tv_homeheading.setTypeface(font2);
        et_year.setTypeface(font2);
        et_make.setTypeface(font2);
        et_model.setTypeface(font2);
        et_vin.setTypeface(font2);
        et_note.setTypeface(font2);

        tv_submit.setOnClickListener(this);
        im_picture.setOnClickListener(this);
        im_cross.setOnClickListener(this);
        et_year.setOnClickListener(this);

        View view1 = getActivity().getCurrentFocus();
        if (view1!=null){
            InputMethodManager im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view1.getWindowToken(),0);
        }
        return view;
    }



    private void makeAddInvoiceRequest() {

        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

        Session_management sessionManagement = new Session_management(getActivity());
        String user_id = sessionManagement.getUserDetails().get(KEY_ID);

        progressdialog.showwaitingdialog(getActivity());
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                BaseURL.ADD_INVOICE, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                progressdialog.dismisswaitdialog();
                try {
                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    if (response.getString("message").equals("Invoice added successfully!!")){
                        check=true;
                        Intent intent = new Intent(getActivity(), ThankyouActivity.class);
                        startActivity(intent);
                        //finish();

                    }else {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressdialog.dismisswaitdialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("year", et_year.getText().toString());
                params.put("make", et_make.getText().toString());
                params.put("model", et_model.getText().toString());
                params.put("vin", et_vin.getText().toString());
                params.put("status", "1");
                params.put("notes", et_note.getText().toString());
                return params;
            }

            @Override
            protected Map<String, ArrayList<DataPart>> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                Map<String, ArrayList<DataPart>> imageList = new HashMap<>();
                ArrayList<DataPart> dataPart = new ArrayList<>();
                String imagename = "image[]";
                for (int i=0; i<encodedImageList.size(); i++){
                    VolleyMultipartRequest.DataPart dp = new VolleyMultipartRequest.DataPart(imagename+i, Base64.decode(encodedImageList.get(i), Base64.DEFAULT), "image/jpeg");
                    dataPart.add(dp);
                    //params.put(imagename, new DataPart(imagename+i, Base64.decode(encodedImageList.get(i), Base64.DEFAULT), "image/jpeg"));
                }
                imageList.put("image[]", dataPart);

                return imageList;
            }
        };
        int socketTimeout = 500000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(multipartRequest);
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from storage","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PICK_IMAGE_ID);
                        return;
                    }
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                else if (options[item].equals("Choose from storage")){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_ID);                }
                else if (options[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                            Cursor cursor = getActivity().getContentResolver().query(mImageUri,
                                    filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageURI  = cursor.getString(columnIndex);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                            im_picture.setImageBitmap(bitmap);
                            im_cross.setVisibility(View.VISIBLE);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                            encodedImageList.add(encodedImage);
                            tv_imageCount.setVisibility(View.VISIBLE);
                            tv_imageCount.setText(encodedImageList.size()+" "+getResources().getString(R.string.count));
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
                                    Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                                    // Move to first row
                                    cursor.moveToFirst();

                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    imageURI  = cursor.getString(columnIndex);
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                                    im_picture.setImageBitmap(bitmap);
                                    im_cross.setVisibility(View.VISIBLE);
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                                    encodedImageList.add(encodedImage);
                                    tv_imageCount.setVisibility(View.VISIBLE);
                                    tv_imageCount.setText(encodedImageList.size()+" "+getResources().getString(R.string.count));

                                    Log.e("next item",""+encodedImageList.get(i));
                                    cursor.close();

                                }
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "no invoice images selected",
                                Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
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
                tv_imageCount.setVisibility(View.VISIBLE);
                tv_imageCount.setText(encodedImageList.size()+" "+getResources().getString(R.string.count));
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (check) {
            updateUI();
            //this.recreate();
        }

    }

    private void updateUI(){
        et_model.setText("");
        et_vin.setText("");
        et_note.setText("");
        et_year.setText("");
        et_make.setText("");
        et_make.requestFocus();
        im_picture.setImageResource(0);
        im_cross.setVisibility(View.GONE);
    }

    public void showYearDialog(TextView tv_year) {

        final Dialog d = new Dialog(getActivity());
        //d.setTitle(R.string.yeartitle);
        d.setContentView(R.layout.yeardialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+50);
        nopicker.setMinValue(year-50);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_year.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int dialogWindowWidth = (int) (displayWidth * 0.8f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if (encodedImageList.size()!=0) {
                    makeAddInvoiceRequest();
                }else {
                    Toast.makeText(getActivity(), "no invoice images selected",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.picture:
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_ID);*/
                selectImage();
                //startActivityForResult(getPickImageChooserIntent(), 200);

                break;
            case R.id.cross:
                im_picture.setImageResource(0);
                im_cross.setVisibility(View.GONE);
                tv_imageCount.setVisibility(View.GONE);
                break;
            case R.id.year:
                showYearDialog(et_year);
                Log.e("cheker", "fuhifjsdf");
                break;
        }

    }
}
