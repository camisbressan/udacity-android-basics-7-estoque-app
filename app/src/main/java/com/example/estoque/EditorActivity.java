package com.example.estoque;

import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.estoque.data.EstoqueContrato.EstoqueEntry;


public class EditorActivity extends AppCompatActivity implements
        LoaderCallbacks<Cursor> {

    private static final int EXISTING_ESTOQUE_LOADER = 0;

    private Uri mCurrentProdutoUri;

    /** EditText do nome do produto*/
    private EditText mNomeProdutoEditText;

    /** EditText do preco do produto */
    private EditText mPrecoEditText;

    /** EditText quantidade do produto */
    private EditText mQuantidadeEditText;

    /** EditText nome do fornecedor */
    private EditText mNomeFornecedorEditText;

    /** EditText DDD telefone do fornecedor */
    private EditText mDDDTelefoneFornecedorEditText;

    /** EditText numero telefone do fornecedor */
    private EditText mTelefoneFornecedorEditText;

    private boolean mProdutoHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProdutoHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentProdutoUri = intent.getData();

        if (mCurrentProdutoUri == null) {
            setTitle(getString(R.string.editor_activity_title_new));

            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit));

            getLoaderManager().initLoader(EXISTING_ESTOQUE_LOADER, null, this);
        }

        mNomeProdutoEditText = findViewById(R.id.edit_produto);
        mPrecoEditText = findViewById(R.id.edit_preco);
        mQuantidadeEditText = findViewById(R.id.edit_quantidade);
        mNomeFornecedorEditText = findViewById(R.id.edit_fornecedor);
        mDDDTelefoneFornecedorEditText = findViewById(R.id.edit_ddd_telefone);
        mTelefoneFornecedorEditText = findViewById(R.id.edit_numero_telefone);


        mNomeProdutoEditText.setOnTouchListener(mTouchListener);
        mPrecoEditText.setOnTouchListener(mTouchListener);
        mQuantidadeEditText.setOnTouchListener(mTouchListener);
        mNomeFornecedorEditText.setOnTouchListener(mTouchListener);
        mDDDTelefoneFornecedorEditText.setOnTouchListener(mTouchListener);
        mTelefoneFornecedorEditText.setOnTouchListener(mTouchListener);

    }



    private int saveProduto() {

        int retorno = 1;

        // Recebendo os valores
        String nomeString = mNomeProdutoEditText.getText().toString().trim();
        String precoString = mPrecoEditText.getText().toString().trim();
        String quantidadeString = mQuantidadeEditText.getText().toString().trim();
        String nomeFornecedorString = mNomeFornecedorEditText.getText().toString().trim();
        String dddTelefoneFornecedorString = mDDDTelefoneFornecedorEditText.getText().toString().trim();
        String telefoneFornecedorString = mTelefoneFornecedorEditText.getText().toString().trim();

        // Verifica se todos os campos estao preenchidos
        if (TextUtils.isEmpty(nomeString)) {
            Toast toast = Toast.makeText(this, getString(R.string.descricao_erro),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        else if (TextUtils.isEmpty(precoString)) {
            Toast toast = Toast.makeText(this, getString(R.string.preco_erro),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        else if (TextUtils.isEmpty(quantidadeString)) {
            Toast toast = Toast.makeText(this, getString(R.string.quantidade_erro),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        else if (TextUtils.isEmpty(nomeFornecedorString)) {
            Toast toast = Toast.makeText(this, getString(R.string.fornecedor_erro),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        else if (TextUtils.isEmpty(dddTelefoneFornecedorString)) {
            Toast toast = Toast.makeText(this, getString(R.string.ddd_erro),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
        else if (TextUtils.isEmpty(telefoneFornecedorString)) {
            Toast toast = Toast.makeText(this, getString(R.string.telefone_erro),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else {
            // Cria um objeto ContentValues com os valores
            ContentValues values = new ContentValues();
            values.put(EstoqueEntry.COLUNA_PRODUTO, nomeString);
            values.put(EstoqueEntry.COLUNA_PRECO, precoString);
            values.put(EstoqueEntry.COLUNA_QUANTIDADE, quantidadeString);
            values.put(EstoqueEntry.COLUNA_NOME_FORNECEDOR, nomeFornecedorString);
            values.put(EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR, dddTelefoneFornecedorString);
            values.put(EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR, telefoneFornecedorString);


            if (mCurrentProdutoUri == null) {
                Uri newUri = getContentResolver().insert(EstoqueEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, getString(R.string.editor_insert_failed),
                            Toast.LENGTH_SHORT).show();
                    return retorno;
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_successful),
                            Toast.LENGTH_SHORT).show();
                    retorno = 2;
                    return retorno;
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentProdutoUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, getString(R.string.editor_update_failed),
                            Toast.LENGTH_SHORT).show();
                    return retorno;
                } else {
                    Toast.makeText(this, getString(R.string.editor_update_successful),
                            Toast.LENGTH_SHORT).show();
                    return retorno;
                }
            }
        }
        return retorno;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acao_salvar:
                int retorno = saveProduto();
                if(retorno == 1){
                    return false;
                }else{
                    finish();
                    return true;
                }
            case android.R.id.home:
                if (!mProdutoHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        if (!mProdutoHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                EstoqueEntry._ID,
                EstoqueEntry.COLUNA_PRODUTO,
                EstoqueEntry.COLUNA_PRECO,
                EstoqueEntry.COLUNA_QUANTIDADE,
                EstoqueEntry.COLUNA_NOME_FORNECEDOR,
                EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR,
                EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR };

        return new CursorLoader(this,   // Parent activity context
                mCurrentProdutoUri,         // Query the content URI for the current
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nomeColumnIndex = cursor.getColumnIndex(EstoqueEntry.COLUNA_PRODUTO);
            int precoColumnIndex = cursor.getColumnIndex(EstoqueEntry.COLUNA_PRECO);
            int quantidadeColumnIndex = cursor.getColumnIndex(EstoqueEntry.COLUNA_QUANTIDADE);
            int fornecedorColumnIndex = cursor.getColumnIndex(EstoqueEntry.COLUNA_NOME_FORNECEDOR);
            int dddTelefoneColumnIndex = cursor.getColumnIndex(EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR);
            int telefoneColumnIndex = cursor.getColumnIndex(EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR);

            String nome = cursor.getString(nomeColumnIndex);
            int preco = cursor.getInt(precoColumnIndex);
            int quantidade = cursor.getInt(quantidadeColumnIndex);
            String fornecedor = cursor.getString(fornecedorColumnIndex);
            int ddd = cursor.getInt(dddTelefoneColumnIndex);
            int telefone = cursor.getInt(telefoneColumnIndex);

            mNomeProdutoEditText.setText(nome);
            mPrecoEditText.setText(Integer.toString(preco));
            mQuantidadeEditText.setText(Integer.toString(quantidade));
            mNomeFornecedorEditText.setText(fornecedor);
            mDDDTelefoneFornecedorEditText.setText(Integer.toString(ddd));
            mTelefoneFornecedorEditText.setText(Integer.toString(telefone));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNomeProdutoEditText.setText("");
        mPrecoEditText.setText("");
        mQuantidadeEditText.setText("");
        mNomeFornecedorEditText.setText("");
        mDDDTelefoneFornecedorEditText.setText("");
        mTelefoneFornecedorEditText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.acao_descartar, discardButtonClickListener);
        builder.setNegativeButton(R.string.acao_continuar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}