package com.example.onlinecms;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinecms.model.Complaint;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ComplaintAdapter extends FirebaseRecyclerAdapter<Complaint, ComplaintAdapter.MyViewHolder> {

    Dialog dialog;

    public ComplaintAdapter(@NonNull FirebaseRecyclerOptions<Complaint> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Complaint complaint) {
        holder.title.setText(complaint.getTitle());

        dialog = new Dialog(holder.cardLayout.getContext());
        dialog.setContentView(R.layout.dialog_complaint);

        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title_content);
                TextView dialogDescription = dialog.findViewById(R.id.dialog_description_content);
                TextView dialogAddress = dialog.findViewById(R.id.dialog_address_content);

                dialogTitle.setText(complaint.getTitle());
                dialogDescription.setText(complaint.getDescription());
                dialogAddress.setText(complaint.getAddress());

                dialog.show();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_card_model, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout cardLayout;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardLayout = itemView.findViewById(R.id.card_model_layout);
            title = itemView.findViewById(R.id.complaint_model_title_text_view);
        }
    }
}
