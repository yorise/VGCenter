package com.example.vgcenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.CartAdapter;
import adapter.UserAdapter;
import database.helper.GameDatabaseHelper;
import entity.Game;

public class UserPageActivity extends AppCompatActivity {
    private RecyclerView recyclerViewGames;
    private UserAdapter userAdapter;
    private GameDatabaseHelper dbHelper;
    private List<Game> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        recyclerViewGames = findViewById(R.id.recyclerViewGames);
        findViewById(R.id.iconCart).setOnClickListener(v -> showCartDialog());

        dbHelper = new GameDatabaseHelper(this);
        gameList = new ArrayList<>();
        userAdapter = new UserAdapter(gameList, this::onGameItemClicked);

        recyclerViewGames.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGames.setAdapter(userAdapter);

        loadGamesFromDatabase();
    }

    private void loadGamesFromDatabase() {
        gameList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_GAMES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(GameDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(GameDatabaseHelper.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndex(GameDatabaseHelper.COLUMN_DESCRIPTION));
                String publisher = cursor.getString(cursor.getColumnIndex(GameDatabaseHelper.COLUMN_PUBLISHER));
                double price = cursor.getDouble(cursor.getColumnIndex(GameDatabaseHelper.COLUMN_PRICE));

                gameList.add(new Game(id, name, description, publisher, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        userAdapter.notifyDataSetChanged();
    }

    private void onGameItemClicked(Game game) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_game_details, null);
        builder.setView(dialogView);

        TextView tvGameName = dialogView.findViewById(R.id.tvGameName);
        TextView tvGameDescription = dialogView.findViewById(R.id.tvGameDescription);
        Button btnAddToCart = dialogView.findViewById(R.id.btnAddToCart);
        Button btnCheckoutNow = dialogView.findViewById(R.id.btnCheckoutNow);

        tvGameName.setText(game.getName());
        tvGameDescription.setText(game.getDescription());

        btnAddToCart.setOnClickListener(v -> addToCart(game));
        btnCheckoutNow.setOnClickListener(v -> checkoutGame(game));

        builder.create().show();
    }

    private void addToCart(Game game) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                GameDatabaseHelper.TABLE_CART,
                null,
                GameDatabaseHelper.COLUMN_CART_GAME_ID + "=?",
                new String[]{String.valueOf(game.getId())},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Jika game sudah ada
            Toast.makeText(this, "Game already in cart!", Toast.LENGTH_SHORT).show();
        } else {
            // Jika game belum ada, tambahkan ke keranjang
            db.execSQL("INSERT INTO cart (game_id) VALUES (?)", new Object[]{game.getId()});
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void showCartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cart, null);
        builder.setView(dialogView);

        RecyclerView recyclerViewCart = dialogView.findViewById(R.id.recyclerViewCart);
        Button btnCheckoutCart = dialogView.findViewById(R.id.btnCheckoutCart);

        List<Game> cartItems = new ArrayList<>();

        // Gunakan array satu elemen untuk menyimpan referensi adapter
        final CartAdapter[] cartAdapter = new CartAdapter[1];

        cartAdapter[0] = new CartAdapter(cartItems, (game) -> removeFromCart(game, cartItems, cartAdapter[0]));

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter[0]);

        loadCartItems(cartItems, cartAdapter[0]);

        btnCheckoutCart.setOnClickListener(v -> checkoutCartItems(cartItems));

        builder.create().show();
    }



    private void loadCartItems(List<Game> cartItems, CartAdapter cartAdapter) {
        cartItems.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Ambil semua game_id dari tabel cart
        Cursor cartCursor = db.query(GameDatabaseHelper.TABLE_CART, null, null, null, null, null, null);

        if (cartCursor.moveToFirst()) {
            do {
                int gameId = cartCursor.getInt(cartCursor.getColumnIndex(GameDatabaseHelper.COLUMN_CART_GAME_ID));

                // Ambil detail game dari tabel games berdasarkan game_id
                Cursor gameCursor = db.query(
                        GameDatabaseHelper.TABLE_GAMES,
                        null,
                        GameDatabaseHelper.COLUMN_ID + "=?",
                        new String[]{String.valueOf(gameId)},
                        null,
                        null,
                        null
                );

                if (gameCursor.moveToFirst()) {
                    int id = gameCursor.getInt(gameCursor.getColumnIndex(GameDatabaseHelper.COLUMN_ID));
                    String name = gameCursor.getString(gameCursor.getColumnIndex(GameDatabaseHelper.COLUMN_NAME));
                    String description = gameCursor.getString(gameCursor.getColumnIndex(GameDatabaseHelper.COLUMN_DESCRIPTION));
                    String publisher = gameCursor.getString(gameCursor.getColumnIndex(GameDatabaseHelper.COLUMN_PUBLISHER));
                    double price = gameCursor.getDouble(gameCursor.getColumnIndex(GameDatabaseHelper.COLUMN_PRICE));

                    // Tambahkan ke daftar keranjang
                    cartItems.add(new Game(id, name, description, publisher, price));
                }
                gameCursor.close();
            } while (cartCursor.moveToNext());
        }
        cartCursor.close();

        cartAdapter.notifyDataSetChanged(); // Perbarui adapter
    }

    private void removeFromCart (Game game, List < Game > cartItems, CartAdapter cartAdapter){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Hapus dari database
        int rowsAffected = db.delete("cart", "game_id=?", new String[]{String.valueOf(game.getId())});

        if (rowsAffected > 0) {
            // Hapus dari dataset
            cartItems.remove(game);

            // Beritahu adapter untuk memperbarui tampilan
            cartAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to remove item from cart", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkoutCartItems(List<Game> cartItems) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction(); // Mulai transaksi database
        try {
            for (Game game : cartItems) {
                // Masukkan data game dari cart ke tabel orders
                db.execSQL(
                        "INSERT INTO orders (game_id) VALUES (?)",
                        new Object[]{game.getId()}
                );
            }

            // Hapus semua item dari tabel cart
            db.delete(GameDatabaseHelper.TABLE_CART, null, null);

            db.setTransactionSuccessful(); // Tandai transaksi sebagai berhasil
            Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Checkout failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction(); // Akhiri transaksi
        }

        // Kosongkan list cartItems untuk merefleksikan data yang sudah dihapus
        cartItems.clear();
    }

    private void checkoutGame(Game game) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction(); // Mulai transaksi database
        try {
            // Masukkan game ke tabel orders
            ContentValues values = new ContentValues();
            values.put(GameDatabaseHelper.COLUMN_ORDER_GAME_ID, game.getId());
            db.insertOrThrow(GameDatabaseHelper.TABLE_ORDERS, null, values);

            // Hapus game dari keranjang
            db.delete(
                    GameDatabaseHelper.TABLE_CART,
                    GameDatabaseHelper.COLUMN_CART_GAME_ID + "=?",
                    new String[]{String.valueOf(game.getId())}
            );

            db.setTransactionSuccessful(); // Tandai transaksi berhasil
            Toast.makeText(this, "Checkout successful for " + game.getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to checkout: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.endTransaction(); // Akhiri transaksi
            db.close(); // Tutup koneksi database
        }
    }

}
