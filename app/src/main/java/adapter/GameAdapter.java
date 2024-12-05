package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import entity.Game;
import com.example.vgcenter.R;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> gameList;
    private boolean isAdminView;
    private OnGameEditClickListener editClickListener;
    private OnGameDeleteClickListener deleteClickListener;

    public GameAdapter(List<Game> gameList, boolean isAdminView, OnGameEditClickListener editClickListener, OnGameDeleteClickListener deleteClickListener) {
        this.gameList = gameList;
        this.isAdminView = isAdminView;
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
        holder.tvGameName.setText(game.getName());
        holder.tvGameDescription.setText(game.getDescription());
        holder.tvGamePublisher.setText(game.getPublisher());
        holder.tvGamePrice.setText(String.format("$%.2f", game.getPrice()));

        if (isAdminView) {
            holder.btnEditGame.setVisibility(View.VISIBLE);
            holder.btnDeleteGame.setVisibility(View.VISIBLE);

            holder.btnEditGame.setOnClickListener(v -> editClickListener.onEditClick(game));
            holder.btnDeleteGame.setOnClickListener(v -> deleteClickListener.onDeleteClick(game));
        } else {
            holder.btnEditGame.setVisibility(View.GONE);
            holder.btnDeleteGame.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView tvGameName, tvGameDescription, tvGamePublisher, tvGamePrice;
        ImageButton btnEditGame, btnDeleteGame;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvGameDescription = itemView.findViewById(R.id.tvGameDescription);
            tvGamePublisher = itemView.findViewById(R.id.tvGamePublisher);
            tvGamePrice = itemView.findViewById(R.id.tvGamePrice);
            btnEditGame = itemView.findViewById(R.id.btnEditGame);
            btnDeleteGame = itemView.findViewById(R.id.btnDeleteGame);
        }
    }

    public interface OnGameEditClickListener {
        void onEditClick(Game game);
    }

    public interface OnGameDeleteClickListener {
        void onDeleteClick(Game game);
    }
}
