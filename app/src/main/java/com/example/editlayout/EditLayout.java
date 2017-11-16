package com.example.editlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Description:综合信息控件
 * <p>
 * Time: 2017/11/9 0008
 */
public class EditLayout extends RelativeLayout {

    private Context mContext;

    private TextView mTvTxtTag;//item的名称
    private FrameLayout mFlContent;//显示的内容
    private ImageView mIvNext;//下一页图标

    private int mTagColor;//tag的颜色
    private int mHintColor;//hint值的颜色
    private int mContentColor;//内容的颜色

    private int mTagSize;//tag的size
    private int mContentSize;//内容的size

    private boolean mIsHasNextPage;//是否有二级页面

    private String mTagTxt;//tag的文字内容
    private String mContentTxt;//Content的文字内容
    private String mHintTxt;//hint的文字内容

    private int mLines;//行数
    private int mMaxLines;//最大行数
	
    private TextUtils.TruncateAt mEllipsize;//折叠方式
    private static final int ELLIPSIZE_START = 0;
    private static final int ELLIPSIZE_MIDDLE = 1;
    private static final int ELLIPSIZE_END= 2;
    private static final int ELLIPSIZE_MARQUEE = 3;

    private static final int DEFAULT_LINE = 1;//默认输入一行

    private CONTENT_WIDGET mContentWidget;//内容的控件
    private static final int CONTENT_EDIT = 0;
    private static final int CONTENT_TEXT = 1;

    private int mContentGravity;//内容的填充方式
    private static final int CONTENT_GRAVITY_LEFT = -1;
    private static final int CONTENT_GRAVITY_CENTER = 0;
    private static final int CONTENT_GRAVITY_RIGHT = 1;

    private onEditLayoutEventLisenter mLisenter;//监听

    private int DEFAULT_TAG_SIZE = 48;//默认字体大小
    private int DEFAULT_CONTENT_SIZE = 48;//默认字体大小

    private EditText etContent;//输入框控件
    private TextView tvContent;//文本控件

    private int mInputType;//输入类型

    private static final int INPUT_TYPE_NUMBER = 0;
    private static final int INPUT_TYPE_TEXT = 1;
    private static final int INPUT_TYPE_DATE = 2;
    private static final int INPUT_TYPE_DATETIME = 3;
    private static final int INPUT_TYPE_NUMBERDECIMAL = 4;
    private static final int INPUT_TYPE_NUMBERPASSWORD = 5;
    private static final int INPUT_TYPE_NUMBERSIGNED = 6;
    private static final int INPUT_TYPE_PHONE = 7;
    private static final int INPUT_TYPE_TEXTAUTOCOMPLETE = 8;
    private static final int INPUT_TYPE_TEXTAUTOCORRECT = 9;
    private static final int INPUT_TYPE_TEXTEMAILADDRESS = 10;
    private static final int INPUT_TYPE_TEXTFILTER = 11;
    private static final int INPUT_TYPE_TEXTPASSWORD = 12;
    private static final int INPUT_TYPE_TEXTPERSONNAME = 13;
    private static final int INPUT_TYPE_TEXTURI = 14;
    private static final int INPUT_TYPE_TEXTVISIBLEPASSWORD = 15;

    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    //内容的控件：EditText或者TextView
    private enum CONTENT_WIDGET {
        EDITTEXT, TEXTVIEW
    }

    public EditLayout(Context context) {
        this(context, null);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initAttr(attrs);
        initView();
    }

    /**
     * 设置
     *
     * @param contentTxt
     */
    public void setContentTxt(String contentTxt) {
        mContentTxt = contentTxt;
//        invalidate();//只能在主线程中调用
        postInvalidate();//可以在子线程中调用
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EditLayout);

        mTagColor = typedArray.getColor(R.styleable.EditLayout_tag_color, getResources().getColor(R.color.default_black));
        mHintColor = typedArray.getColor(R.styleable.EditLayout_hint_color, getResources().getColor(R.color.default_gray_light));
        mContentColor = typedArray.getColor(R.styleable.EditLayout_content_color, getResources().getColor(R.color.default_black));

