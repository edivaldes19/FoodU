package Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manuel.foodu.ComunicarActivitys;
import com.manuel.foodu.R;

import java.util.ArrayList;

import Adaptadores.AdapterEstablecimiento;
import Mapa.Establecimiento;
import Mapa.LatitudLongitud;

public class CategoryFragment extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener {

    AdapterEstablecimiento adapterEstablecimiento;
    RecyclerView recyclerViewEstablishment;
    ArrayList<Establecimiento> listaEstablecimientos;
    Activity activity;
    ComunicarActivitys comunicarActivitys;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerViewEstablishment = view.findViewById(R.id.recyclerView);
        listaEstablecimientos = new ArrayList<>();
        cargarLista();
        mostrarData();
        setHasOptionsMenu(true);
        return view;
    }

    private void mostrarData() {
        recyclerViewEstablishment.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterEstablecimiento = new AdapterEstablecimiento(getContext(), listaEstablecimientos);
        recyclerViewEstablishment.setAdapter(adapterEstablecimiento);
        adapterEstablecimiento.setOnClickListener(view -> comunicarActivitys.enviarEstablecimiento(listaEstablecimientos.get(recyclerViewEstablishment.getChildAdapterPosition(view))));
    }

    private void cargarLista() {
        listaEstablecimientos.addAll(LatitudLongitud.establecimiento);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.buscador, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterEstablecimiento.filter(newText);
        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
            comunicarActivitys = (ComunicarActivitys) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}