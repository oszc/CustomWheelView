package com.zc.customwheelview.wheelview;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import kankan.wheel.widget.ArrayWheelAdapter;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

import java.util.Calendar;
import java.util.Date;

/**
 * 8/25/14  11:36 PM
 * Created by JustinZhang.
 */
public class DateDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private Calendar c ;
    private String[]arrYear,arrMonth,arrDay;
    private WheelView ssYear,ssMonth,ssDay;
    private int id;
    private Button bt_cancel;
    private static final String DEFAULTDATE="date";

    private OnWheelViewConfirmListener mOnWheelViewConfirmListener;
    /**
     * int类型 前N年,默认为50
     */
    public static final String YEARPREVNUM="yearPrevNum";
    /**
     * int类型 后N年,默认为50
     */
    public static final String YEARNEXTNUM="yearNextNum";
    private static final String TAG = "ScrollSelectActivity";

    public DateDialog(Context context) {
        super(context);
        sharedConstruct(context);
    }

    public DateDialog(Context context, int theme) {
        super(context, theme);
        sharedConstruct(context);
    }

    public DateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        sharedConstruct(context);
    }

    private void sharedConstruct(Context context){

        mContext = context;
        setContentView(R.layout.date_dialog);

        Window dialogWindow = getWindow();
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        double rote = d.getWidth() / 480.0;
        p.width = (int) (480 * rote);
        dialogWindow.setAttributes(p);

        Date da = new Date();

        //最多向前显示多少年
        int yearPrevNum = 100;
        int yearNextNum = 100;

        /*
        if(yearPrevNum==0&& yearNextNum==0){
            yearNextNum=8;
        }
        if(yearPrevNum == 0){
            yearPrevNum = 8;
        }
        //最多向后多少年
        if(yearNextNum == 0){
            yearNextNum = 1;
        }
        */
        init(da,yearPrevNum,yearNextNum);

        findViewById(R.id.ss_confirm).setOnClickListener(this);
        findViewById(R.id.ss_cancel).setOnClickListener(this);
    }

    /**
     * 根据当前日期初始化
     * @param d
     */
    private void init(Date d,int yearPrevNum,int yearNextNum){
        Date date = new Date();
        c = Calendar.getInstance();

        //当前日期
        int currYear = 1900+date.getYear();
        int currMonth = date.getMonth();

        //判断最小日期和最大日期
        int minYear = currYear-yearPrevNum;
        int yearNum = yearPrevNum+yearNextNum;

        arrYear = new String[yearNum];
        arrYear = setArr(arrYear,minYear-1);
        arrMonth = new String[12];
        arrMonth = setArr(arrMonth);
        c.setTime(d==null?date:d);
        int c_day = c.get(Calendar.DAY_OF_MONTH);
        int c_month = c.get(Calendar.MONTH);
        int c_year = c.get(Calendar.YEAR);
        arrDay = new String[getDaysOfMonth(c_year, c_month)];
        arrDay = setArr(arrDay);

        ssYear = (WheelView)findViewById(R.id.year);
        ssYear.setVisibleItems(7);
        ssYear.setLabel("年");
        ssYear.setAdapter(new ArrayWheelAdapter<String>(arrYear,4));
//		ssYear.setCurrentItem(d==null?0:d.getYear()-(currYear-1900));
        ssYear.setCurrentItem(findCurIndex(arrYear, c_year+""));

        ssMonth = (WheelView)findViewById(R.id.month);
        ssMonth.setVisibleItems(7);
        ssMonth.setLabel("月");
        ssMonth.setAdapter(new ArrayWheelAdapter<String>(arrMonth,4));
        ssMonth.setCurrentItem(c_month);

        ssDay = (WheelView)findViewById(R.id.day);
        ssDay.setVisibleItems(7);
        ssDay.setLabel("日");
        ssDay.setAdapter(new ArrayWheelAdapter<String>(arrDay,4));
        ssDay.setCurrentItem(c_day-1);

        ssYear.addChangingListener(changeListener);
        ssMonth.addChangingListener(changeListener);
    }
    /**
     * 年月改变时个改日
     */
    private OnWheelChangedListener changeListener = new OnWheelChangedListener() {

        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int year,month;
            if(wheel == ssYear){
                year = Integer.valueOf(arrYear[newValue]);
                month = Integer.valueOf(arrMonth[ssMonth.getCurrentItem()])-1;
            }else{
                year = Integer.valueOf(arrYear[ssYear.getCurrentItem()]);
                month = Integer.valueOf(arrMonth[newValue])-1;
            }
            arrDay = setArr(new String[getDaysOfMonth(year, month)]);
            ssDay.setAdapter(new ArrayWheelAdapter<String>(arrDay));
            //更新当前日，以免超出下标
            int curDay = 0;

            if(ssDay.getCurrentItem() > arrDay.length-1){
                curDay = arrDay.length-1;
            }else{
                curDay = ssDay.getCurrentItem();
            }

            ssDay.setCurrentItem(curDay,true);

        }
    };

    /**
     * 返回某年某月有多少日
     * @param year
     * @param month
     * @return
     */
    private int getDaysOfMonth(int year,int month){
        c.set(year, month, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    /**
     * 初始化数组
     * @param arr
     * @return
     */
    private String[] setArr(String[] arr){
        return setArr(arr, 0);
    }
    private int findCurIndex(String[] s,String date) {
        for(int i=0;i<s.length;i++){
            if(date.equals(s[i])){
                return i;
            }
        }
        return 0;
    }
    private String[] setArr(String[] arr,int n){
        for(int i=0;i<arr.length;i++){
            arr[i]=(i+1+n)<10?"0"+(i+1+n):(i+1+n)+"";
        }
        return arr;
    }

    public void setOnWheelViewConfirmListener(OnWheelViewConfirmListener listener) {
        this.mOnWheelViewConfirmListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ss_confirm:
                String date = arrYear[ssYear.getCurrentItem()]+"-"+
                        arrMonth[ssMonth.getCurrentItem()]+"-"+
                        arrDay[ssDay.getCurrentItem()];
                if(mOnWheelViewConfirmListener!=null){
                    mOnWheelViewConfirmListener.onConfirm(date);
                }
                dismiss();
                break;
            case R.id.ss_cancel:
                if(mOnWheelViewConfirmListener!=null){
                    mOnWheelViewConfirmListener.onCancel();
                }
                dismiss();
                break;
        }
    }

    /*
	public void enter(View v){

		Intent intent = new Intent();
		String date = arrYear[ssYear.getCurrentItem()]+"-"+
				arrMonth[ssMonth.getCurrentItem()]+"-"+
				arrDay[ssDay.getCurrentItem()];
		intent.putExtra("date", date);
		setResult(id,intent);
		finish();
	}
	public void cancel(View v){
		finish();
	}
	*/



}
