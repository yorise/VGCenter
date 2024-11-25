package com.example.vgcenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final List<Game> gameList;
    private final OnGameClickListener editClickListener;
    private final OnGameClickListener deleteClickListener;

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    public GameAdapter(List<Game> gameList, OnGameClickListener editClickListener, OnGameClickListener deleteClickListener) {
        this.gameList = gameList;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.tvName.setText(game.getName());
        holder.tvDescription.setText(game.getDescription());
        holder.tvPublisher.setText(game.getPublisher());
        holder.tvPrice.setText(String.format("$%.2f", game.getPrice()));

        holder.btnEdit.setOnClickListener(v -> editClickListener.onGameClick(game));
        holder.btnDelete.setOnClickListener(v -> deleteClickListener.onGameClick(game));
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvPublisher, tvPrice;
        ImageButton btnEdit, btnDelete;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvGameName);
            tvDescription = itemView.findViewById(R.id.tvGameDescription);
            tvPublisher = itemView.findViewById(R.id.tvGamePublisher);
            tvPrice = itemView.findViewById(R.id.tvGamePrice);
            btnEdit = itemView.findViewById(R.id.btnEditGame);
            btnDelete = itemView.findViewById(R.id.btnDeleteGame);
        }
    }
}
