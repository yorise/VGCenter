package com.example.vgcenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.GameAdapter;
import database.helper.GameDatabaseHelper;
import entity.Game;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private GameDatabaseHelper gdbHelper;
    private List<Game> gameList;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        recyclerView = findViewById(R.id.recyclerViewGames);
        findViewById(R.id.btnAddGame).setOnClickListener(view -> showAddEditGameDialog(false, null));

        gdbHelper = new GameDatabaseHelper(this);
        gameList = new ArrayList<>();
        gameAdapter = new GameAdapter(gameList,true, this::onGameItemClicked, this::onGameDeleteClicked);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(gameAdapter);

        loadGamesFromDatabase();
    }

    private void loadGamesFromDatabase() {
        gameList.clear();
        SQLiteDatabase db = gdbHelper.getReadableDatabase();
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
        gameAdapter.notifyDataSetChanged();
    }

    private void showAddEditGameDialog(boolean isEdit, Game existingGame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.addgame_layout, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etGameName);
        EditText etDescription = dialogView.findViewById(R.id.etGameDescription);
        EditText etPublisher = dialogView.findViewById(R.id.etGamePublisher);
        EditText etPrice = dialogView.findViewById(R.id.etGamePrice);

        if (isEdit && existingGame != null) {
            etName.setText(existingGame.getName());
            etDescription.setText(existingGame.getDescription());
            etPublisher.setText(existingGame.getPublisher());
            etPrice.setText(String.valueOf(existingGame.getPrice()));
        }

        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnSaveGame).setOnClickListener(v -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String publisher = etPublisher.getText().toString();
            String priceStr = etPrice.getText().toString();

            if (name.isEmpty() || description.isEmpty() || publisher.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            SQLiteDatabase db = gdbHelper.getWritableDatabase();

            if (isEdit) {
                // Update game
                db.execSQL("UPDATE " + GameDatabaseHelper.TABLE_GAMES +
                        " SET name=?, description=?, publisher=?, price=?" +
                        " WHERE id=?", new Object[]{name, description, publisher, price, existingGame.getId()});
                Toast.makeText(this, "Game updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Add new game
                db.execSQL("INSERT INTO " + GameDatabaseHelper.TABLE_GAMES +
                                " (name, description, publisher, price) VALUES (?, ?, ?, ?)",
                        new Object[]{name, description, publisher, price});
                Toast.makeText(this, "Game added successfully!", Toast.LENGTH_SHORT).show();
            }
            loadGamesFromDatabase();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void onGameItemClicked(Game game) {
        showAddEditGameDialog(true, game);
    }
    private void onGameDeleteClicked(Game game) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Game")
                .setMessage("Are you sure you want to delete this game?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SQLiteDatabase db = gdbHelper.getWritableDatabase();
                    db.execSQL("DELETE FROM " + GameDatabaseHelper.TABLE_GAMES + " WHERE id=?", new Object[]{game.getId()});
                    Toast.makeText(this, "Game deleted successfully!", Toast.LENGTH_SHORT).show();
                    loadGamesFromDatabase();
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
}
