package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import entity.Game;
import com.example.vgcenter.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Game> cartItems;
    private final OnRemoveClickListener onRemoveClickListener;

    public CartAdapter(List<Game> cartItems, OnRemoveClickListener onRemoveClickListener) {
        this.cartItems = cartItems;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Game game = cartItems.get(position);
        holder.tvName.setText(game.getName());
        holder.tvPrice.setText(String.format("Rp %.2f", game.getPrice()));

        holder.btnRemove.setOnClickListener(v -> onRemoveClickListener.onRemoveClick(game));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartItemName);
            tvPrice = itemView.findViewById(R.id.tvCartItemPrice);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(Game game);
    }
}

