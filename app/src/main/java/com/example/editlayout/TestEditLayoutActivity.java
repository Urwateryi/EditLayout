package com.example.editlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Description:
 * <p>
 * Time: 2017/11/13 0013
 */
public class TestEditLayoutActivity extends AutoLayoutActivity {

    private EditLayout mEtLayout1;
    private Button mBtnClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_edit_layout_activity);

        mEtLayout1 = findViewById(R.id.et_layout1);
        mBtnClick = findViewById(R.id.btn_click);

        mBtnClick.setOnClickListener(v -> {
            mEtLayout1.setContentTxt(mBtnClick.getText().toString());
        });

        mEtLayout1.setOnEditLayoutEventLisenter(content -> {
            Toast.makeText(this, "进入二级页面", Toast.LENGTH_LONG).show();
        });
    }
}
