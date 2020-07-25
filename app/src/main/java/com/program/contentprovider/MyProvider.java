package com.program.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class MyProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.program.contentprovider.MyProvider";
    static final String URL = "content://"+PROVIDER_NAME+"/rehber";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private Veritabani veritabani;
    private SQLiteDatabase database;

    static final String TABLE_NAME = "rehber";
    static final String ID = "id";
    static final String NAME = "ad";
    static final String TEL = "tel";

    private static HashMap<String,String> RehberMap;
    static final int REHBER = 1;
    static final int REHBER_PARAMETRE = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"rehber",REHBER);
        uriMatcher.addURI(PROVIDER_NAME,"rehber/*",REHBER_PARAMETRE);
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        veritabani = new Veritabani(context);
        database = veritabani.getWritableDatabase();
        if(database == null){
            return false;
        }else{
            return true;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables("TABLE_NAME");

        switch (uriMatcher.match(uri)){
            case REHBER:
                queryBuilder.setProjectionMap(RehberMap);
                break;
            case REHBER_PARAMETRE:
                queryBuilder.appendWhere(ID+"="+uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException(("bilinmeyen URI" + uri));
        }

        Cursor cursor = queryBuilder.query(database,strings,s,strings1,null,null,ID);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long row = database.insert(TABLE_NAME,"",contentValues);

        if(row > 0){
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI,row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Yeni Kayıt Oluşturma Hatası " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        int count = 0;

        switch (uriMatcher.match(uri)){
            case REHBER:
                count = database.delete(TABLE_NAME,s,strings);
                break;
            case REHBER_PARAMETRE:
                count = database.delete(TABLE_NAME,ID+"="+uri.getLastPathSegment(),strings);
                break;
            default:
                throw new IllegalArgumentException(("bilinmeyen URI" + uri));
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        int count = 0;

        switch (uriMatcher.match(uri)){
            case REHBER:
                count = database.update(TABLE_NAME,contentValues,s,strings);
                break;
            case REHBER_PARAMETRE:
                count = database.update(TABLE_NAME,contentValues,ID+"="+uri.getLastPathSegment(),strings);
                break;
            default:
                throw new IllegalArgumentException(("bilinmeyen URI" + uri));
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
