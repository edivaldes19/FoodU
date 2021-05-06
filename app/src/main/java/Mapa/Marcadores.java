package Mapa;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Marcadores {
    public MarkerOptions marker;
    public LatLng latLng;

    public MarkerOptions Marcador(double latitud, double longitud, String titulo) {
        latLng = new LatLng(latitud, longitud);
        marker = new MarkerOptions();
        marker.position(latLng);
        marker.title(titulo);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        marker.alpha(0.75f);
        return marker;
    }

    public LatLng posicionDefecto() {
        return new LatLng(19.4147269, -102.0522647);
    }
}