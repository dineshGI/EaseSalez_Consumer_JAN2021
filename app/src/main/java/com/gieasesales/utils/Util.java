package com.gieasesales.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.gieasesales.BuildConfig;
import com.gieasesales.Http.CallApi;
import com.gieasesales.interfaces.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.MODE_PRIVATE;

public class Util {

    //V1.1.4 - Added intent validation
    //9840287157
    public static final String IMAGE_DIRECTORY_NAME = "EaseSalez" + "/";

    public static String[] url_name = {"S", "L"};

    //Encrypted Data:
    //public static String app_version = "1.0.3";
    public static String app_version = BuildConfig.VERSION_NAME;

    // public static String[] urlarray = {"http://220.225.104.144/SmartSalezApiStaging/", "http://220.225.104.144/easeSalezApi/"};
    public static String[] urlarray = {"http://14.141.212.203/SmartSalezApiStaging/", "http://115.110.148.101/smartsalezapi/"};
    public static String[] stage = {"TEST", "PROD"};
    public static String[] appid = {"16919f368e09276caef07fb4d91961", "45599ba885d1452f49711d22d99554"};

    //7825 8875 78
    public static int url_type = 1;

    public static String MOBILE_API = String.valueOf(urlarray[url_type]);
    public static String app_version_name = "V " + app_version + " " + url_name[url_type];
    public static String MOBILENO_VALIDATE = MOBILE_API + "Registration/MobileNumValidate";
    public static String RESEND_OTP = MOBILE_API + "Registration/ResendOTP";
    public static String OTP_VERIFICATION = MOBILE_API + "Registration/OTPVerification";
    public static String CALL_VERIFICATION = "http://220.225.104.138:5000/initiateCall?phoneNumber=";
    public static String REGISTRATION = MOBILE_API + "Registration/AddConsumerRegistration";
    public static String LOGIN = MOBILE_API + "Registration/ConsumerLogIn";
    public static String CHANGE_PASSWORD = MOBILE_API + "Registration/UpdConsumerChangePassword";
    //public static String GET_STORES = MOBILE_API + "Consumer/GetPOSDetails";
    //public static String GET_STORES = MOBILE_API + "Master/POSDetails";
    public static String GET_STORES = MOBILE_API + "Consumer/GetConsumerPOSDetails";
    public static String GET_DOCTYPE = MOBILE_API + "Master/DocumentType";
    public static String GET_CATEGORY = MOBILE_API + "Master/GetCategory";
    public static String GET_PRODUCTLIST = MOBILE_API + "Consumer/GetConsumerPrdList";
    // public static String ADD_PRODUCT = MOBILE_API + "Consumer/InsCart";
    public static String ADD_PRODUCT = MOBILE_API + "Consumer/AddCartAttribute";
    public static String REMOVE_PRODUCT = MOBILE_API + "Consumer/DelCart";
    public static String GET_CART = MOBILE_API + "Consumer/GetCart";
    public static String BANNER = MOBILE_API + "Master/GetPosBanner";
    // public static String PLACE_ORDER = MOBILE_API + "Indent/IndentDetails";
    public static String PLACE_ORDER = MOBILE_API + "Indent/IndentDetailsV1";
    public static String ORDER_HISTORY = MOBILE_API + "Indent/GetConsumerIndentDetails";
    // public static String ORDER_HISTORY = MOBILE_API + "Indent/GetIndentDetails";
    public static String HISTORY_DETAIL = MOBILE_API + "Indent/GetIndentPrds";
    public static String CANCEL_ORDER = MOBILE_API + "Indent/UpdIndentStatus";
    public static String PROFILE_UPDATE = MOBILE_API + "Registration/UpdateConsumerDetails";
    public static String GET_PROFILE = MOBILE_API + "Registration/GetConsumerDetails";
    public static String FORGET_PASSWORD = MOBILE_API + "Registration/UpdConsumerForgotPassword";
    public static String REVIEW = MOBILE_API + "Registration/InsConsumerReview";
    public static String PROFILE_PIC = MOBILE_API + "Registration/UpdProfileImage";
    public static String GET_PAYMENTMODE = MOBILE_API + "Consumer/GetPaymentMode";
    public static String PRIVACY_POLICY = "http://smartsalez.in/Account/PrivacyPolicy";
    public static String GET_MAINCATAGORY = MOBILE_API + "Consumer/GetLoadCategories";
    public static String PAYMENT = MOBILE_API + "PaymentGateway/UpdPaymentGateWay";
    public static String PAYMENT_DUE = MOBILE_API + "Report/GetConsumerCreditBalanceDetails";