        mTagSize = typedArray.getDimensionPixelSize(R.styleable.EditLayout_tag_size, DEFAULT_TAG_SIZE);
        mContentSize = typedArray.getDimensionPixelSize(R.styleable.EditLayout_content_size, DEFAULT_CONTENT_SIZE);

        mIsHasNextPage = typedArray.getBoolean(R.styleable.EditLayout_has_next_page, false);

        mTagTxt = typedArray.getString(R.styleable.EditLayout_tag_txt);
        mContentTxt = typedArray.getString(R.styleable.EditLayout_content_txt);
        mHintTxt = typedArray.getString(R.styleable.EditLayout_hint_txt);

        mLines = typedArray.getInt(R.styleable.EditLayout_lines, DEFAULT_LINE);
        mMaxLines = typedArray.getInt(R.styleable.EditLayout_max_line, DEFAULT_LINE);
        int ellipsize = typedArray.getInt(R.styleable.EditLayout_ellipsize, ELLIPSIZE_END);
        if (ellipsize == ELLIPSIZE_START) {
            mEllipsize = TextUtils.TruncateAt.START;
        } else if (ellipsize == ELLIPSIZE_MIDDLE) {
            mEllipsize = TextUtils.TruncateAt.MIDDLE;
        } else if (ellipsize == ELLIPSIZE_END) {
            mEllipsize = TextUtils.TruncateAt.END;
        } else if (ellipsize == ELLIPSIZE_END) {
            mEllipsize = TextUtils.TruncateAt.MARQUEE;
        }

        int widgetType = typedArray.getInt(R.styleable.EditLayout_content_widget, CONTENT_EDIT);
        if (widgetType == CONTENT_EDIT) {
            mContentWidget = CONTENT_WIDGET.EDITTEXT;
        } else if (widgetType == CONTENT_TEXT) {
            mContentWidget = CONTENT_WIDGET.TEXTVIEW;
        }

        int contentGravity = typedArray.getInt(R.styleable.EditLayout_content_gravity, CONTENT_GRAVITY_LEFT);
        if (contentGravity == CONTENT_GRAVITY_LEFT) {
            mContentGravity = Gravity.LEFT;
        } else if (contentGravity == CONTENT_GRAVITY_CENTER) {
            mContentGravity = Gravity.CENTER;
        } else if (contentGravity == CONTENT_GRAVITY_RIGHT) {
            mContentGravity = Gravity.RIGHT;
        }

        int inputType = typedArray.getInt(R.styleable.EditLayout_input_type, InputType.TYPE_CLASS_TEXT);
        if (inputType == INPUT_TYPE_NUMBER) {//number
            mInputType = InputType.TYPE_CLASS_NUMBER;
        } else if (inputType == INPUT_TYPE_TEXT) {//text
            mInputType = InputType.TYPE_CLASS_TEXT;
        } else if (inputType == INPUT_TYPE_DATE) {//date
            mInputType = InputType.TYPE_DATETIME_VARIATION_DATE;
        } else if (inputType == INPUT_TYPE_DATETIME) {//datetime
            mInputType = InputType.TYPE_CLASS_DATETIME;
        } else if (inputType == INPUT_TYPE_NUMBERDECIMAL) {//numberDecimal
            mInputType = InputType.TYPE_NUMBER_FLAG_DECIMAL;
        } else if (inputType == INPUT_TYPE_NUMBERPASSWORD) {//numberPassword
            mInputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        } else if (inputType == INPUT_TYPE_NUMBERSIGNED) {//numberSigned
            mInputType = InputType.TYPE_NUMBER_FLAG_SIGNED;
        } else if (inputType == INPUT_TYPE_PHONE) {//phone
            mInputType = InputType.TYPE_CLASS_PHONE;
        } else if (inputType == INPUT_TYPE_TEXTAUTOCOMPLETE) {//textAutoComplete
            mInputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
        } else if (inputType == INPUT_TYPE_TEXTAUTOCORRECT) {//textAutoCorrect
            mInputType = InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;
        } else if (inputType == INPUT_TYPE_TEXTEMAILADDRESS) {//textEmailAddress
            mInputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
        } else if (inputType == INPUT_TYPE_TEXTFILTER) {//textFilter
            mInputType = InputType.TYPE_TEXT_VARIATION_FILTER;
        } else if (inputType == INPUT_TYPE_TEXTPASSWORD) {//textPassword
            mInputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;
        } else if (inputType == INPUT_TYPE_TEXTPERSONNAME) {//textPersonName
            mInputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME;
        } else if (inputType == INPUT_TYPE_TEXTURI) {//textUri
            mInputType = InputType.TYPE_TEXT_VARIATION_URI;
        } else if (inputType == INPUT_TYPE_TEXTVISIBLEPASSWORD) {//textVisiblePassword
            mInputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        }

