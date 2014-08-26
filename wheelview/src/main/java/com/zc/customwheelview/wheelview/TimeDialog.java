package com.zc.customwheelview.wheelview;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kankan.wheel.widget.NumericWheelAdapter;
import kankan.wheel.widget.WheelView;

import java.util.Calendar;

/**
 * 8/26/14  10:37 AM
 * Created by JustinZhang.
 */
public class TimeDialog extends Dialog implements View.OnClickListener {
    @InjectView(R.id.bt_confirm)
    Button mBtConfirm;
    @InjectView(R.id.bt_cancel)
    Button mBtCancel;
    @InjectView(R.id.wv_hours)
    WheelView mWvHours;
    @InjectView(R.id.wv_minutes)
    WheelView mWvMinutes;
    @InjectView(R.id.wv_seconds)
    WheelView mWvSeconds;
    @InjectView(R.id.ss_boday)
    LinearLayout mSsBoday;
    private Context mContext;

    private OnWheelViewConfirmListener mOnWheelViewConfirmListener;

    public TimeDialog(Context context) {
        super(context);
        sharedConstruction(context);
    }

    public TimeDialog(Context context, int theme) {
        super(context, theme);
        sharedConstruction(context);
    }

    public TimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        sharedConstruction(context);
    }

    private void sharedConstruction(Context context) {
        mContext = context;

        init();
    }

    private void init() {
        setContentView(R.layout.time_dialog);
        ButterKnife.inject(this);


        Window dialogWindow = getWindow();
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        double rote = d.getWidth() / 480.0;
        p.width = (int) (480 * rote);
        dialogWindow.setAttributes(p);


        mWvHours.setVisibleItems(5);
        mWvHours.setLabel("时");
        mWvHours.setAdapter(new NumericWheelAdapter( 0, 23));

        mWvMinutes.setVisibleItems(5);
        mWvMinutes.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        mWvMinutes.setLabel("分");


        mWvSeconds.setVisibleItems(5);
        mWvSeconds.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        mWvSeconds.setLabel("秒");

        Calendar c = Calendar.getInstance();
        int curHours = c.get(Calendar.HOUR_OF_DAY);
        int curMinutes = c.get(Calendar.MINUTE);
        int curSeconds = c.get(Calendar.SECOND);

        mWvHours.setCurrentItem(curHours);
        mWvMinutes.setCurrentItem(curMinutes);
        mWvSeconds.setCurrentItem(curSeconds);

        mBtConfirm.setOnClickListener(this);
        mBtCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.bt_confirm:

                int hour = mWvHours.getCurrentItem();
                int minute = mWvMinutes.getCurrentItem();
                int sec = mWvSeconds.getCurrentItem();
                if(mOnWheelViewConfirmListener!=null){
                    mOnWheelViewConfirmListener.onConfirm(convertTimeToString(hour)+":"+convertTimeToString(minute)+":"+convertTimeToString(sec));
                }
                dismiss();
                break;
            case R.id.bt_cancel:
                dismiss();
                break;
        }
    }

    public void setOnWheelViewConfirmListener(OnWheelViewConfirmListener mOnWheelViewConfirmListener) {
        this.mOnWheelViewConfirmListener = mOnWheelViewConfirmListener;
    }

    public String convertTimeToString(int time){
        if(time<10){
            return "0"+time;
        }
        return time+"";
    }
}
