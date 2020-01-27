package com.app.admin.sellah.controller.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.admin.sellah.controller.WebServices.OnlineStatus;
import com.app.admin.sellah.view.activities.MainActivity;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.app.admin.sellah.R;
import com.app.admin.sellah.controller.WebServices.WebService;
import com.app.admin.sellah.model.extra.MySpannable;
import com.app.admin.sellah.view.adapter.ExpandableListAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.fabric.sdk.android.Fabric;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.admin.sellah.controller.utils.Configuration.SERVER_CERT;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.PROFILESTATUS;
import static com.app.admin.sellah.controller.utils.SAConstants.Keys.UID;
import static com.app.admin.sellah.controller.utils.SAConstants.Urls.BASEURL;
import static com.app.admin.sellah.controller.utils.SAConstants.Urls.SOCKETURL;

public class Global extends MultiDexApplication {
    public static String videopath = "no_image" ;
    public static final String KEY_IMAGE_URL = "image.resource";
    public static final String KEY_IMAGE = "image.string";
    private FirebaseAnalytics mFirebaseAnalytics;
    public static String HITCARD = "no_hit_card";
    private static Global mInstance;
    public static boolean from_register = false;
    public static boolean deleted_account = false;

    public static  String DEEP_LINKING_STATUS="disable";
    public static  String DEEP_LINKING_PRODUCT_ID="";
    public static  String DEEP_LINKING_PRODUCT_TYPE="";
    public static  String DEEP_LINKING_PRODUCT_URL="";

  //private RefWatcher refWatcher;


