package kankan.wheel.widget;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 3/26/14  3:55 PM
 * Created by JustinZhang.
 */
public class Utils {

    private static final String TAG ="Utils" ;

    public static boolean isJson(String json){
        try {
            JSONObject obj = new JSONObject(json);
        } catch (JSONException e) {
            return false ;
        }
        return true;
    }

    /**
     * 匹配Url，并返回
     *
     * @param content
     * @return urls.size()=0 如果字符串中没有URL
     */
    public static List<String> getMatchedUrl(String content) {
        String pattern = "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&][%]]*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);



        List<String> urls = new ArrayList<String>();
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String s = content.substring(start,end);
            boolean matches = Patterns.WEB_URL.matcher(s).matches();
            if(matches)
                 urls.add(content.substring(start, end));

        }
        return urls;
    }

    /**
     * 保留两位小数
     * @param unrounded
     * @return
     */
    public static String  round(double unrounded)
    {
       return new DecimalFormat("###,##0.00").format(unrounded);
    }

    /**
     * 隐藏银行卡号
     * 隐藏60%信息
     * 如果小于3个字符，则全部隐藏
     * 大于3个字符，中间的60%隐藏
     * @param s
     * @return
     */
    public static String hideInfo(String s){

        if(isEmailAddress(s)) {
            String[] str = s.split("@");

            if (str == null || str.length != 2) {
                return "";

            }

            String username = hideInfoFun(str[0]);

            return username + "@" + str[1];
        }
        return hideInfoFun(s);

    }

    private static String hideInfoFun(String s ){
        if(TextUtils.isEmpty(s)){
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int length = s.length();
        if(length<4){

            for(int i = 0 ; i < s.length() ;i++){
                if(i==0){
                    sb.append(s.charAt(i));
                }else{
                    sb.append("*");
                }
            }

            return sb.toString();

        }

        int hideLength =(int)Math.floor(length*0.6);

        int start = (length-hideLength)/2;
        int end = start + hideLength;

        for(int i = 0 ; i < length ; i++){
            if(i>start && i<=end){
                sb.append("*");
            }else{
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isEmailAddress(String email){
        if(TextUtils.isEmpty(email)){
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }

        return false;
    }


    public static Intent getStringJumpIntent(String packageName,String activityName,Map<String,String> extras) {
        Intent intent = new Intent();
        intent.setClassName(packageName,activityName);
        if(extras!=null){
            for(Map.Entry<String,String>entry :extras.entrySet()){
                intent.putExtra(entry.getKey(),entry.getValue());
            }
        }
        return intent;
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertSpToPixel(float sp){
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        float px = scaledDensity *  sp;
        return px;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



}
