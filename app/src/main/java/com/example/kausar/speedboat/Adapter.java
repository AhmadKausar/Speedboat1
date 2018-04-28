package com.example.kausar.speedboat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by henyheny on 23/04/18.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<ModelItem> barang;
    private Context context;

    public Adapter(Context context, ArrayList<ModelItem> Barang) {
        this.barang = Barang;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelItem modelItem = barang.get(position);
        holder.NamaSB.setText(modelItem.getSpeedboat());
        holder.HargaSB.setText(modelItem.getHarga());
        holder.HariSB.setText(modelItem.getHari());
        holder.JamSB.setText(modelItem.getJam());
    }

    @Override
    public int getItemCount() {
        return barang.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView NamaSB, HargaSB, JamSB, HariSB;

        public ViewHolder(final View itemView) {
            super(itemView);
            NamaSB = itemView.findViewById(R.id.NamaSB);
            HargaSB = itemView.findViewById(R.id.HargaSB);
            JamSB = itemView.findViewById(R.id.JamSB);
            HariSB = itemView.findViewById(R.id.HariSB);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = getAdapterPosition();
                    Intent intent = new Intent(context, Detail.class);
                    intent.putExtra("nama", barang.get(i).getSpeedboat());
                    intent.putExtra("harga", barang.get(i).getHarga());
                    intent.putExtra("jam", barang.get(i).getJam());
                    intent.putExtra("hari",barang.get(i).getHari());
                    intent.putExtra("no_hp",barang.get(i).getNo_HP());
                    context.startActivity(intent);
                }
            });
        }
    }
}
