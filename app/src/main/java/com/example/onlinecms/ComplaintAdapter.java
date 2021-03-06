package com.example.onlinecms;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinecms.model.Complaint;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;

public class ComplaintAdapter extends FirebaseRecyclerAdapter<Complaint, ComplaintAdapter.MyViewHolder> {

    Dialog dialog;
    private FirebaseStorage firebaseStorage;
    //String[] some_array = getResources().getStringArray(R.array.status);

    public ComplaintAdapter(@NonNull FirebaseRecyclerOptions<Complaint> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Complaint complaint) {
        holder.title.setText(complaint.getTitle());
        holder.date.setText(complaint.getDateCreated());

        if (complaint.getStatus() == 1){
            holder.cardLayout.setBackgroundColor(Color.parseColor("#C0C0C0"));
            holder.complaintImage.setBackgroundResource(R.drawable.ic_complaint_reverse);
            holder.title.setTextColor(Color.parseColor("#FFF6AD"));
            holder.date.setTextColor(Color.parseColor("#FFF6AD"));
        }

        dialog = new Dialog(holder.cardLayout.getContext());
        dialog.setContentView(R.layout.dialog_complaint);

        firebaseStorage = FirebaseStorage.getInstance();

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title_content);
                TextView dialogDescription = dialog.findViewById(R.id.dialog_description_content);
                TextView dialogAddress = dialog.findViewById(R.id.dialog_address_content);
                ImageView imageView = dialog.findViewById(R.id.dialog_image_view);
                TextView dialogDate = dialog.findViewById(R.id.dialog_date_content);
                TextView dialogStatus = dialog.findViewById(R.id.dialog_status_text_content);

                dialogTitle.setText(complaint.getTitle());
                dialogDescription.setText(complaint.getDescription());
                dialogAddress.setText(complaint.getAddress());
                dialogDate.setText(complaint.getDateCreated());
                dialogStatus.setText(holder.cardLayout.getContext().getResources().getStringArray(R.array.status)[complaint.getStatus()]);
                imageView.setVisibility(View.GONE);

                if (!complaint.getImageUrl().equals("")){
                    GlideApp.with(holder.cardLayout.getContext())
                            .load(firebaseStorage.getReferenceFromUrl(complaint.getImageUrl()))
                            .into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                }

                dialog.show();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_card_model, parent, false);

        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cardLayout;
        TextView title;
        TextView date;
        ImageView complaintImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardLayout = itemView.findViewById(R.id.card_model_layout);
            title = itemView.findViewById(R.id.complaint_model_title_text_view);
            date = itemView.findViewById(R.id.complaint_model_date_text_view);
            complaintImage = itemView.findViewById(R.id.complaint_model_icon_image_view);
        }
    }
}