package marko.mitrovic.singidroid4.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import marko.mitrovic.singidroid4.R;
import org.jetbrains.annotations.NotNull;

public class LocationFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        AdapterView.OnItemSelectedListener{
    final static String TAG = "LocationFragment";
    final LatLng SINGID_DANIJELOVA_BG = new LatLng(44.78224991433838, 20.479034350409012);
    private View view;
    private MapView mapView;
    private GoogleMap googleMap;
    private Spinner uniLocationSpinner;
    private LatLng beogradDan, beogradKum, noviSad, nis, zlatibor;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beogradDan = new LatLng(44.78224991433838, 20.479034350409012);
        beogradKum = new LatLng(44.75943473663913, 20.49655333049052);
        noviSad = new LatLng(45.2530986579024, 19.844236965141153);
        nis = new LatLng(43.31872455232555, 21.896026789820684);
        zlatibor = new LatLng(43.728680803488544, 19.704651877755616);

    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_university_location, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);//Works only when OnMapReadyCallback is implemented

        //Uni location spinner setup
        uniLocationSpinner = view.findViewById(R.id.locationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.uni_locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uniLocationSpinner.setOnItemSelectedListener(this); //Works only when OnItemSelectedListener is implemented
        uniLocationSpinner.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap map) {
        googleMap = map;
        googleMap.addMarker(new MarkerOptions()
                .position(beogradDan)
                .title("Univerzitet Singidunum Danijelova"));

        googleMap.addMarker(new MarkerOptions()
                .position(beogradKum)
                .title("Univerzitet Singidunum Kumodraska"));

        googleMap.addMarker(new MarkerOptions()
                .position(noviSad)
                .title("Centar Novi Sad"));

        googleMap.addMarker(new MarkerOptions()
                .position(nis)
                .title("Centar Nis"));
        googleMap.addMarker(new MarkerOptions()
                .position(zlatibor)
                .title("Centar Zlatibor"));
        googleMap.setOnMarkerClickListener(this);
//        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
//                this.getContext(), R.raw.style_json));
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.78224991433838, 20.479034350409012), 17));

    }

    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        Log.d(TAG, "onMarkerClick: " + marker.getTitle());
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull @NotNull Marker marker) {
        Log.d(TAG, "onInfoWindowClick: " + marker.getTitle());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: " + uniLocationSpinner.getSelectedItem().toString());
        String selectedLocation = uniLocationSpinner.getSelectedItem().toString();
        final int zoomLevel = 16;
        switch (selectedLocation) {
            case "Univerzitet Singidunum Danijelova":
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beogradDan, zoomLevel));
                break;
            case "Univerzitet Singidunum Kumodraska":
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beogradKum, zoomLevel));
                break;
            case "Centar Novi Sad":
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(noviSad, zoomLevel));
                break;
            case "Centar Nis": //Nikole Pasica
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nis, zoomLevel));
                break;
            case "Centar Zlatibor": //Zlatibor Centar
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zlatibor, zoomLevel));
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

