package com.example.freakdeveloper.kep.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RankingAdapterRecyclerView{

    public class RankingViewHolder extends RecyclerView.ViewHolder{

        //AQUI SE DEFINEN TODOS LOS VIEWS DEL CARDVIEW
        private TextView tvNoOrden, tvNickName, tvPorcentaje;



        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);

            //tvNoOrden = (TextView) itemView.findViewById(R.id.tvNoOrden);
            //tvNickName = (TextView) itemView.findViewById(R.id.tvNickName);
            //tvPorcentaje =  (TextView) itemView.findViewById(R.id.tvPorcentaje);

        }




    }

}
