package com.employee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import main.employee.com.employeeleavemanagement.R;

/**
 * Created by Srisht on 13-09-2017.
 */

public class EmployeeViewAdapter extends RecyclerView.Adapter<EmployeeViewAdapter.ViewHolerAdapter>
        implements View.OnClickListener, RecyclerView.OnItemTouchListener{

    ImageView imgEmployee;
    TextView tvEmployeeName;
    LinkedList<EmployeeAdapter> linkedList;
    Context contextAdapter;

    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    public EmployeeViewAdapter(Context context,LinkedList<EmployeeAdapter> linkedList){
        this.linkedList = linkedList;
        this.contextAdapter = context;
    }

    public EmployeeViewAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public ViewHolerAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCartRecycler = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_recycler_list, parent, false);
        ViewHolerAdapter viewHolerAdapter = new ViewHolerAdapter(viewCartRecycler);
        return viewHolerAdapter;
    }

    @Override
    public void onBindViewHolder(ViewHolerAdapter holder, final int position) {
        Picasso.with(contextAdapter).load(linkedList.get(position).getEmpImgLink()).into(imgEmployee/*.imageMoviePoster*/);
        tvEmployeeName.setText(linkedList.get(position).getEmpName());
        imgEmployee.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return linkedList.size();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onClick(View v) {
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolerAdapter extends RecyclerView.ViewHolder{
        public ViewHolerAdapter(View itemView) {
            super(itemView);
            imgEmployee = (ImageView) itemView.findViewById(R.id.imgViewEmpPic);
            tvEmployeeName = (TextView) itemView.findViewById(R.id.textViewEmpName);
            /*imgEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linkedList.get(getAdapterPosition());
                }
            });*/
        }
    }
}
