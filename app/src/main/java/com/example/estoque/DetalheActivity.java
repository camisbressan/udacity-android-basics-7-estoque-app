package com.example.estoque;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estoque.data.EstoqueContrato;

public class DetalheActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ESTOQUE_LOADER = 0;

    private Uri mCurrentProdutoUri;

    private int mCurrentProdutoId;

    /** TextView do nome do produto*/
    private TextView mNomeProdutoTextView;

    /** TextView do preco do produto */
    private TextView mPrecoTextView;

    /** TextView quantidade do produto */
    private TextView mQuantidadeTextView;

    /** TextView nome do fornecedor */
    private TextView mNomeFornecedorTextView;

    /** TextView DDD telefone do fornecedor */
    private TextView mDDDTelefoneFornecedorTextView;

    /** TextView numero telefone do fornecedor */
    private TextView mTelefoneFornecedorTextView;

    /** Diminui a quantidade de produtos */
    private Button mButtonMenos;

    /** Aumenta a quantidade de produtos */
    private Button mButtonMais;

    /** Botao para ligar para o fornecedor */
    private Button mButtonLigar;

    private boolean mProdutoHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);


        final Intent intent = getIntent();
        mCurrentProdutoUri = intent.getData();
        String mCurrentProdutoString = mCurrentProdutoUri.toString();
        String idString = mCurrentProdutoString.substring(mCurrentProdutoString.length() - 1);
        mCurrentProdutoId = Integer.parseInt(idString);

        getLoaderManager().initLoader(EXISTING_ESTOQUE_LOADER, null, this);

        mNomeProdutoTextView = findViewById(R.id.edit_produto);
        mPrecoTextView = findViewById(R.id.edit_preco);
        mQuantidadeTextView = findViewById(R.id.edit_quantidade);
        mNomeFornecedorTextView = findViewById(R.id.edit_fornecedor);
        mDDDTelefoneFornecedorTextView = findViewById(R.id.edit_ddd_telefone);
        mTelefoneFornecedorTextView = findViewById(R.id.edit_numero_telefone);

        mButtonMenos = findViewById(R.id.button_menos);

        mButtonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuantidadeTextView.getText().equals("0")) {
                    Toast.makeText(DetalheActivity.this, getString(R.string.quantidade_text),
                            Toast.LENGTH_SHORT).show();
                } else {
                    alterarQuantidade(mButtonMenos);
                }

            }
        });


        mButtonMais = findViewById(R.id.button_mais);
        mButtonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarQuantidade(mButtonMais);
            }
        });

        mButtonLigar = findViewById(R.id.button_fornecedor);
        mButtonLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = mDDDTelefoneFornecedorTextView.getText().toString() + mTelefoneFornecedorTextView.getText().toString();

                Uri uri = Uri.parse("tel:" + numero);

                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                //Verifica permissao para ligacao
                if (ActivityCompat.checkSelfPermission(DetalheActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetalheActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentProdutoUri == null) {
            MenuItem menuItem = menu.findItem(R.id.acao_deletar);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acao_editar:
                Intent intent = new Intent(DetalheActivity.this, EditorActivity.class);
                Uri currentProdutoUri = ContentUris.withAppendedId(EstoqueContrato.EstoqueEntry.CONTENT_URI, mCurrentProdutoId);
                intent.setData(currentProdutoUri);
                startActivity(intent);
                return true;
            case R.id.acao_deletar:
                showDeleteConfirmationDialog();
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
                EstoqueContrato.EstoqueEntry._ID,
                EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO,
                EstoqueContrato.EstoqueEntry.COLUNA_PRECO,
                EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE,
                EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR,
                EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR,
                EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR};

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
            int nomeColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO);
            int precoColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_PRECO);
            int quantidadeColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE);
            int fornecedorColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR);
            int dddTelefoneColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR);
            int telefoneColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR);

            String nome = cursor.getString(nomeColumnIndex);
            int preco = cursor.getInt(precoColumnIndex);
            int quantidade = cursor.getInt(quantidadeColumnIndex);
            String fornecedor = cursor.getString(fornecedorColumnIndex);
            int ddd = cursor.getInt(dddTelefoneColumnIndex);
            int telefone = cursor.getInt(telefoneColumnIndex);

            mNomeProdutoTextView.setText(nome);
            mPrecoTextView.setText(Integer.toString(preco));
            mQuantidadeTextView.setText(Integer.toString(quantidade));
            mNomeFornecedorTextView.setText(fornecedor);
            mDDDTelefoneFornecedorTextView.setText(Integer.toString(ddd));
            mTelefoneFornecedorTextView.setText(Integer.toString(telefone));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNomeProdutoTextView.setText("");
        mPrecoTextView.setText("");
        mQuantidadeTextView.setText("");
        mNomeFornecedorTextView.setText("");
        mDDDTelefoneFornecedorTextView.setText("");
        mTelefoneFornecedorTextView.setText("");
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


    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.acao_deletar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteProduto();
            }
        });
        builder.setNegativeButton(R.string.acao_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteProduto() {
        if (mCurrentProdutoUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProdutoUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }


    public void alterarQuantidade(Button mbutton) {
        // Recebendo os valores
        String nomeString = mNomeProdutoTextView.getText().toString().trim();
        String precoString = mPrecoTextView.getText().toString().trim();
        int preco = Integer.parseInt(precoString);
        String nomeFornecedorString = mNomeFornecedorTextView.getText().toString().trim();
        String dddTelefoneFornecedorString = mDDDTelefoneFornecedorTextView.getText().toString().trim();
        String telefoneFornecedorString = mTelefoneFornecedorTextView.getText().toString().trim();

        String quantidadeString = mQuantidadeTextView.getText().toString();
        int quantidade = Integer.parseInt(quantidadeString);

        if(mbutton == mButtonMais){
            quantidade += 1;
        }else{
            quantidade -= 1;
        }

        // Cria um objeto ContentValues com os valores
        ContentValues values = new ContentValues();
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO, nomeString);
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_PRECO, preco);
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE, quantidade);
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR, nomeFornecedorString);
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR, dddTelefoneFornecedorString);
        values.put(EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR, telefoneFornecedorString);

        int rowsAffected = getContentResolver().update(mCurrentProdutoUri, values, null, null);

        if (rowsAffected == 0) {
            Toast.makeText(this, getString(R.string.quantidade_atualizada_erro_text),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.quantidade_atualizada_text),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
