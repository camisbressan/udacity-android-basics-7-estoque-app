package com.example.estoque;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.estoque.data.EstoqueContrato;

/**
 * {@link EstoqueCursorAdapter} adapter para a list view
 */
public class EstoqueCursorAdapter extends CursorAdapter {

    private View.OnClickListener mButtonListener;

    public EstoqueCursorAdapter(Context context, Cursor c, View.OnClickListener buttonListener) {
        super(context, c, 0);
        this.mButtonListener = buttonListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View cardView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        EstoqueViewHolder iceCreamViewHolder = new EstoqueViewHolder(cardView);
        cardView.setTag(iceCreamViewHolder);
        return cardView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        EstoqueViewHolder estoqueViewHolder = (EstoqueViewHolder) view.getTag();

        int nomeColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_PRODUTO);
        int precoColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_PRECO);
        int quantidadeColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry.COLUNA_QUANTIDADE);

        TextView nomeTextView = view.findViewById(R.id.nome);
        TextView precoTextView = view.findViewById(R.id.preco);
        TextView quantidadeTextView = view.findViewById(R.id.quantidade);

        nomeTextView.setText(cursor.getString(nomeColumnIndex));
        precoTextView.setText(cursor.getString(precoColumnIndex));
        quantidadeTextView.setText(cursor.getString(quantidadeColumnIndex));

        ImageView image = view.findViewById(R.id.image_sale);
        image.setImageResource(R.drawable.ic_sale);

        int idColumnIndex = cursor.getColumnIndex(EstoqueContrato.EstoqueEntry._ID);
        Integer id = cursor.getInt(idColumnIndex);
        int quantity = cursor.getInt(quantidadeColumnIndex);

        VendaObjeto sale = new VendaObjeto(id, quantity);
        estoqueViewHolder.sale.setTag(sale);
        estoqueViewHolder.sale.setOnClickListener(mButtonListener);

    }

    /**
     *     Classe View Holder
     */
    static final class EstoqueViewHolder {

        TextView nome;
        TextView preco;
        TextView quantidade;
        ImageView sale;

        EstoqueViewHolder(View view) {
            nome = view.findViewById(R.id.nome);
            preco = view.findViewById(R.id.preco);
            quantidade = view.findViewById(R.id.quantidade);
            sale = view.findViewById(R.id.image_sale);
        }
    }

    /**
     *     Classe Venda
     */
    public static class VendaObjeto {

        private int mId;
        private int mQuantidade;

        public VendaObjeto(int mId, int mQuantidade) {
            this.mId = mId;
            this.mQuantidade = mQuantidade;
        }

        public int getId() {
            return mId;
        }

        public int getQuantidade() {
            return mQuantidade;
        }
    }
}