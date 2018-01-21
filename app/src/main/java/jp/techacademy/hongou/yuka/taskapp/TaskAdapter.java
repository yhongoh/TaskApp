package jp.techacademy.hongou.yuka.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by hongoyuka on 2018/01/14.
 */

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private List<Task> mTaskList;

    public TaskAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskList(List<Task> taskList) {
        mTaskList = taskList;
    }

    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTaskList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView ==null) {
            convertView = mLayoutInflater.inflate(R.layout.list_layout, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.text2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.text3);

        textView2.setText(mTaskList.get(position).getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskList.get(position).getDate();
        textView3.setText(simpleDateFormat.format(date));

        textView1.setText(mTaskList.get(position).getCategory());

        return convertView;
        }
    }
