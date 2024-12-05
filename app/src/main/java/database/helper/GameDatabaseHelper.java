package database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "games.db";
    private static final int DATABASE_VERSION = 2; // Tingkatkan versi database untuk menambahkan tabel baru

    // Tabel games
    public static final String TABLE_GAMES = "games";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PUBLISHER = "publisher";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URI = "image_uri"; // Tambahkan kolom untuk gambar

    // Tabel cart
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "cart_id";
    public static final String COLUMN_CART_GAME_ID = "game_id";

    // Tabel orders
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_GAME_ID = "game_id";
    public static final String COLUMN_ORDER_QUANTITY = "quantity";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "total_price";

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabel games
        String createGamesTable = "CREATE TABLE " + TABLE_GAMES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PUBLISHER + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE_URI + " TEXT)";
        db.execSQL(createGamesTable);

        // Tabel cart
        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CART_GAME_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CART_GAME_ID + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_ID + "))";
        db.execSQL(createCartTable);

        // Tabel orders
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORDER_GAME_ID + " INTEGER, " +
                COLUMN_ORDER_QUANTITY + " INTEGER, " +
                COLUMN_ORDER_TOTAL_PRICE + " REAL, " +
                "FOREIGN KEY(" + COLUMN_ORDER_GAME_ID + ") REFERENCES " + TABLE_GAMES + "(" + COLUMN_ID + "))";
        db.execSQL(createOrdersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }
}

