package com.bbcc.mobilehealth.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import com.bbcc.mobilehealth.R;




public class WheelView extends View {
    /**
     * 鎺т欢瀹藉害
     */
    private float controlWidth;
    /**
     * 鎺т欢楂樺害
     */
    private float controlHeight;
    /**
     * 鏄惁婊戝姩涓?
     */
    private boolean isScrolling = false;
    /**
     * 閫夋嫨鐨勫唴瀹?
     */
    private ArrayList<ItemObject> itemList = new ArrayList<ItemObject>();
    /**
     * 璁剧疆鏁版嵁
     */
    private ArrayList<String> dataList = new ArrayList<String>();
    /**
     * 鎸変笅鐨勫潗鏍?
     */
    private int downY;
    /**
     * 鎸変笅鐨勬椂闂?
     */
    private long downTime = 0;
    /**
     * 鐭績绉诲姩
     */
    private long goonTime = 200;
    /**
     * 鐭績绉诲姩璺濈
     */
    private int goonDistence = 100;
    /**
     * 鐢荤嚎鐢荤瑪
     */
    private Paint linePaint;
    /**
     * 搴曢儴鐢荤嚎鐢荤瑪
     */
    private Paint bottomlinePaint;
    /**
     * 搴曢儴绾挎潯鍜屼笂闈㈠垎鍓茬嚎鐨勯棿璺?
     */
    float firstLineAndSecondLineSpace = 50;
    /**
     * 绾跨殑榛樿棰滆壊
     */
    private int lineColor = 0xffff0000;
    /**
     * 搴曠嚎鐨勯粯璁ら鑹?
     */
    private int bottomLineColor = 0xffDADADA;
    /**
     * 绾跨殑榛樿瀹藉害
     */
    private float lineWidth = 2f;
    /**
     * 榛樿瀛椾綋
     */
    private float normalFont = 14.0f;
    /**
     * 閫変腑鐨勬椂鍊欏瓧浣?
     */
    private float selectedFont = 22.0f;
    /**
     * 鍗曞厓鏍奸珮搴?
     */
    private int unitHeight = 50;
    /**
     * 鏄剧ず澶氬皯涓唴瀹?
     */
    private int itemNumber = 7;
    /**
     * 榛樿瀛椾綋棰滆壊
     */
    private int normalColor = 0xff000000;
    /**
     * 閫変腑鏃跺?欑殑瀛椾綋棰滆壊
     */
    private int selectedColor = 0xffff0000;
    /**
     * 钂欐澘楂樺害
     */
    private float maskHight = 48.0f;
    /**
     * 閫夋嫨鐩戝惉
     */
    private OnSelectListener onSelectListener;
    /**
     * 鏄惁鍙敤
     */
    private boolean isEnable = true;
    /**
     * 鍒锋柊鐣岄潰
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 绉诲姩璺濈
     */
    private static final int MOVE_NUMBER = 5;
    /**
     * 鏄惁鍏佽閫夌┖
     */
    private boolean noEmpty = true;

    /**
     * 姝ｅ湪淇敼鏁版嵁锛岄伩鍏岰oncurrentModificationException寮傚父
     */
    private boolean isClearing = false;

    public WheelView(Context context, AttributeSet attrs,
                     int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
        initData();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        initData();
    }