    //manage address
    public static String GET_ADDRESS = MOBILE_API + "Consumer/GetConsumerAddress";
    public static String DELETE_ADDRESS = MOBILE_API + "Consumer/DelConsumerAddress";
    public static String ADD_ADDRESS = MOBILE_API + "Consumer/InsConsumerAddress";
    public static String SET_DEFAULT = MOBILE_API + "Consumer/UpdConsumerAddress";
    public static String ORDER_SCAN = MOBILE_API + "Consumer/InsConsumerOrderImage";

    // public static String SHOP_FILTER = MOBILE_API + "Master/GetPOSType";
    public static String SHOP_FILTER = MOBILE_API + "Master/GetProductType";
    public static String ADD_WISHLIST = MOBILE_API + "Consumer/InsWishList";
    public static String GET_WISHLIST = MOBILE_API + "Consumer/GetWishlist";
    public static String DELETE_WISHLIST = MOBILE_API + "Consumer/DelWishlist";
    public static String ERROR_LOG = MOBILE_API + "MobileLog/InsMobileLogs";
    public static String SCAN_QR = MOBILE_API + "Consumer/InsQRPOSConsumer";
    public static String GET_CARTCOUNT = MOBILE_API + "Registration/GetCartCount";

    //Detail Page
    public static String GET_PROD_DETAIL = MOBILE_API + "Product/ProductParticulars";
    public static String GET_ATTR_PRICE = MOBILE_API + "Product/GetAttributeProductPrize";
    public static String PROD_REVIEW = MOBILE_API + "Registration/InsProductReview";
    public static String APPLY_COUPON = MOBILE_API + "Coupon/GetCouponValidation";
    public static String VEHICLE_FILTER = MOBILE_API + "Consumer/GetVehicleFilter";
    public static String APPLY_FILTER = MOBILE_API + "Consumer/GetMobileappPrdSearch";
    public static String GET_QUOTE = MOBILE_API + "Report/GetQuote";
    public static String DELIVERY_DETAILS = MOBILE_API + "Indent/GetOrderDeliveryDetails";

    public static String CALCULATE_DELIVERY = MOBILE_API + "Indent/GetDeliveryChargesCalc";
    public static String UNIVERSAL_SEARCH = MOBILE_API + "AIR/UniversalAutoSearch";
    public static String UNIVERSAL_PROD_SEARCH = MOBILE_API + "AIR/UniversalProductSearch";

    public static String NEW_REG = MOBILE_API.replace("SmartSalezApiStaging/", "") + "SmartSalezCPStaging/account/MarathonRegistration?ConsumerId=";
    public static String shop_address = "";
    public static String contact_person = "";
    public static String mobileno = "";
    public static String working_hours = "";
    public static String shopimg = "";
    public static String ShowQRDATA = "";
    public static boolean SHOWALERT = true;
    public static boolean UPDATECHARGES = false;

    //payment details staging
    public static String STAGE = String.valueOf(stage[url_type]);
    public static String APP_ID = String.valueOf(appid[url_type]);

    private SharedPreferences preferences;

    public Util(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public synchronized static boolean isFirstLaunch(Context context) {
        boolean launchFlag = false;
        SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("install", false);
        return launchFlag;
    }

    public static final class Operations {
        private Operations() throws InstantiationException {
            throw new InstantiationException("This class is not for instantiation");
        }

        public static boolean isOnline(Context context) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }
    }

