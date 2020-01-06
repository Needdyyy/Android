package com.needyyy.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.StrictMode;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;


import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.needyyy.AppController;
import com.needyyy.app.Modules.Home.Activities.WebViewActivity;
import com.needyyy.app.Modules.Login.Activities.LoginActivity;
import com.needyyy.app.R;
import com.needyyy.app.constants.Constants;
import com.needyyy.app.manager.BaseManager.BaseManager;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import jp.wasabeef.blurry.Blurry;

import static com.needyyy.app.ImageClasses.CropImage.calculateInSampleSize;


/**
 * Created by kindlebit on 9/12/2016.
 */
public class CommonUtil {


    private static AlertDialog alertDialog;
    //    private static AlertDialog progressDialog;
    private static int hour, minutes;
    final String time = "";
    private static Dialog dialog;
    private static Progress mprogress;


    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    /**
     * Shows an alert dialog with the OK button. When the user presses OK button, the dialog
     * dismisses.
     **/
    public static void showAlertDialog(Context context, String title, String body) {
        showAlertDialog(context, title, body, null);
    }


    /**
     * Creates a confirmation dialog that show a pop-up with button labeled as parameters labels.
     *
     * @param ctx                 {@link Activity} {@link Context}
     * @param message             Message to be shown in the dialog.
     * @param dialogClickListener
     * @param positiveBtnLabel    For e.g. "Yes"
     * @param negativeBtnLabel    For e.g. "No"
     **/
    public static void showDialog(Context ctx, String message, String positiveBtnLabel, String negativeBtnLabel, DialogInterface.OnClickListener dialogClickListener) {

        if (dialogClickListener == null) {
            throw new NullPointerException("Action listener cannot be null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(message).setPositiveButton(positiveBtnLabel, dialogClickListener).setNegativeButton(negativeBtnLabel, dialogClickListener).show();
    }

    /**
     * Shows an alert dialog with OK button
     **/
    public static void showAlertDialog(Context context, String title, String body, DialogInterface.OnClickListener okListener) {

        if (okListener == null) {
            okListener = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(body).setPositiveButton("OK", okListener);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.show();
    }

    public static void showLongToast(Context context, String message) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 60000);
    }

    public static void showShortToast(Context context, String message) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

    public static void showAlert(Context context, String message, String tittle) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(tittle);

        alertDialogBuilder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });


        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void closeKeyboard(Activity cnx) {

        InputMethodManager imm = (InputMethodManager) cnx.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            if (imm.isAcceptingText() || imm.isActive())
                imm.hideSoftInputFromWindow(cnx.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void imageDialog(String image, Context context){
//
//        Dialog imageDialog = new Dialog(context);
//        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//
//        imageDialog.setCancelable(true);
//        imageDialog.setCanceledOnTouchOutside(true);
//        imageDialog.setContentView(R.layout.image_dialogue_layout);
//
//        final ImageView ivImage= imageDialog.findViewById(R.id.iv_image);
//
//        if(!image.equals("")) {
//            Ion.with(context)
//                    .load(image)
//                    .asBitmap().setCallback(new FutureCallback<Bitmap>() {
//                @Override
//                public void onCompleted(Exception e, Bitmap result) {
//                    if(e==null)
//                       ivImage.setImageBitmap(result);
//                    else
//                        ivImage.setImageResource(R.drawable.profile_default);
//                }
//            });
//        }else{
//            ivImage.setImageResource(R.drawable.profile_default);
//        }
//
//        imageDialog.show();
//    }


    public static void sendLink(Activity activity, String subject, String msg) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
        if (sharingIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(sharingIntent);
        }
    }

    public static boolean checkEmail(String email) {
        return Constant.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public static void snackBar(String message, Activity context) {
        Snackbar.make(context.findViewById(R.id.frame_main), message, Snackbar.LENGTH_SHORT).show();
    }

//    public static SpotsDialog showProgressDialog(Activity context) {
//        try {
//
//            progressDialog = new SpotsDialog(context, R.style.CustomProgress);
//            progressDialog.setCancelable(false);
//
//
//            progressDialog.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return progressDialog;
//    }
//
//    public static void cancelProgressDialog() {
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }


    public static void cancelProgressDialog() {
        if (dialog.isShowing()) {
            dialog.cancel();
            dialog = null;
        }
    }

    public static void showProgress(Activity activity) {
        mprogress = new Progress(activity);
        mprogress.setCancelable(false);
        mprogress.show();
    }

    public static void cancelProgress() {
        if (mprogress != null)
            if (mprogress.isShowing()) {
                mprogress.hide();
                mprogress = null;
            }
    }

    public static void DatePicker(Context context, final TextInputEditText tv, final Fragment fragment) {
        int mYear, mMonth, mDay, mHour, mMinute;

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = null;
        int style;
        style = R.style.datepickerlolipop;

        datePickerDialog = new DatePickerDialog(context, style,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        SimpleDateFormat sdfSource = new SimpleDateFormat("dd mm yyyy");
//                        //parse the string into Date object
//                        Date date = null;
//                        try {
//                            date = sdfSource.parse(dayOfMonth + " " + (monthOfYear + 1) + " " + year);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        //create SimpleDateFormat object with desired date format
//                        SimpleDateFormat sdfDestination = new SimpleDateFormat("dd MMM yyyy");

                        //parse the date into another format
                        tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        tv.setText(year+ "-"+ (monthOfYear + 1)+ "-"+dayOfMonth  );
                        //       ((CheckOutFragment)fragment).getTimeSlots();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    public static void DatePickerWithouValidation(Context context, final TextInputEditText tv, final Fragment fragment) {
        int mYear, mMonth, mDay, mHour, mMinute;

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = null;
        int style;
        style = R.style.datepickerlolipop;

        datePickerDialog = new DatePickerDialog(context, style,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        SimpleDateFormat sdfSource = new SimpleDateFormat("dd mm yyyy");
//                        //parse the string into Date object
//                        Date date = null;
//                        try {
//                            date = sdfSource.parse(dayOfMonth + " " + (monthOfYear + 1) + " " + year);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        //create SimpleDateFormat object with desired date format
//                        SimpleDateFormat sdfDestination = new SimpleDateFormat("dd MMM yyyy");

                        //parse the date into another format
                        tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        tv.setText(year+ "-"+ (monthOfYear + 1)+ "-"+dayOfMonth  );
                        //       ((CheckOutFragment)fragment).getTimeSlots();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    public static void getCompleteAddress(Context context, double LATITUDE, double LONGITUDE, AutoCompleteTextView ac) {

        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> address = geocoder.getFromLocation(LATITUDE, LONGITUDE, 2);

            String addressline = address.get(0).getAddressLine(0);
            ac.setText(addressline);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String MD5_Hash(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeBase64(String coded) {
        String converted;
        converted = Base64.encodeToString(coded.getBytes(), Base64.NO_WRAP);
        return converted;
    }

    public static double getRandomNumber() {
        double x = Math.random();
        return x;
    }

    public static String getPaymrntMode(String mode) {
        String paymentMode = "";
        switch (mode) {
            case "0":
                return "other";
            case "1":
                return "Cash";
            case "2":
                return "Online";
            case "3":
                return "Wallet";
        }
        return "other";
    }

    //subtract 378691200 from the coming timstamp for getting dob.

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static void DobDatePicker(Context context, final TextInputEditText tv, final Fragment fragment) {
        int mYear, mMonth, mDay, mHour, mMinute;

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -12);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = null;
        int style;
        style = R.style.datepickerlolipop;

        datePickerDialog = new DatePickerDialog(context, style,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        SimpleDateFormat sdfSource = new SimpleDateFormat("dd mm yyyy");
//                        //parse the string into Date object
//                        Date date = null;
//                        try {
//                            date = sdfSource.parse(dayOfMonth + " " + (monthOfYear + 1) + " " + year);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        //create SimpleDateFormat object with desired date format
//                        SimpleDateFormat sdfDestination = new SimpleDateFormat("dd MMM yyyy");

                        //parse the date into another format
                        tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        tv.setText(year+ "-"+ (monthOfYear + 1)+ "-"+dayOfMonth  );
                        //       ((CheckOutFragment)fragment).getTimeSlots();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return strAdd;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static String CapitalizeText(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.contains(" ")) {
                String[] strarr = text.split(" +");
                String fname = null;
                for (String name : strarr) {
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1); // d
                    fname = (fname == null ? name : fname + " " + name);
                }
                return fname;
            } else return Character.toUpperCase(text.charAt(0)) + text.substring(1);
        }
        return text;
    }

    public static TextDrawable GetDrawable(String text, Context context, String userid) {
        if (!TextUtils.isEmpty(text)) {
            String firstLetter = text.substring(0, 1);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, Constant.color[DbAdapter.getInstance(context).getColor(userid)]);
            return drawable;
        } else
            return null;
    }

    //methods to compress image starts//
    public static Bitmap decodeSampledBitmap(String url, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(url, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(url, options);
    }

    //methods to compress image starts//
    public static byte[] FileToByteArray(String file) {
        File fil = new File(file);

        byte[] b = new byte[(int) fil.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(fil);
            fileInputStream.read(b);
            for (int i = 0; i < b.length; i++) {
                System.out.print((char) b[i]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return b;
    }
    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    public static String convertLongDateToAgoString (Long createdDate, Long timeNow){
        Long timeElapsed = timeNow - createdDate;

        // For logging in Android for testing purposes
        /*
        Date dateCreatedFriendly = new Date(createdDate);
        Log.d("MicroR", "dateCreatedFriendly: " + dateCreatedFriendly.toString());
        Log.d("MicroR", "timeNow: " + timeNow.toString());
        Log.d("MicroR", "timeElapsed: " + timeElapsed.toString());*/

        // Lengths of respective time durations in Long format.
        Long oneMin = 60000L;
        Long oneHour = 3600000L;
        Long oneDay = 86400000L;
        Long oneWeek = 604800000L;

        String finalString = "0sec";
        String unit;

        if (timeElapsed < oneMin){
            // Convert milliseconds to seconds.
            double seconds = (double) ((timeElapsed / 1000));
            // Round up
            seconds = Math.round(seconds);
            // Generate the friendly unit of the ago time
            if (seconds == 1) {
                unit = "sec";
            } else {
                unit = "secs";
            }
            finalString = String.format("%.0f", seconds) + unit;
        } else if (timeElapsed < oneHour) {
            double minutes = (double) ((timeElapsed / 1000) / 60);
            minutes = Math.round(minutes);
            if (minutes == 1) {
                unit = "min";
            } else {
                unit = "mins";
            }
            finalString = String.format("%.0f", minutes) + unit;
        } else if (timeElapsed < oneDay) {
            double hours   = (double) ((timeElapsed / 1000) / 60 / 60);
            hours = Math.round(hours);
            if (hours == 1) {
                unit = "hr";
            } else {
                unit = "hrs";
            }
            finalString = String.format("%.0f", hours) + unit;
        } else if (timeElapsed < oneWeek) {
            double days   = (double) ((timeElapsed / 1000) / 60 / 60 / 24);
            days = Math.round(days);
            if (days == 1) {
                unit = "day";
            } else {
                unit = "days";
            }
            finalString = String.format("%.0f", days) + unit;
        } else if (timeElapsed > oneWeek) {
            double weeks = (double) ((timeElapsed / 1000) / 60 / 60 / 24 / 7);
            weeks = Math.round(weeks);
            if (weeks == 1) {
                unit = "week";
            } else {
                unit = "weeks";
            }
            finalString = String.format("%.0f", weeks) + unit;
        }
        return finalString;
    }
    public static int getNumberOfDays(String inputString1, String inputString2){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String input1= inputString1.replace("-"," ");
        String input2 =inputString2.replace("-"," ");
        int days = 0;
        try {
            Date date1 = myFormat.parse(input1);
            Date date2 = myFormat.parse(input2);
            long diff = date2.getTime() - date1.getTime();

            days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  days ;
    }

    public static int getAge(String ageDob){
        String[] dobDate=ageDob.split("-");
        int year ,month,day ;
        day   = Integer.parseInt(dobDate[0]);
        month  = Integer.parseInt(dobDate[1]);
        year    = Integer.parseInt(dobDate[2]);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);


        return age;
    }
    public static  void ConvertURLToBitmap(String profilePicture, Context context, ImageView civProfile) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(profilePicture);

            Blurry.with(context)
                    .radius(10).
                    from(BitmapFactory.decodeStream((InputStream)url.getContent())).into(civProfile);
//            CommonUtil.setBlur(2,BitmapFactory.decodeStream((InputStream)url.getContent()),context ,civProfile);
//            Blurry.with(context)
//                    .radius(10)
//                    .sampling(8)
//                    .color(Color.argb(66, 255, 255, 0))
//                    .async()
//                    .animate(500)
//                    .onto(civProfile);
        } catch (IOException e) {
            Log.e("error", e.getMessage());
        }
    }

    public static void setBlur(int radius, Bitmap bitmap, Context context, ImageView civProfile) {
        final int MAX_RADIUS = 25;
        final int MIN_RADIUS = 1;
        // max radius = 25
        if (radius > MIN_RADIUS && radius <= MAX_RADIUS) {

            Bitmap blurred = blurRenderScript(bitmap, radius,context);
            civProfile.setImageBitmap(blurred);
        } else
            Log.e("BLUR", "actualRadius invalid: " + radius);
    }

    private static Bitmap blurRenderScript(Bitmap smallBitmap, int radius,Context context) {
        float defaultBitmapScale = 0.1f;
        int width  = Math.round(smallBitmap.getWidth() * defaultBitmapScale);
        int height = Math.round(smallBitmap.getHeight() * defaultBitmapScale);

        Bitmap inputBitmap  = Bitmap.createScaledBitmap(smallBitmap, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        theIntrinsic.setRadius(radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static void GoToWebViewActivity(Activity activity, String url,String title) {
        Intent newintent = new Intent(activity, WebViewActivity.class);
        newintent.putExtra(Constants.URL, url);
        newintent.putExtra(Constant.Title, title);
        activity.startActivity(newintent);
    }
    public static String  getGender(String gender){
        String OTHER  = "others";
        String MALE   = "male";
        String FEMALE = "female";
        if (gender.equals("0"))
            return OTHER;
        else if (gender.equals("1"))
            return MALE;
        else if (gender.equals("2"))
            return FEMALE;
        return "";
    }

    public static void logout(Activity activity) {
        AppController.getManager().clearPreference();
        BaseManager.clearPreference();
        generateFirebaseTocken();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    public static void generateFirebaseTocken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("SplashActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.e("tocken","====="+token);
                        AppController.getManager().setTocken(token);

                    }
                });
    }

}


