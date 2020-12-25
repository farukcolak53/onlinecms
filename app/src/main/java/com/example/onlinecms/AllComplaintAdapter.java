package com.example.onlinecms;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecms.model.Complaint;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class AllComplaintAdapter extends RecyclerView.Adapter<AllComplaintAdapter.MyViewHandle> {

    List<Complaint> complaintList;
    Context context;
    Dialog dialog;
    private FirebaseStorage firebaseStorage;

    public AllComplaintAdapter( Context context) {
        this.complaintList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<Complaint> complaints){
        complaintList.addAll(complaints);
    }

    public void addSingleComplaint(Complaint complaint){
        complaintList.add(complaint);
    }
    @NonNull
    @Override
    public MyViewHandle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.dialog_complaint,parent,false);
        return new MyViewHandle(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandle holder, int position) {
            holder.title.setText(complaintList.get(position).getTitle());
            holder.description.setText(complaintList.get(position).getDescription());
            holder.address.setText(complaintList.get(position).getAddress());
            GlideApp.with(holder.image.getContext()).load(firebaseStorage.getReferenceFromUrl(complaintList.get(position).getImageUrl())).into(holder.image);
//        holder.title.setText(complaintList.get(position).getTitle());
//
//        dialog = new Dialog(holder.cardLayout.getContext());
//        dialog.setContentView(R.layout.dialog_complaint);
//        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView dialogTitle = dialog.findViewById(R.id.dialog_title_content);
//                TextView dialogDescription = dialog.findViewById(R.id.dialog_description_content);
//                TextView dialogAddress = dialog.findViewById(R.id.dialog_address_content);
//                ImageView imageView = dialog.findViewById(R.id.dialog_image_view);
//
//                dialogTitle.setText(complaintList.get(position).getTitle());
//                dialogDescription.setText(complaintList.get(position).getDescription());
//                dialogAddress.setText(complaintList.get(position).getAddress());
//
//
//                GlideApp.with(holder.cardLayout.getContext())
//                        .load(firebaseStorage.getReferenceFromUrl(complaintList.get(position).getImageUrl()))
//                        .into(imageView);
//
//                dialog.show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public class MyViewHandle extends RecyclerView.ViewHolder{
        TextView title, description, address;
        ImageView image;
        ConstraintLayout cardLayout;
        TextView titleComplaint;
        public MyViewHandle(View itemView){
            super(itemView);
            //cardLayout = itemView.findViewById(R.id.card_model_layout);
            title  = itemView.findViewById(R.id.dialog_title_content);
            description = itemView.findViewById(R.id.dialog_description_content);
            address = itemView.findViewById(R.id.dialog_address_content);
            image = itemView.findViewById(R.id.dialog_image_view);
            //titleComplaint = itemView.findViewById(R.id.complaint_model_title_text_view);
        }
    }


}