    private Socket mSocket;
    {
        try {

            //-------------ssl security--------------------------------
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

// default settings for all sockets
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);

// set as an option
            IO.Options  opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;

            mSocket = IO.socket(SOCKETURL  , opts);

            } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MultiDex.install(this);
        mInstance = this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        TypefaceUtil.setDefaultFont(getApplicationContext(), "SERIF", "fonts/product_sans_regular.ttf");
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/product_sans_regular.ttf");
//        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/gothambold.ttf");
//        mFirebaseAnalytics.
        //------------leak memory--------------------
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);*/
    }

    public static synchronized Global getInstance() {
        return mInstance;
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(Activity con, String screenName) {
        mFirebaseAnalytics.setCurrentScreen(con, screenName, null);
    }

    public static MaterialDialog getResetPasswordDialog(Context context, MaterialDialog.InputCallback inputCallback) {
        return new MaterialDialog.Builder(context)
                .title(R.string.forgot)
                .autoDismiss(false)
                .content(R.string.dialog_content_forgot)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText("Send")
                .input(R.string.email, 0, inputCallback).build();

    }

    /*  public static MaterialDialog getMakeOfferDialog(Context context, MaterialDialog.InputCallback inputCallback) {
          return new MaterialDialog.Builder(context)
                  .title("Make Offer")
                  .content("Please Enter amount below")
                  .inputType(InputType.TYPE_CLASS_NUMBER)
                  .positiveText("Make Offer")
                  .input(R.string.tags, 0, inputCallback).build();

      }*/
    public static MaterialDialog addTag(Context context, MaterialDialog.InputCallback inputCallback) {
        return new MaterialDialog.Builder(context)
                .title("Add Tags")
                .content("Please Enter your tag below")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("Add")
                .input(R.string.tags, 0, inputCallback).build();

    }

    public static MaterialDialog addComment(Context context, MaterialDialog.InputCallback inputCallback) {
        return new MaterialDialog.Builder(context)
                .title("Report")
                .content("Please Enter your comment")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("Ok")
                .input(0, 0, inputCallback).build();

    }

    public static void getTotalHeightofGridRecyclerView(Context con, RecyclerView recyclerView, int layoutId, int paddingValue) {

        RecyclerView.Adapter mAdapter = recyclerView.getAdapter();

        int totalHeight = 0;
        int paddingDp = paddingValue;
        int remainder = (mAdapter.getItemCount()) % 2;

        int numberOfCells;

        switch (remainder) {
            case 1:
                numberOfCells = (mAdapter.getItemCount() + 1) / 2;
                break;
            default:
                numberOfCells = (mAdapter.getItemCount()) / 2;
                break;
        }
        View v = LayoutInflater.from(con).inflate(layoutId, null);
        for (int i = 0; i < numberOfCells; i++) {
//            View mView = con.getChildViewHolder(v).itemView;
//            View v = recyclerView.findViewHolderForAdapterPosition(i).itemView;

            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += v.getMeasuredHeight() + paddingDp;
        }
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
    }

    public static void getTotalHeightofLinearRecyclerView(Context con, RecyclerView recyclerView, int layout, int paddingValue) {
        try {
            RecyclerView.Adapter mAdapter = recyclerView.getAdapter();

            int totalHeight = 0;
            int paddingDp = paddingValue;

            View v = LayoutInflater.from(con).inflate(layout, null);

            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalHeight += v.getMeasuredHeight() + paddingDp;
            }

//        if (totalHeight > 100) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = totalHeight;
            recyclerView.setLayoutParams(params);
        } catch (Exception e) {
        }
        //        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                /*if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }*/
                int lineEndIndex;
                String text;
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(/*Html.fromHtml(*/tv.getText().toString(), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final String strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(/*View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |*/ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(/*View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |*/ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    public static void makeTransperantStatusBar(Context context, boolean isTransperant) {
        if (isTransperant) {
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(EditText edt, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(edt.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);

    }


    public static void setExpandableListViewHeight(ExpandableListView listView) {
        try {
            ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                View listItem = listAdapter.getGroupView(i, false, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
            if (height < 10) height = 200;
            params.height = height;
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setListViewHeight(ExpandableListView listView,
                                         int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String formateDateTo_HHmm(String dateToChange) {

        String formattedDate = "00:00";
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat writeFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = readFormat.parse(dateToChange);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }
        return formattedDate;
    }

    public static String getTimeDuration(String startTime, String endTime) throws ParseException {

//        Log.e("time", "getTimeDuration: " + startTime + ":" + endTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date1 = simpleDateFormat.parse(startTime);
        Date date2 = simpleDateFormat.parse(endTime);

        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        int sec = (int) ((difference / 1000) % 60);
        hours = (hours < 0 ? -hours : hours);
//        Log.e("Hours", " :: " + hours + ":" + min + ":" + sec);



        String   minVal="";
        try {  minVal = String.format("%02d", min);  }catch (Exception e){ }

        String   secVal="";
        try {  secVal = String.format("%02d", sec);  }catch (Exception e){ }





        return (minVal + ":" + secVal);
    }

    public static class BackstackConstants {
        public static String HOMETAG = "sa.home.frag.tag";
        public static String SETTINGTAG = "sa.setting.frag.tag";
        public static String PROFILETAG = "sa.profile.frag.tag";
        public static String NOTIFICATIONETAG = "sa.noti.frag.tag";
        public static String ADDPRODUCTTAG = "sa.sell.frag.tag";
        public static String LIVETAG = "sa.live.frag.tag";

    }

    public static class WebServiceConstants {
        public static WebService getRetrofitinstance() {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3,TimeUnit.MINUTES)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .client(okHttpClient)
                    .client(getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            return retrofit.create(WebService.class);
        }

        public static Retrofit getRetrofit() {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3,TimeUnit.MINUTES)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            return retrofit;
        }
    }

    public static class getUser {


        public static boolean isLogined(Context context) {
            String user_id = HelperPreferences.get(context).getString(UID);
            if (user_id != null && !user_id.equalsIgnoreCase(""))
                return true;
            else return false;
        }


    }

    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static void requestFocus(Context context, View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public static void clearFocus(Context context, View view) {
        if (view.requestFocus()) {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public static class NetworStatus {
        //to check internet connection
        public static boolean isOnline(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                return false;
            }
        }

        //to check connection with wifi
        public static boolean isInternetAvailable() {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com");
                return !ipAddr.equals("");
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static String getTimeAgo(String actualTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("TimeZone", "getTimeAgo: " + TimeZone.getDefault().getDisplayName());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        sdf.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
        long time = 0;
        try {
            time = sdf.parse(actualTime).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nowTime = System.currentTimeMillis();
        String timeAgo = "";
        try {
            Calendar cal = Calendar.getInstance();
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(actualTime);
            Date now = new Date();
            Log.e("ttgtgtg",now.getTime()+"");
            Log.e("ttgtgtg",past.getTime()+"");

            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());



            if (seconds < 60) {
                if (seconds == 0) {
                    return "a second ago";
                } else {
                    return seconds + " seconds ago";
                }
            } else if (minutes < 60) {
                if (minutes == 0) {
                    return "a minute ago";
                } else {
                    return minutes + " minutes ago";
                }
            } else if (hours < 24) {
                if (hours == 0) {
                    return "a hour ago";
                } else {
                    return hours + " hours ago";
                }
            } else if (days <= dayOfMonth) {
                if (days == 0) {
                    return "a day ago";
                } else {
                    return days + " days ago";
                }
            } else {
//                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(days * (1000 * 60 * 60 * 24));
                int month = cal.get(Calendar.MONTH);
                if (month > 1) {
                    return month + " months ago";
                } else {
                    if (month == 0) {
                        return "a month ago";
                    } else {
                        return month + " month ago";
                    }
                }
            }
        } catch (Exception j) {
            j.printStackTrace();
            return String.valueOf(DateUtils.getRelativeTimeSpanString(time, nowTime, DateUtils.MINUTE_IN_MILLIS));
        }
    }

    public static RequestOptions getGlideOptions() {
        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
        requestOptions.override(100);
        requestOptions.placeholder(R.drawable.default_image);
        requestOptions.error(R.drawable.default_image);
        return requestOptions;
    }

    public static RequestOptions getBannerGlideOptions() {
        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.transform(new CircleCrop());
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();
        requestOptions.format(DecodeFormat.PREFER_ARGB_8888);
        requestOptions.override(Target.SIZE_ORIGINAL);
        requestOptions.placeholder(R.drawable.default_image);
        requestOptions.error(R.drawable.default_image);
        return requestOptions;
    }

    public static void logout(Context context) {
        new OnlineStatus().changeOnlineStatus(HelperPreferences.get(context).getString(UID), "OFF");
        HelperPreferences.get(context).clear();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
        Toast.makeText(context, "Logout from app successfully", Toast.LENGTH_SHORT).show();
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromByte(String byteArray) {
        byte[] b = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            b = byteArray.getBytes(StandardCharsets.UTF_8);
        } else {
            b = byteArray.getBytes(Charset.forName("UTF-8"));
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);

        return Bitmap.createScaledBitmap(bmp, 100,
                100, false);
/*
        String imgString = Base64.encodeToString(getBytesFromBitmap(someImg),
                Base64.NO_WRAP);*/
    }

    public static class ProfileStatusCheck {

        public static void checkProfileStatus(Context context, ProfileStatusCallback callback) {

            if (HelperPreferences.get(context).getString(PROFILESTATUS) != null && HelperPreferences.get(context).getString(PROFILESTATUS).equalsIgnoreCase("Y")) {
                callback.onIfProfileUpdated();
            } else {
                callback.onIfProfileNotUpdated();
            }

        }

        public interface ProfileStatusCallback {
            void onIfProfileUpdated();

            void onIfProfileNotUpdated();
        }
    }

    public static String convertUTCToLocal(String time) {
       /* Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        *//* debug: is it local time? *//*
        Log.e("Time zone: ", tz.getDisplayName());

        *//* date formatter in local timezone *//*
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(tz);

        *//* print your timestamp and double check it's the date you expect *//*
        long timestamp = cursor.getLong(columnIndex);
        String localTime = sdf.format(new Date(timestamp * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
        Log.e("Time: ", localTime);
        */
//        Log.e("Time_utc ", ":" + time);

      /*  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = df.parse(time);
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);
        Log.e("Time: ", formattedDate);
*/
        String dateToReturn = "00:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date gmt = null;

        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdfOutPutToSend.setTimeZone(TimeZone.getDefault());

        try {

            gmt = sdf.parse(time);
            dateToReturn = sdfOutPutToSend.format(gmt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Log.e("Time: ", dateToReturn);
        return dateToReturn;
//        return formattedDate;
    }

    private void timerClock() {
      /*  String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));*/
    }

    public static void playMessageTone(Activity context) {
        try {
            MessageToneManager messageToneManager = new MessageToneManager(context);
            messageToneManager.updatePrefs();
            messageToneManager.playBeepSoundAndVibrate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink,
                    startIndexOfLink + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setHighlightColor(
                Color.TRANSPARENT); // prevent TextView change background when highlight
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    public static void CheckPriceDecimal(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = editText.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 20, 2);

                if (!str2.equals(str)) {
                    editText.setText(str2);
                    int pos = editText.getText().length();
                    editText.setSelection(pos);
                }

            }
        });



    }

    public static String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
    }

    public static String gettotalamount(String amount)
    {
        if (!amount.isEmpty())
        {
            float a = Float.parseFloat(amount);
            double cardFees = 3.4*a/100 + 0.50;
            double adminFee = 1*a/100 + 1;
            double totalDeductionAmount = cardFees + adminFee;
            double finalPrice = a - totalDeductionAmount;
            String formattedStr = String.format("%.2f",finalPrice);

            return String.valueOf(formattedStr);
        }
        return "";

    }


    //-------------------------ssl secutity------------------------
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

  /*  public static RefWatcher getRefWatcher(Context context) {
        Global application = (Global) context.getApplicationContext();
        return application.refWatcher;
    }*/

}