        typedArray.recycle();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        View view = View.inflate(getContext(), R.layout.lay_edit_layout, this);

        mTvTxtTag = view.findViewById(R.id.tv_txt_tag);
        mFlContent = view.findViewById(R.id.fl_content);
        mIvNext = view.findViewById(R.id.iv_next);

        //tag的初始化
        mTvTxtTag.setText(mTagTxt);
        mTvTxtTag.setTextColor(mTagColor);
        mTvTxtTag.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTagSize);
        AutoUtils.autoTextSize(mTvTxtTag);

        //内容的组件的初始化
        if (mContentWidget == CONTENT_WIDGET.EDITTEXT) {
            etContent = new EditText(mContext);

            etContent.setInputType(mInputType);
            etContent.setLines(mLines);
            etContent.setMaxLines(mMaxLines);
            etContent.setEllipsize(mEllipsize);
            etContent.setText(mContentTxt);
            etContent.setTextColor(mContentColor);
            etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentSize);

            etContent.setBackgroundDrawable(null);
            etContent.setPadding(0, 0, 0, 0);

            etContent.setHint(mHintTxt);
            etContent.setHintTextColor(mHintColor);

            etContent.setGravity(mContentGravity | Gravity.CENTER_VERTICAL);

            mFlContent.removeAllViews();
            mFlContent.addView(etContent);

            AutoUtils.autoTextSize(etContent);

        } else if (mContentWidget == CONTENT_WIDGET.TEXTVIEW) {
            tvContent = new TextView(mContext);

            tvContent.setInputType(mInputType);
            tvContent.setLines(mLines);
            tvContent.setMaxLines(mMaxLines);
            tvContent.setEllipsize(mEllipsize);
            tvContent.setText(mContentTxt);
            tvContent.setTextColor(mContentColor);
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentSize);

            tvContent.setHint(mHintTxt);
            tvContent.setHintTextColor(mHintColor);

            tvContent.setGravity(mContentGravity | Gravity.CENTER_VERTICAL);

            mFlContent.removeAllViews();
            mFlContent.addView(tvContent);

            AutoUtils.autoTextSize(tvContent);
        }

        //是否有二级页面
        if (mIsHasNextPage) {
            mIvNext.setVisibility(VISIBLE);
            this.setOnClickListener(v -> {
                mLisenter.onClickItem(mContentTxt);
            });
        } else {
            mIvNext.setVisibility(INVISIBLE);
        }

        AutoUtils.autoSize(this);
    }

    @Override
    public EditLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new EditLayout.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams implements AutoLayoutHelper.AutoLayoutParams {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo() {
            return mAutoLayoutInfo;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mContentWidget == CONTENT_WIDGET.EDITTEXT) {
            if (etContent != null) {
                etContent.setText(mContentTxt);
            }
        } else if (mContentWidget == CONTENT_WIDGET.TEXTVIEW) {
            if (tvContent != null) {
                tvContent.setText(mContentTxt);
            }
        }
    }

    //整个Item的点击事件
    public interface onEditLayoutEventLisenter {
        void onClickItem(String content);
    }

    public void setOnEditLayoutEventLisenter(onEditLayoutEventLisenter onClickItemLisenter) {
        mLisenter = onClickItemLisenter;
    }
}
