package com.kplike.library.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Description: 适配器
 * Copyright:   Copyright (c)2015
 * Company:     广东道一信息科技有限公司
 * author:      Passer
 * version:     1.0
 * date:        2016/1/14 17:05
 * <p>
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2016/1/14   Passer      1.0         1.0 Version
 */
public class BaseAdapterAdvance<T> extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Class<T>> list;
    private int layoutId;
    private ItemViewHandler mItemViewHandler;

    public BaseAdapterAdvance(Context context, List<Class<T>> list, int layoutId,
                              ItemViewHandler mItemViewHandler){
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutId = layoutId;
        this.mItemViewHandler = mItemViewHandler;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, null);
            viewHolder = new ViewHolder(convertView,list.get(position));
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        mItemViewHandler.binderViewHolder(position, viewHolder);
        return convertView;
    }

    /**
     * bind adapter holder
     */
    public static class ViewHolder{
        private SparseArray<View> mViews;
        private View mConvertView;
        private Object data;
        public ViewHolder(View mConvertView,Object data){
            this.mConvertView = mConvertView;
            this.data = data;
            mViews = new SparseArray<View>();
        }

        /**
         * Get view in the item
         * @param viewId
         * @return T
         */
        public <T extends View> T getView(int viewId){
            View view = mViews.get(viewId);
            if(view == null){
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T)view;
        }

        /**
         * Get item data
         */
//        public <T> T getItem() {
//            return (T)data;
//        }

        /**
         * Get the itemView
         */
        public View getmConvertView(){
            return mConvertView;
        }
    }

    /**
     * Bind adapter interface
     */
    public interface ItemViewHandler {
        public void  binderViewHolder(int position, ViewHolder vh);
    }
}