    public WheelView(Context context) {
        super(context);
        initData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!isEnable)
            return true;
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScrolling = true;
                downY = (int) event.getY();
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(y - downY);
                onSelectListener();
                break;
            case MotionEvent.ACTION_UP:
                int move = Math.abs(y - downY);
                // 鍒ゆ柇娈垫椂闂寸Щ鍔ㄧ殑璺濈
                if (System.currentTimeMillis() - downTime < goonTime
                        && move > goonDistence) {
                    goonMove(y - downY);
                } else {
                    actionUp(y - downY);
                }
                noEmpty();
                isScrolling = false;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLine(canvas);
        drawList(canvas);
        drawMask(canvas);
    }

    private synchronized void drawList(Canvas canvas) {
        if (isClearing)
            return;
        try {
            for (ItemObject itemObject : itemList) {
                itemObject.drawSelf(canvas);
            }
        } catch (Exception e) {
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        controlWidth = getWidth();
        if (controlWidth != 0) {
            setMeasuredDimension(getWidth(), itemNumber * unitHeight);
            controlWidth = getWidth();
        }

    }

    /**
     * 缁х画绉诲姩涓?瀹氳窛绂?
     */
    private synchronized void goonMove(final int move) {
        new Thread(new Runnable() {

            public void run() {
                int distence = 0;
                while (distence < unitHeight * MOVE_NUMBER) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actionThreadMove(move > 0 ? distence : distence * (-1));
                    distence += 10;

                }
                actionUp(move > 0 ? distence - 10 : distence * (-1) + 10);
                noEmpty();
            }
        }).start();
    }

    /**
     * 涓嶈兘涓虹┖锛屽繀椤绘湁閫夐」
     */
    private void noEmpty() {
        if (!noEmpty)
            return;
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return;
        }
        int move = (int) itemList.get(0).moveToSelected();
        if (move < 0) {
            defaultMove(move);
        } else {
            defaultMove((int) itemList.get(itemList.size() - 1)
                    .moveToSelected());
        }
        for (ItemObject item : itemList) {
            if (item.isSelected()) {
                if (onSelectListener != null)
                    onSelectListener.endSelect(item.id, item.itemText);
                break;
            }
        }
    }

    /**
     * 鍒濆鍖栨暟鎹?
     */
    private void initData() {
        isClearing = true;
        itemList.clear();
        for (int i = 0; i < dataList.size(); i++) {
            ItemObject itmItemObject = new ItemObject();
            itmItemObject.id = i;
            itmItemObject.itemText = dataList.get(i);
            itmItemObject.x = 0;
            itmItemObject.y = i * unitHeight;
            itemList.add(itmItemObject);
        }
        isClearing = false;
    }

    /**
     * 绉诲姩鐨勬椂鍊?
     *
     * @param move
     */
    private void actionMove(int move) {
        for (ItemObject item : itemList) {
            item.move(move);
        }
        invalidate();
    }

    /**
     * 绉诲姩锛岀嚎绋嬩腑璋冪敤
     *
     * @param move
     */
    private void actionThreadMove(int move) {
        for (ItemObject item : itemList) {
            item.move(move);
        }
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);
    }

    /**
     * 鏉惧紑鐨勬椂鍊?
     *
     * @param move
     */
    private void actionUp(int move) {
        int newMove = 0;
        if (move > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                if (itemList.get(i).isSelected()) {
                    newMove = (int) itemList.get(i).moveToSelected();
                    if (onSelectListener != null)
                        onSelectListener.endSelect(itemList.get(i).id,
                                itemList.get(i).itemText);
                    break;
                }
            }
        } else {
            for (int i = itemList.size() - 1; i >= 0; i--) {
                if (itemList.get(i).isSelected()) {
                    newMove = (int) itemList.get(i).moveToSelected();
                    if (onSelectListener != null)
                        onSelectListener.endSelect(itemList.get(i).id,
                                itemList.get(i).itemText);
                    break;
                }
            }
        }
        for (ItemObject item : itemList) {
            item.newY(move + 0);
        }
        slowMove(newMove);
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);

    }

    /**
     * 缂撴參绉诲姩
     *
     * @param move
     */
    private synchronized void slowMove(final int move) {
        new Thread(new Runnable() {

            public void run() {
                // 鍒ゆ柇鏀垮簻
                int m = move > 0 ? move : move * (-1);
                int i = move > 0 ? 1 : (-1);
                // 绉诲姩閫熷害
                int speed = 1;
                while (true) {
                    m = m - speed;
                    if (m <= 0) {
                        for (ItemObject item : itemList) {
                            item.newY(m * i);
                        }
                        Message rMessage = new Message();
                        rMessage.what = REFRESH_VIEW;
                        handler.sendMessage(rMessage);
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    for (ItemObject item : itemList) {
                        item.newY(speed * i);
                    }
                    Message rMessage = new Message();
                    rMessage.what = REFRESH_VIEW;
                    handler.sendMessage(rMessage);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (ItemObject item : itemList) {
                    if (item.isSelected()) {
                        if (onSelectListener != null)
                            onSelectListener.endSelect(item.id, item.itemText);
                        break;
                    }
                }

            }
        }).start();
    }

    /**
     * 绉诲姩鍒伴粯璁や綅缃?
     *
     * @param move
     */
    private void defaultMove(int move) {
        for (ItemObject item : itemList) {
            item.newY(move);
        }
        Message rMessage = new Message();
        rMessage.what = REFRESH_VIEW;
        handler.sendMessage(rMessage);
    }

    /**
     * 婊戝姩鐩戝惉
     */
    private void onSelectListener() {
        if (onSelectListener == null)
            return;
        for (ItemObject item : itemList) {
            if (item.isSelected()) {
                onSelectListener.selecting(item.id, item.itemText);
            }
        }
    }

    /**
     * 缁樺埗绾挎潯
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {

        if (linePaint == null) {
            linePaint = new Paint();
            linePaint.setColor(bottomLineColor);
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(lineWidth / 2);
        }
        canvas.drawLine(0, controlHeight / 2 - unitHeight / 2 + 2,
                controlWidth, controlHeight / 2 - unitHeight / 2 + 2, linePaint);
        canvas.drawLine(0, controlHeight / 2 + unitHeight / 2 - 2,
                controlWidth, controlHeight / 2 + unitHeight / 2 - 2, linePaint);

        if (bottomlinePaint == null) {
            bottomlinePaint = new Paint();
            bottomlinePaint.setColor(lineColor);
            bottomlinePaint.setAntiAlias(true);
            bottomlinePaint.setStrokeWidth(lineWidth);
        }
        canvas.drawLine(firstLineAndSecondLineSpace, controlHeight / 2 - unitHeight / 2 + 2,
                controlWidth- firstLineAndSecondLineSpace, controlHeight / 2 - unitHeight / 2 + 2, bottomlinePaint);
        canvas.drawLine(firstLineAndSecondLineSpace, controlHeight / 2 + unitHeight / 2 - 2,
                controlWidth- firstLineAndSecondLineSpace, controlHeight / 2 + unitHeight / 2 - 2, bottomlinePaint);

    }

    /**
     * 缁樺埗閬洊鏉?
     *
     * @param canvas
     */
    private void drawMask(Canvas canvas) {
        LinearGradient lg = new LinearGradient(0, 0, 0, maskHight, 0x00f2f2f2,
                0x00f2f2f2, TileMode.MIRROR);
        Paint paint = new Paint();
        paint.setShader(lg);
        canvas.drawRect(0, 0, controlWidth, maskHight, paint);

        LinearGradient lg2 = new LinearGradient(0, controlHeight - maskHight,
                0, controlHeight, 0x00f2f2f2, 0x00f2f2f2, TileMode.MIRROR);
        Paint paint2 = new Paint();
        paint2.setShader(lg2);
        canvas.drawRect(0, controlHeight - maskHight, controlWidth,
                controlHeight, paint2);
    }

    /**
     * 鍒濆鍖栵紝鑾峰彇璁剧疆鐨勫睘鎬?
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray attribute = context.obtainStyledAttributes(attrs,
                R.styleable.WheelView);
        unitHeight = (int) attribute.getDimension(
                R.styleable.WheelView_unitHight, 32);
        normalFont = attribute.getDimension(
                R.styleable.WheelView_normalTextSize, 14.0f);
        selectedFont = attribute.getDimension(
                R.styleable.WheelView_selectedTextSize, 22.0f);
        itemNumber = attribute.getInt(R.styleable.WheelView_itemNumber, 7);
        normalColor = attribute.getColor(
                R.styleable.WheelView_normalTextColor, 0xff000000);
        selectedColor = attribute.getColor(
                R.styleable.WheelView_selectedTextColor, 0xffff0000);
        lineColor = attribute.getColor(R.styleable.WheelView_lineColor,
                0xff000000);
        bottomLineColor = attribute.getColor(R.styleable.WheelView_bottomLineColor,
                0xffDADADA);
        lineWidth = attribute.getDimension(R.styleable.WheelView_lineHeight, 2f);
        firstLineAndSecondLineSpace = attribute.getDimension(R.styleable.WheelView_firstLineAndSecondLineSpace, 40f);
        maskHight = attribute.getDimension(R.styleable.WheelView_maskHight,
                48.0f);
        noEmpty = attribute.getBoolean(R.styleable.WheelView_noEmpty, true);
        isEnable = attribute
                .getBoolean(R.styleable.WheelView_isEnable, true);
        attribute.recycle();

        controlHeight = itemNumber * unitHeight;

    }

    /**
     * 璁剧疆鏁版嵁 锛堢涓?娆★級
     *
     * @param data
     */
    public void setData(ArrayList<String> data) {
        this.dataList = data;
        initData();
    }

    /**
     * 閲嶇疆鏁版嵁
     *
     * @param data
     */
    public void resetData(ArrayList<String> data) {
        setData(data);
        invalidate();
    }

    /**
     * 鑾峰彇杩斿洖椤? id
     *
     * @return
     */
    public int getSelected() {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.id;
        }
        return -1;
    }

    /**
     * 鑾峰彇杩斿洖鐨勫唴瀹?
     *
     * @return
     */
    public String getSelectedText() {
        for (ItemObject item : itemList) {
            if (item.isSelected())
                return item.itemText;
        }
        return "";
    }

    /**
     * 鏄惁姝ｅ湪婊戝姩
     *
     * @return
     */
    public boolean isScrolling() {
        return isScrolling;
    }

    /**
     * 鏄惁鍙敤
     *
     * @return
     */
    public boolean isEnable() {
        return isEnable;
    }

    /**
     * 璁剧疆鏄惁鍙敤
     *
     * @param isEnable
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * 璁剧疆榛樿閫夐」
     *
     * @param index
     */
    public void setDefault(int index) {
        if (index > itemList.size() - 1)
            return;
        float move = itemList.get(index).moveToSelected();
        defaultMove((int) move);
    }

    /**
     * 鑾峰彇鍒楄〃澶у皬
     *
     * @return
     */
    public int getListSize() {
        if (itemList == null)
            return 0;
        return itemList.size();
    }

    /**
     * 鑾峰彇鏌愰」鐨勫唴瀹?
     *
     * @param index
     * @return
     */
    public String getItemText(int index) {
        if (itemList == null)
            return "";
        return itemList.get(index).itemText;
    }

    /**
     * 鐩戝惉
     *
     * @param onSelectListener
     */
    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    invalidate();
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 鍗曟潯鍐呭
     *
     * @author JiangPing
     */
    private class ItemObject {
        /**
         * id
         */
        public int id = 0;
        /**
         * 鍐呭
         */
        public String itemText = "";
        /**
         * x鍧愭爣
         */
        public int x = 0;
        /**
         * y鍧愭爣
         */
        public int y = 0;
        /**
         * 绉诲姩璺濈
         */
        public int move = 0;
        /**
         * 瀛椾綋鐢荤瑪
         */
        private Paint textPaint;
        /**
         * 瀛椾綋鑼冨洿鐭╁舰
         */
        private Rect textRect;

        public ItemObject() {
            super();
        }

        /**
         * 缁樺埗鑷韩
         *
         * @param canvas
         */
        public void drawSelf(Canvas canvas) {

            if (textPaint == null) {
                textPaint = new Paint();
                textPaint.setAntiAlias(true);
            }

            if (textRect == null)
                textRect = new Rect();

            // 鍒ゆ柇鏄惁琚?夋嫨
            if (isSelected()) {
                textPaint.setColor(selectedColor);
                // 鑾峰彇璺濈鏍囧噯浣嶇疆鐨勮窛绂?
                float moveToSelect = moveToSelected();
                moveToSelect = moveToSelect > 0 ? moveToSelect : moveToSelect
                        * (-1);
                // 璁＄畻褰撳墠瀛椾綋澶у皬
                float textSize = (float) normalFont
                        + ((float) (selectedFont - normalFont) * (1.0f - (float) moveToSelect
                        / (float) unitHeight));
                textPaint.setTextSize(textSize);
            } else {
                textPaint.setColor(normalColor);
                textPaint.setTextSize(normalFont);
            }

            // 杩斿洖鍖呭洿鏁翠釜瀛楃涓茬殑鏈?灏忕殑涓?涓猂ect鍖哄煙
            textPaint.getTextBounds(itemText, 0, itemText.length(), textRect);
            // 鍒ゆ柇鏄惁鍙
            if (!isInView())
                return;

            // 缁樺埗鍐呭
            canvas.drawText(itemText, x + controlWidth / 2 - textRect.width()
                            / 2, y + move + unitHeight / 2 + textRect.height() / 2,
                    textPaint);

        }

        /**
         * 鏄惁鍦ㄥ彲瑙嗙晫闈㈠唴
         *
         * @return
         */
        public boolean isInView() {
            if (y + move > controlHeight
                    || (y + move + unitHeight / 2 + textRect.height() / 2) < 0)
                return false;
            return true;
        }

        /**
         * 绉诲姩璺濈
         *
         * @param _move
         */
        public void move(int _move) {
            this.move = _move;
        }

        /**
         * 璁剧疆鏂扮殑鍧愭爣
         *
         * @param _move
         */
        public void newY(int _move) {
            this.move = 0;
            this.y = y + _move;
        }

        /**
         * 鍒ゆ柇鏄惁鍦ㄩ?夋嫨鍖哄煙鍐?
         *
         * @return
         */
        public boolean isSelected() {
            if ((y + move) >= controlHeight / 2 - unitHeight / 2 + 2
                    && (y + move) <= controlHeight / 2 + unitHeight / 2 - 2)
                return true;
            if ((y + move + unitHeight) >= controlHeight / 2 - unitHeight / 2
                    + 2
                    && (y + move + unitHeight) <= controlHeight / 2
                    + unitHeight / 2 - 2)
                return true;
            if ((y + move) <= controlHeight / 2 - unitHeight / 2 + 2
                    && (y + move + unitHeight) >= controlHeight / 2
                    + unitHeight / 2 - 2)
                return true;
            return false;
        }

        /**
         * 鑾峰彇绉诲姩鍒版爣鍑嗕綅缃渶瑕佺殑璺濈
         */
        public float moveToSelected() {
            return (controlHeight / 2 - unitHeight / 2) - (y + move);
        }
    }

    /**
     * 閫夋嫨鐩戝惉
     *
     * @author JiangPing
     */
    public interface OnSelectListener {
        /**
         * 缁撴潫閫夋嫨
         *
         * @param id
         * @param text
         */
        public void endSelect(int id, String text);

        /**
         * 閫変腑鐨勫唴瀹?
         *
         * @param id
         * @param text
         */
        public void selecting(int id, String text);

    }
}
