package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vgcenter.R;

import java.util.List;

import entity.Game;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<Game> gameList;
    private final OnGameClickListener onGameClickListener;

    public UserAdapter(List<Game> gameList, OnGameClickListener onGameClickListener) {
        this.gameList = gameList;
        this.onGameClickListener = onGameClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.tvName.setText(game.getName());
        holder.tvPrice.setText(String.format("$ %.2f", game.getPrice()));

        holder.itemView.setOnClickListener(v -> onGameClickListener.onGameClick(game));
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvGameName);
            tvPrice = itemView.findViewById(R.id.tvGamePrice);
        }
    }

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }
}
