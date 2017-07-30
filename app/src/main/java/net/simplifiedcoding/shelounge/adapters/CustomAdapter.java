package net.simplifiedcoding.shelounge.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.models.DataModel;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context mC;


    private ArrayList<DataModel> dataSet;


    public CustomAdapter(Context c, ArrayList<DataModel> data) {
        this.dataSet = data;
        this.mC = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView complaint_id = holder.complaint_id;
        TextView complaint_date = holder.complaint_date;
        TextView complaint_status = holder.complaint_status;
        ImageView imageView = holder.imageViewIcon;

        int s = dataSet.get(listPosition).getStatus();
        complaint_id.setText(String.valueOf(dataSet.get(listPosition).getId()));
        complaint_date.setText(dataSet.get(listPosition).getDate());

        if (s == 0) {
            complaint_status.setText("Pending");
            complaint_status.setTextColor(Color.RED);
        } else {
            complaint_status.setText("Done");
            complaint_status.setTextColor(Color.GREEN);
        }

       /* Drawable myDrawable = mC.getResources().getDrawable(R.drawable.do_sel);
        imageView.setImageDrawable(myDrawable);*/
        //  Picasso.with(mC).load(dataSet.get(listPosition).getImage()).resize(120, 60).into(imageView);

        // load the background image
        if (dataSet.get(listPosition).getImage() != null) {
            Glide.with(mC)
                    .load(dataSet.get(listPosition).getImage().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView complaint_id, complaint_status, complaint_date;

        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.complaint_id = (TextView) itemView.findViewById(R.id.complaint_number);
            this.complaint_date = (TextView) itemView.findViewById(R.id.complaint_date);
            this.complaint_status = (TextView) itemView.findViewById(R.id.complaint_status);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.image_background);
        }
    }
}