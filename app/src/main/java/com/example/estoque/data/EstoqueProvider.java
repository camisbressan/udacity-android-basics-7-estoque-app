package com.example.estoque.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * {@link ContentProvider} para Estoque app.
 */
public class EstoqueProvider extends ContentProvider {

    /** Mensagem de Log */
    public static final String LOG_TAG = EstoqueProvider.class.getSimpleName();

    /** URI para tabela de estoque */
    private static final int ESTOQUE = 100;

    /** URI para um produto na tabela de estoque */
    private static final int ESTOQUE_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(EstoqueContrato.CONTENT_AUTHORITY, EstoqueContrato.PATH_ESTOQUE, ESTOQUE);
        sUriMatcher.addURI(EstoqueContrato.CONTENT_AUTHORITY, EstoqueContrato.PATH_ESTOQUE + "/#", ESTOQUE_ID);
    }

    /** Database helper objeto */
    private EstoqueDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new EstoqueDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ESTOQUE:

                cursor = database.query(EstoqueContrato.EstoqueEntry.NOME_TABELA, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ESTOQUE_ID:

                selection = EstoqueContrato.EstoqueEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(EstoqueContrato.EstoqueEntry.NOME_TABELA, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ESTOQUE:
                return insertProduto(uri, contentValues);
            default:
                throw new IllegalArgumentException("Inserção não suportada para o produto " + uri);
        }
    }

    /**
     * Insere o produto e retorna o URI
     */
    private Uri insertProduto(Uri uri, ContentValues values) {

        String nome = values.getAsString(EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO);
        if (nome == null) {
            throw new IllegalArgumentException("Produto precisa de um descritivo.");
        }

        Integer preco = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_PRECO);
        if (preco == null) {
            throw new IllegalArgumentException("Produto precisa de um preço.");
        }

        Integer quantidade = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE);
        if (quantidade != null && quantidade < 0) {
            throw new IllegalArgumentException("Produto requires valid weight");
        }

        String fornecedor = values.getAsString(EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Produto precisa de um fornecedor.");
        }

        Integer ddd = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR);
        if (ddd != null && ddd < 0) {
            throw new IllegalArgumentException("Produto precisa de um DDD do telefone de fornecedor.");
        }

        Integer telefone = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR);
        if (telefone != null && telefone < 0) {
            throw new IllegalArgumentException("Produto precisa de um telefone de fornecedor.");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(EstoqueContrato.EstoqueEntry.NOME_TABELA, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Falha a inserir a linha  " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ESTOQUE:
                return updateProduto(uri, contentValues, selection, selectionArgs);
            case ESTOQUE_ID:
                selection = EstoqueContrato.EstoqueEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduto(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Atualização não suportada para o produto " + uri);
        }
    }


    private int updateProduto(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO)) {
            String nome = values.getAsString(EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO);
            if (nome == null) {
                throw new IllegalArgumentException("Produto precisa de um descritivo.");
            }
        }

        if (values.containsKey(EstoqueContrato.EstoqueEntry.COLUNA_PRECO)) {
            Integer preco = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_PRECO);
            if (preco == null) {
                throw new IllegalArgumentException("Produto precisa de um preço.");
            }
        }

        if (values.containsKey(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE)) {
            Integer quantidade = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE);
            if (quantidade == null) {
                throw new IllegalArgumentException("Produto precisa de uma quantidade.");
            }
        }

        if (values.containsKey(EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR)) {
            String fornecedor = values.getAsString(EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR);
            if (fornecedor == null) {
                throw new IllegalArgumentException("Produto precisa de um fornecedor.");
            }
        }

        if (values.containsKey(EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR)) {
            Integer ddd = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR);
            if (ddd == null) {
                throw new IllegalArgumentException("Produto precisa de um DDD do telefone de fornecedor.");
            }
        }

        if (values.containsKey(EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR)) {
            Integer telefone = values.getAsInteger(EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR);
            if (telefone == null) {
                throw new IllegalArgumentException("Produto precisa de um telefone de fornecedor.");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(EstoqueContrato.EstoqueEntry.NOME_TABELA, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ESTOQUE:
                rowsDeleted = database.delete(EstoqueContrato.EstoqueEntry.NOME_TABELA, selection, selectionArgs);
                break;
            case ESTOQUE_ID:
                selection = EstoqueContrato.EstoqueEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(EstoqueContrato.EstoqueEntry.NOME_TABELA, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deleção não suportada para o produto " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ESTOQUE:
                return EstoqueContrato.EstoqueEntry.CONTENT_LIST_TYPE;
            case ESTOQUE_ID:
                return EstoqueContrato.EstoqueEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("URI desconhecida" + uri + " com " + match);
        }
    }
}
