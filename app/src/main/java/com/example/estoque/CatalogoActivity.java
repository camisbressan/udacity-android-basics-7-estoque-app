package com.example.estoque;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.estoque.data.EstoqueContrato;

/**
 * Exibe a lista de produtos
 */
public class CatalogoActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the data loader
     */
    private static final int ESTOQUE_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    EstoqueCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        Button button = findViewById(R.id.button_new);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogoActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView produtoListView = findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        produtoListView.setEmptyView(emptyView);


        View.OnClickListener saleButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EstoqueCursorAdapter.VendaObjeto saleTagObject =
                        (EstoqueCursorAdapter.VendaObjeto) v.findViewById(R.id.image_sale).getTag();
                if (saleTagObject.getQuantidade() > 0) {
                    atualizarQuantidade(saleTagObject.getQuantidade() - 1, saleTagObject.getId());
                }
            }
        };

        mCursorAdapter = new EstoqueCursorAdapter(this, null, saleButtonListener);
        produtoListView.setAdapter(mCursorAdapter);
        produtoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogoActivity.this, DetalheActivity.class);
                Uri currentProdutoUri = ContentUris.withAppendedId(EstoqueContrato.EstoqueEntry.CONTENT_URI, id);
                intent.setData(currentProdutoUri);
                startActivity(intent);
            }
        });


        getLoaderManager().initLoader(ESTOQUE_LOADER, null, this);
    }


    private void insertProduto() {

        ContentValues values = new ContentValues();
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO, "Vaso");
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_PRECO, "10");
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE, 1);
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR,"Carrefour");
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR, "11");
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR, "988784344");

        Uri newUri = getContentResolver().insert(EstoqueContrato.EstoqueEntry.CONTENT_URI, values);
    }


    private void deleteAllProdutos() {
        int rowsDeleted = getContentResolver().delete(EstoqueContrato.EstoqueEntry.CONTENT_URI, null, null);
        Log.v("CatalogoActivity", rowsDeleted + " rows deleted from estoque database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalogo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acao_inserir_produto:
                insertProduto();
                return true;
            case R.id.acao_deletar_produto:
                deleteAllProdutos();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                EstoqueContrato.EstoqueEntry._ID,
                EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO,
                EstoqueContrato.EstoqueEntry.COLUNA_PRECO,
                EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE};

        return new CursorLoader(this,   // Parent activity context
                EstoqueContrato.EstoqueEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


    private void atualizarQuantidade(int newStockValue, int rowId) {
        ContentValues values = new ContentValues();
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE, newStockValue);
        String selection = EstoqueContrato.EstoqueEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(rowId)};
        Uri uri = ContentUris.withAppendedId(EstoqueContrato.EstoqueEntry.CONTENT_URI, rowId);
        getContentResolver().update(uri, values, selection, selectionArgs);
    }
}
