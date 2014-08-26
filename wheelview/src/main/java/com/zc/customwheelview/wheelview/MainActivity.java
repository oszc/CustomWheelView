package com.zc.customwheelview.wheelview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements OnWheelViewConfirmListener, View.OnClickListener {



    DateDialog mDateDialog;
    TimeDialog mTimeDialog;
    @InjectView(R.id.tv)
    TextView mTv;
    @InjectView(R.id.bt_date)
    Button mBtDate;
    @InjectView(R.id.bt_time)
    Button mBtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mDateDialog = new DateDialog(MainActivity.this, R.style.CustomDialogStyle);
        mTimeDialog = new TimeDialog(MainActivity.this, R.style.CustomDialogStyle);

        mDateDialog.setOnWheelViewConfirmListener(this);

        mTimeDialog.setOnWheelViewConfirmListener(this);

        mBtDate.setOnClickListener(this);
        mBtTime.setOnClickListener(this);


    }


    @Override
    public void onConfirm(String info) {
        mTv.setText(info);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_date:
                mDateDialog.show();
                break;
            case R.id.bt_time:
                mTimeDialog.show();
                break;
        }
    }
}