    public static void hideKeypad(Context context, View view) {
        final InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void saveData(String key, String value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("smartparking", Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getData(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("smartparking", Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static void clearSession(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("smartparking", Activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static String getDeviceId(Context ctx) {
        String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static boolean isEmailValid(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;

    }

    //
    public static boolean ValidateVehicleNo(String vehno) {
        String expression = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$";
        CharSequence inputStr = vehno;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static String getStringFromFile(String filePath) throws Exception {

        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;

    }

    public static boolean isOnline(Context con) {

        ConnectivityManager cm = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Log.i("netInfo", "" + netInfo);
            return true;
        }
        return false;

    }

    public static String getFileExtension(String filePath) {

        String ext = null;

        int i = filePath.lastIndexOf('.');

        if (i > 0 && i < filePath.length() - 1) {
            ext = filePath.substring(i + 1).toLowerCase();
        }

        return ext;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @SuppressLint("NewApi")
    public static String EncryptURL(String data) {

        String strret = "";
        try {
            byte[] sharedkey = "A1234&ABCDE/98745#000078".getBytes();
            byte[] sharedvector = {8, 7, 5, 6, 4, 1, 2, 3, 18, 17, 15, 16, 14, 11, 12, 13};
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sharedkey, "AES"), new IvParameterSpec(sharedvector));
            byte[] encrypted = c.doFinal(data.getBytes("UTF-8"));
            // strret = Base64.getEncoder().encodeToString(encrypted);
            strret = Base64.encodeToString(encrypted, Base64.DEFAULT);
            strret = strret.replace("\n", "");
            strret = URLEncoder.encode(strret, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e("Encrypted Data:", strret);
        return strret;
    }

    public static String Decrypt(String data) {

        String decrypt = "";
        byte[] sharedkey = "A1234&ABCDE/98745#000078".getBytes();
        byte[] sharedvector = {8, 7, 5, 6, 4, 1, 2, 3, 18, 17, 15, 16, 14, 11, 12, 13};

        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sharedkey, "AES"), new IvParameterSpec(sharedvector));
            byte[] decrypted = c.doFinal(Base64.decode((URLDecoder.decode(data, "UTF-8")), Base64.DEFAULT));
            decrypt = new String(decrypted, "UTF-8");

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        System.out.print("\nDECRYPT" + decrypt);

        return decrypt;

    }

    public static String converttobase64(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public static String getdate(String date) {
        //  SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss aa");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String datetime = date.substring(0, 10);
        return datetime;
    }

    public static String gettime(String date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss aa");
        String datetime = date.substring(10, 19);
        return datetime;
    }

    public static String readjson(String filepath) throws IOException {

        File yourFile = new File(filepath);

        FileInputStream stream = new FileInputStream(yourFile);
        String jString = null;
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            jString = Charset.defaultCharset().decode(bb).toString();

        } finally {
            stream.close();
        }
        return jString;
    }

    /*public static String ChangeNumber(String value) {
        double values = Double.parseDouble(value);
        return NumberFormat.getInstance().format(values);
    }*/

    public static String clearjson(String filepath) {
        try {
            Writer output = null;
            File file = new File(filepath);
            output = new BufferedWriter(new FileWriter(file));
            output.write("");
            output.close();

        } catch (Exception e) {
        }
        return "";
    }

    public static List<Integer> extractNumbers(String s) {

        List<Integer> numbers = new ArrayList<Integer>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }
        return numbers;
    }

    public static String ShowToast(Context con, String Msg) {
        Toast.makeText(con, Msg, Toast.LENGTH_LONG);
        return "";
    }

    public static String getAddress(double latitude, double longitude, Context con) {
        Logcat.e("getLocality :" + latitude + longitude);
        String result = "";
        try {
            Geocoder geocoder = new Geocoder(con, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                // result = address.getFeatureName() + " " + address.getLocality() + " " + address.getAdminArea() + " " + address.getCountryName() + " " + address.getPostalCode();
                result = address.getLocality();

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        if (result == null) {
            result = "Location NA";
        }

        // return "Chennai";
        // return result.replace("null", "");
        return result;
    }

    public static String comparedate(String oldtime, String newtime) {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(oldtime);
            d2 = format.parse(newtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d1.getTime() - d2.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        //Log.e("date",  String.format("%02d", diffHours) + ":" + String.format("%02d", diffMinutes)+ ":" + String.format("%02d", diffSeconds) + " " + "Hours");
        Log.e("difference min", String.valueOf(minutes));
        return String.valueOf(minutes);
    }

    public static String getonlydate() {
        Calendar c = Calendar.getInstance();
        // SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String datetime = dateformat.format(c.getTime());
        return datetime.replace("-", "/");
    }

    public static String getdatetime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        //SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        String datetime = dateformat.format(c.getTime());
        return datetime.replace("-", "/");
    }

    public static String only2time() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("kk");
        //SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        String datetime = dateformat.format(c.getTime());
        return datetime.replace("-", "/");
    }

    public static String ErrorLog(Context context, String method) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("AppType", "1");
            obj.put("LoginId", getData("LoginId", context.getApplicationContext()));
            obj.put("ErrorDescription", method);
            obj.put("UserId", getData("ConsumerId", context.getApplicationContext()));

            Util.Logcat.e("ERROR:::" + obj.toString());
            String data = Util.EncryptURL(obj.toString());
            JSONObject params = new JSONObject();
            params.put("Getrequestresponse", data);
            CallApi.postResponseNopgrss(context, params.toString(), ERROR_LOG, new VolleyResponseListener() {
                @Override
                public void onError(String message) {

                    Util.Logcat.e("onError" + message);
                }

                @Override
                public void onResponse(JSONObject response) {
                    //Util.Logcat.e("onResponse : " + response);
                    try {
                        Util.Logcat.e("ERROR:::" + Util.Decrypt(response.getString("Postresponse")));
                        JSONObject resobject = new JSONObject(Util.Decrypt(response.getString("Postresponse")));
                        /*if (resobject.getString("Status").equalsIgnoreCase("0")) {

                        } else if (resobject.getString("Status").equalsIgnoreCase("1")) {

                        }*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String BitMapToString(Bitmap bitmap) throws IOException {

       /* ByteBuffer buffer =
                ByteBuffer.allocate(bitmap.getRowBytes() *
                        bitmap.getHeight());
        bitmap.copyPixelsToBuffer(buffer);
        byte[] data = buffer.array();
        Log.e("IMAGE", Base64.encodeToString(data, Base64.DEFAULT));*/

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public static Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static HashMap<String, Integer> sortByDistance(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static JSONArray SortJSONArray(JSONArray json, final String value) {

        JSONArray sortedJsonArray = new JSONArray();

        try {
            JSONArray jsonArr = json;

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }
            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                private final String KEY_NAME = value;

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    Double valA = 0.0;
                    Double valB = 0.0;

                    try {
                        valA = a.getDouble(KEY_NAME);
                        valB = b.getDouble(KEY_NAME);
                    } catch (JSONException e) {
                        Log.e("MainActivity", e.getMessage());
                    }

                    if (valA < valB) {
                        return -1;
                    } else if (valB < valA) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            for (int i = 0; i < jsonArr.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
        } catch (JSONException e) {
            Log.e("MainActivity", e.getMessage());
        }
        return sortedJsonArray;
    }

    public static JSONArray sortbydistance(JSONArray array, final String value) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsons, new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;
                try {
                    lid = lhs.getString(value);
                    rid = rhs.getString(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
                // return rid.compareTo(lid);
            }
        });
        return new JSONArray(jsons);
    }

    public static boolean registration(String select) throws ParseException {

        boolean value = false;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            Date eventdate = formatter.parse(select);
            Date currentdate = formatter.parse(getonlydate());

            if (eventdate.after(currentdate)||eventdate.equals(currentdate)) {
                Logcat.e("IF :" + eventdate + " <after> " + currentdate);
                value = true;
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return value;

    }

    public static JSONArray sortbyshop(JSONArray array, final String value) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;
                try {
                    lid = lhs.getString(value);
                    rid = rhs.getString(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
                // return rid.compareTo(lid);
            }
        });
        return new JSONArray(jsons);
    }

    public static JSONArray sortbyrating(JSONArray array, final String value) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;
                try {
                    lid = lhs.getString(value);
                    rid = rhs.getString(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Here you could parse string id to integer and then compare.
                //return lid.compareTo(rid);
                return rid.compareTo(lid);
            }
        });
        return new JSONArray(jsons);
    }

    public static boolean checkTimepickup(String startTime, String endTime, String checkTime) throws ParseException {
        Log.e("checkTime:", checkTime);
        int from = Integer.parseInt(startTime);
        int to = Integer.parseInt(endTime) + 12;
        int now = 0;
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date newDate = spf.parse(checkTime);
            spf = new SimpleDateFormat("HH:mm");
            String date = spf.format(newDate);

            now = Integer.parseInt(date.substring(0, 2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean isBetween = false;
        SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date hai = form.parse(checkTime);
        Date date2 = form.parse(getdatetime());
        Log.e("1:", from + "<" + now);
        Log.e("2:", now + "<" + to);
        if (from <= now && now < to) {
            isBetween = true;
            Logcat.e("IF: " + isBetween);
        } /*else if (hai.compareTo(date2) < 0 && from < now && now < to) {
            isBetween = true;
            Logcat.e("IF ELSE: " + isBetween);
        }*/
        Logcat.e("isBetween: " + isBetween);
        return isBetween;

    }

    public static boolean Datecompare(String from, String to) throws ParseException {
        boolean value = false;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            Date date1 = formatter.parse(from);
            Date date2 = formatter.parse(to);

            if (date1.compareTo(date2) < 0) {
                value = true;
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return value;

    }

    public static boolean DateTimecompare(String select, String current) throws ParseException {

        boolean value = false;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            Date selectdate = formatter.parse(select);
            Date currentdate = formatter.parse(current);

            if (selectdate.after(currentdate)) {
                Logcat.e("IF :" + selectdate + " <after> " + currentdate);
                value = true;
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return value;

    }


    public static boolean validtime(String time) throws ParseException {
        boolean value = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(time);
        if (System.currentTimeMillis() > strDate.getTime()) {
            value = true;
        }
        Log.e("value", String.valueOf(value));
        return value;

    }

    public static int get2value() {
        String dsa = Util.only2time();
        Log.e("INT TIME", dsa);
        return Integer.parseInt(dsa);

    }

    public static boolean validdatetime(String time) throws ParseException {
        boolean value = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:kk");
        Date strDate = sdf.parse(time);
        //strDate.
        if (System.currentTimeMillis() < strDate.getDate()) {
            value = true;
        }
        Log.e("validdatetime 1", String.valueOf(value));
        Log.e("TIME 1", String.valueOf(strDate.getTime()));
        return value;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean checkDelivery(String shopfrom, String shopto, String from, String to) throws ParseException {
        int sfrom = Integer.parseInt(shopfrom);
        int sto = Integer.parseInt(shopto);
        int userfrom = 0;
        int userto = 0;
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date newDate1 = spf.parse(from);
            Date newDate2 = spf.parse(to);
            spf = new SimpleDateFormat("HH:mm");
            String date1 = spf.format(newDate1);
            String date2 = spf.format(newDate2);
            Log.e("userfrom:", date1);
            Log.e("userto:", date2);
            userfrom = Integer.parseInt(date1.substring(0, 2));
            userto = Integer.parseInt(date2.substring(0, 2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean isBetween = false;
        SimpleDateFormat yes = new SimpleDateFormat("dd/MM/yyyy");
        Date newDate1 = yes.parse(from);
        Date newDate2 = yes.parse(getonlydate());
        Log.e("1:", sfrom + "<=" + userfrom + userto + "<=" + sto);
        if (sfrom <= userfrom && userto <= sto) {
            isBetween = true;
            Log.e("ONE", "1");
        }

       /* if(newDate1.after(newDate2) && sfrom < userfrom && userto < sto){
            isBetween = true;
            Log.e("TWO", "2");
        }*/
        /*if (sfrom  userfrom && userto < sto) {
            isBetween = true;
        }*/
        Logcat.e("isBetween: " + isBetween);
        return isBetween;

    }

    public static class Logcat {

        private static final String TAG = "SMART SALEZ";

        public static void e(String msg) {
            /*if (url_type == 0) {
                Log.e(TAG, msg);
            }*/
            Log.e(TAG, msg);
        }
    }
}

