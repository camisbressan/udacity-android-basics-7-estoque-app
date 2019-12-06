package com.example.estoque.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contrato para Estoque App
 */
public class EstoqueContrato {

    // Caso alguém instancie por acidente, foi criado
    // um construtor vazio
    private EstoqueContrato() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.estoque";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     */
    public static final String PATH_ESTOQUE = "estoque";

    /**
     * Inner class que define os valores da base.
     */
    public static final class EstoqueEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ESTOQUE);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ESTOQUE;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ESTOQUE;

        /**
         * Nome da tabela de estoque
         */
        public final static String NOME_TABELA = "estoque";

        /**
         * ID unico
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Nome do produto
         *
         * Type: TEXT
         */
        public final static String COLUNA_PRODUTO = "produto";

        /**
         * Preco do produto
         *
         * Type: INTEGER
         */
        public final static String COLUNA_PRECO = "preco";

        /**
         * Quantidade
         *
         * Type: INTEGER
         */
        public final static String COLUNA_QUANTIDADE = "quantidade";

        /**
         * Nome do Fornecedor
         *
         * Type: TEXT
         */
        public final static String COLUNA_NOME_FORNECEDOR = "fornecedor";

        /**
         * DDD Telefone do Fornecedor
         *
         * Type: INTEGER
         */
        public final static String COLUNA_DDD_TELEFONE_FORNECEDOR = "ddd";

        /**
         * Telefone do Fornecedor
         *
         * Type: INTEGER
         */
        public final static String COLUNA_TELEFONE_FORNECEDOR = "telefone";
    }

}
