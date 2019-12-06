package com.example.estoque.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Gestao da base de estoque
 */
public class EstoqueDbHelper extends SQLiteOpenHelper {

    /** Nome da base de dados */
    private static final String DATABASE_NAME = "estoque.db";

    /**
     * Versao da base de dados
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constroi uma nova instancia do {@link EstoqueDbHelper}.
     *
     * @param context do app
     */
    public EstoqueDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Chamado quando a base de dados é criada pela primeira vez
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CRIAR_TABELA_ESTOQUE= "CREATE TABLE " + EstoqueContrato.EstoqueEntry.NOME_TABELA + " ("
                + EstoqueContrato.EstoqueEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO + " TEXT NOT NULL, "
                + EstoqueContrato.EstoqueEntry.COLUNA_PRECO + " INTEGER NOT NULL, "
                + EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE + " INTEGER NOT NULL DEFAULT 0, "
                + EstoqueContrato.EstoqueEntry.COLUNA_NOME_FORNECEDOR + " TEXT NOT NULL, "
                + EstoqueContrato.EstoqueEntry.COLUNA_DDD_TELEFONE_FORNECEDOR + " INTEGER NOT NULL, "
                + EstoqueContrato.EstoqueEntry.COLUNA_TELEFONE_FORNECEDOR + " INTEGER NOT NULL DEFAULT 0);";

        // Executa o SQL
        db.execSQL(SQL_CRIAR_TABELA_ESTOQUE);
    }

    /**
     * Atualizacao de dados
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}