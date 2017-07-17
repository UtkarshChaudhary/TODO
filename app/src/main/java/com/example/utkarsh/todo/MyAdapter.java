package com.example.utkarsh.todo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Utkarsh on 7/16/2017.
 */

public class MyAdapter extends ArrayAdapter<String>{

    Context context;
    ArrayList<Details> detailsArrayList;
    public MyAdapter(Context context, ArrayList<Details> detailsArrayList) {
        super(context, 0);
        this.context=context;
        this.detailsArrayList=detailsArrayList;
    }

    @Override
    public int getCount() {
        return detailsArrayList.size();
    }
    static class ExpenseViewHolder
    {
        TextView title;
        TextView date;
        TextView category;
        TextView priority;
        public ExpenseViewHolder(TextView title,TextView category,TextView priority,TextView date)
        {
            this.title=title;
            this.date=date;
            this.category=category;
            this.priority=priority;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.list_item,null);
            TextView title=convertView.findViewById(R.id.title);
            TextView date=convertView.findViewById(R.id.date);
            TextView category=convertView.findViewById(R.id.category);
            TextView priority=convertView.findViewById(R.id.priority);
            ExpenseViewHolder expense=new ExpenseViewHolder(title,category,priority,date);
            convertView.setTag(expense);
        }
        Details details=detailsArrayList.get(position);
        ExpenseViewHolder expense=(ExpenseViewHolder) convertView.getTag();
        expense.title.setText(details.title);
        expense.date.setText(details.date);
        expense.category.setText(details.category);
        expense.priority.setText(details.priority);
        return convertView;
    }
}
