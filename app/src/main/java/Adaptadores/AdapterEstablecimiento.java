package Adaptadores;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manuel.foodu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Mapa.Establecimiento;

public class AdapterEstablecimiento extends RecyclerView.Adapter<AdapterEstablecimiento.ViewHolder> implements View.OnClickListener {
    LayoutInflater inflater;
    ArrayList<Establecimiento> model;
    ArrayList<Establecimiento> originalItems;
    View.OnClickListener listener;

    public AdapterEstablecimiento(Context context, ArrayList<Establecimiento> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_establecimientos, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int imagen = model.get(position).getImagen();
        String titulo = model.get(position).getNombre();
        String direccion = model.get(position).getDireccion();
        holder.imageViewEstablecimiento.setImageResource(imagen);
        holder.textViewTitulo.setText(titulo);
        holder.textViewDireccion.setText(direccion);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void filter(final String s) {
        if (s.length() == 0) {
            model.clear();
            model.addAll(originalItems);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                model.clear();
                List<Establecimiento> collect = originalItems.stream().filter(i -> i.getNombre().toLowerCase().contains(s)).collect(Collectors.toList());
                model.addAll(collect);
            } else {
                model.clear();
                for (Establecimiento establecimiento : originalItems) {
                    if (establecimiento.getNombre().toLowerCase().contains(s)) {
                        model.add(establecimiento);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewEstablecimiento;
        TextView textViewTitulo, textViewDireccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewEstablecimiento = itemView.findViewById(R.id.imagen_establecimiento);
            textViewTitulo = itemView.findViewById(R.id.titulo_establecimiento);
            textViewDireccion = itemView.findViewById(R.id.direccion_establecimiento);
        }
    }
}