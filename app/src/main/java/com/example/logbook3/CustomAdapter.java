package com.example.logbook3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList demo_id;
    private final ArrayList demo_name;
    private final ArrayList demo_dob;
    private final ArrayList demo_email;
    private final ArrayList demo_image;

    CustomAdapter(Context context, ArrayList demo_id, ArrayList demo_name, ArrayList demo_dob, ArrayList demo_email, ArrayList demo_image){
        this.context = context;
        this.demo_id = demo_id;
        this.demo_name = demo_name;
        this.demo_dob = demo_dob;
        this.demo_email = demo_email;
        this.demo_image = demo_image;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.demo_name_txt.setText(String.valueOf(demo_name.get(position)));
        holder.demo_dob_txt.setText(String.valueOf(demo_dob.get(position)));
        holder.demo_email_txt.setText(String.valueOf(demo_email.get(position)));
        int resourceId = context.getResources().getIdentifier(String.valueOf(demo_image.get(position)), "drawable", context.getPackageName());
        holder.demo_image.setImageResource(resourceId);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(demo_id.get(position)));
                intent.putExtra("name", String.valueOf(demo_name.get(position)));
                intent.putExtra("dob", String.valueOf(demo_dob.get(position)));
                intent.putExtra("email", String.valueOf(demo_email.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return demo_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView demo_name_txt, demo_dob_txt, demo_email_txt;
        ImageView demo_image;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            demo_name_txt = itemView.findViewById(R.id.name_txt);
            demo_dob_txt = itemView.findViewById(R.id.dob_txt);
            demo_email_txt = itemView.findViewById(R.id.email_txt);
            demo_image = itemView.findViewById(R.id.image_img);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
