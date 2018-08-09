package com.akashapplications.shoutube.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akashapplications.shoutube.Player;
import com.akashapplications.shoutube.R;
import com.akashapplications.shoutube.model.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    Context context;
    ArrayList<VideoModel> videoList;

    public VideoAdapter(Context context, ArrayList<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_card_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(videoList.get(position).getTitle());
        holder.description.setText(videoList.get(position).getDescription());
        holder.dateCreated.setText(videoList.get(position).getPublishedAt());


        Picasso.get()
                .load(videoList.get(position).getThumbnail())
                .placeholder(R.drawable.logo_small_32)
                .error(R.drawable.logo_small_32)
                .into(holder.thumbnail);

        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.comment.setText("");
                Toast.makeText(context,"Your comment is posted",Toast.LENGTH_SHORT).show();
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!videoList.get(position).getVideoId().equals(""))
                    context.startActivity(new Intent(context, Player.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",videoList.get(position).getVideoId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title, description, dateCreated;
        EditText comment;
        ImageButton send;
        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.videoThumbnail);
            title = itemView.findViewById(R.id.videoTitle);
            description = itemView.findViewById(R.id.videoDesc);
            dateCreated = itemView.findViewById(R.id.videoDate);
            comment = itemView.findViewById(R.id.commentInput);
            send = itemView.findViewById(R.id.btnSendComment);
        }
    }
}
