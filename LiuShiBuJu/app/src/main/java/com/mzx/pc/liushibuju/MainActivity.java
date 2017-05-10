package com.mzx.pc.liushibuju;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import cn.bingoogolapple.flowlayout.BGAFlowLayout;

public class MainActivity extends AppCompatActivity {
//    private String[] mVals = new String[]{"bingo", "googol", "apple", "bingoogolapple", "helloworld"};
    private ArrayList<String> mVals=new ArrayList<>();
    private BGAFlowLayout mFlowLayout;
    private EditText mTagEt;
    private Button confirm,delete;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建SQLiteOpenHelper实例
        dbHelper = new MyDatabaseHelper(this, "buju.db", null, 1);
        //创建数据库
        dbHelper.getWritableDatabase();

        confirm = (Button)findViewById(R.id.confirm);
        delete = (Button)findViewById(R.id.delete);
        mTagEt = (EditText) findViewById(R.id.et_main_tag);
        mFlowLayout = (BGAFlowLayout) findViewById(R.id.flowlayout);
        //填充数据
        query();
        initData();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = mTagEt.getText().toString().trim();
                if (!TextUtils.isEmpty(tag)) {
                    mFlowLayout.addView(getLabel(tag), mFlowLayout.getChildCount() - 1, new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
                    mFlowLayout.setVisibility(View.VISIBLE);
                    //把数据加到数据库表中
                    SQLiteDatabase dbOperate= dbHelper.getWritableDatabase();
                    ContentValues values= new ContentValues();
                    //下面没有给表中的id赋值，因为在建表的时候，id是默认自动增长的
                    //添加第一条记录到Book
                    values.put( "name", tag);
                    dbOperate.insert( "buju", null, values);
                    values.clear();
                }
                mTagEt.setText("");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlowLayout.removeAllViews();
                mFlowLayout.setVisibility(View.GONE);

                //将数据库清空
                SQLiteDatabase dbOperate= dbHelper.getWritableDatabase();
                ContentValues values= new ContentValues();
                //下面没有给表中的id赋值，因为在建表的时候，id是默认自动增长的
                dbOperate.insert( "buju", null, values);
                dbOperate.delete("buju",null,null);
            }
        });


        //EditText在软键盘上编辑完后，点击软键盘上的确认按钮，会触发
//        mTagEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_GO) {
//                    String tag = mTagEt.getText().toString().trim();
//                    if (!TextUtils.isEmpty(tag)) {
//                        mFlowLayout.addView(getLabel(tag), mFlowLayout.getChildCount() - 1, new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
//                    }
//                    mTagEt.setText("");
//                }
//                return true;
//            }
//        });
    }



    public void initData() {
        if(mVals!=null&&mVals.size()>0) {
            for (int i = 0; i < mVals.size(); i++) {
                mFlowLayout.setVisibility(View.VISIBLE);
                mFlowLayout.addView(getLabel(mVals.get(i)), mFlowLayout.getChildCount() - 1, new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
            }
        }
    }

    private TextView getLabel(String text) {
        TextView label = new TextView(this);
        label.setTextColor(Color.WHITE);
        label.setBackgroundResource(R.drawable.selector_tag);
        label.setGravity(Gravity.CENTER);
        label.setSingleLine(true);
        label.setEllipsize(TextUtils.TruncateAt.END);
        int padding = BGAFlowLayout.dp2px(this, 5);
        label.setPadding(padding, padding, padding, padding);
        label.setText(text);
        return label;
    }

    //查询数据
    public void query() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //参数以此表示 表名 列名 where约束条件 占位符填充值 groudby having orderBy
        Cursor cursor = db.query("buju", null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            //也可以使用sql语句,达到同样效果
            //db.rawQuery("select * from buju",null);
            mVals.add(name);
        }
        cursor.close();
        //也可以使用sql语句,达到同样效果
        //db.execSQL("delete from buju where name=?", new String[]{"ben"});
    }
}
