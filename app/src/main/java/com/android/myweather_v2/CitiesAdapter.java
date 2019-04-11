package com.android.myweather_v2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private List<CityItem> cities;
    private OnItemClickListener onItemClickListener;

    //Конструктор
    public CitiesAdapter(List<CityItem> cities) {
        this.cities = cities;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView coatOfArmsImageView;
        final TextView cityNameTextView;

        //Конструктор ViewHolder
        public ViewHolder (View v){
            super(v);
            coatOfArmsImageView = (ImageView)v.findViewById(R.id.coat_of_arms);
            cityNameTextView = (TextView)v.findViewById(R.id.city_name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getAdapterPosition());
                    }

                }
            });


        }

    }

    //Интерфейс для обработки нажатий
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    //Сеттер
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public CitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityItem city = cities.get(position);
        holder.cityNameTextView.setText(city.getCityName());
        holder.coatOfArmsImageView.setImageResource(city.getCoatOfArms());

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
