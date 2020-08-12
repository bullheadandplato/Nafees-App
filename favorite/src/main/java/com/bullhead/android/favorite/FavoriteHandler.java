package com.bullhead.android.favorite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FavoriteHandler<T extends Serializable> extends SQLiteOpenHelper {
    private static final String QUERY =
            "CREATE TABLE IF NOT EXISTS " + FavoriteEntry.TABLE_NAME + " ( " +
                    FavoriteEntry.ID + " TEXT PRIMARY KEY," +
                    FavoriteEntry.COLUMN_JSON + " TEXT )";

    private Field favoriteField;

    public FavoriteHandler(Context context, Class<T> tClass) {
        super(context, "fav_db", null, 1);
        if (!annotationPresent(tClass)) {
            throw new IllegalArgumentException("Please annotate your id with @FavoriteId");
        }
    }

    public void insert(@NonNull T t) {
        SQLiteDatabase db            = getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put(FavoriteEntry.COLUMN_JSON, new Gson().toJson(t));
        contentValues.put(FavoriteEntry.ID, getId(t));
        db.insert(FavoriteEntry.TABLE_NAME, null, contentValues);
    }

    public void delete(@NonNull T t) {
        String   id          = getId(t);
        String   whereClause = FavoriteEntry.ID + " =?";
        String[] args        = new String[]{id};
        getWritableDatabase().delete(FavoriteEntry.TABLE_NAME, whereClause, args);
    }

    @NonNull
    public List<T> getAll(Class<T> cls) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(FavoriteEntry.TABLE_NAME, new String[]{FavoriteEntry.ID, FavoriteEntry.COLUMN_JSON},
                null, null, null, null, null);
        List<T> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            String json = cursor.getString(1);
            T      t    = new Gson().fromJson(json, cls);
            data.add(t);
        }
        cursor.close();
        return data;
    }

    public long count() {
        return DatabaseUtils.queryNumEntries(getWritableDatabase(), FavoriteEntry.TABLE_NAME);
    }

    public boolean isFavorite(T t) {
        String   id          = getId(t);
        String   whereClause = FavoriteEntry.ID + " =?";
        String[] args        = new String[]{id};
        Cursor cursor = getWritableDatabase().query(FavoriteEntry.TABLE_NAME,
                new String[]{FavoriteEntry.ID, FavoriteEntry.COLUMN_JSON}, whereClause, args, null, null, null);
        boolean exits = cursor.moveToFirst();
        cursor.close();
        return exits;
    }

    @Nullable
    private String getId(T t) {
        try {
            Field field = t.getClass().getDeclaredField(favoriteField.getName());
            field.setAccessible(true);
            return field.get(t).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean annotationPresent(Class<T> t) {
        Field[] fields = t.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FavoriteId.class)) {
                favoriteField = field;
                return true;
            }
        }
        return false;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static class FavoriteEntry {
        private static final String TABLE_NAME  = "favorites";
        private static final String COLUMN_JSON = "json";
        private static final String ID          = "id";
    }
}
