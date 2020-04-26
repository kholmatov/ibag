package com.tinyminds.ibag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


public class FileAdapter extends BaseAdapter {
    private Context mContext;
    private List<File> mFiles;

    public FileAdapter(Context context, List<File> files) {
        mContext = context;
        mFiles = files;
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    public File getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_item, parent, false);
            holder = new ViewHolder();
            holder.fileImageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.firtsLineTextView = (TextView) convertView.findViewById(R.id.firstLine);
            holder.secondLineTextView = (TextView) convertView.findViewById(R.id.secondLine);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.firtsLineTextView.setText(file.getTitle());
        holder.secondLineTextView.setText(file.getFile());
        holder.fileImageView.setImageResource(R.drawable.ic_pdf);

        return convertView;
    }

    static class ViewHolder {
        ImageView fileImageView;
        TextView firtsLineTextView;
        TextView secondLineTextView;
    }
}
