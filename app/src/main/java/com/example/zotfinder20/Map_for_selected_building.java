package com.example.zotfinder20;

import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Color;


import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.geojson.Point;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.List;



public class Map_for_selected_building extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, Safety_Dialog.SafetyDialogListener {

    private static final LatLng BOUND_CORNER_NW = new LatLng(33.651033, -117.849535);
    private static final LatLng BOUND_CORNER_SE = new LatLng(33.640910, -117.835027);
    private static final LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
            .include(BOUND_CORNER_NW)
            .include(BOUND_CORNER_SE)
            .build();

    private MapboxMap mapboxMap;
    private MapView mapView;
    private Button button;
    private Button button_change;
    private Button button_arrived;

    private String buildingSelection = selected_building.getData();

    private String[][] location_database = {
            //blank for copy and paste purposes (delete later)
            {"", "","33.", "-117."},

            //Misc.
            {"Aldrich Hall", "","33.648432", "-117.841221"},
            {"AIRB", "","33.642886", "-117.838220"}, //Anteater Instruction and Research Building
            {"ALP", "", "33.646974", "-117.844535"}, //ALP is between humanities and biologies
            {"Anthill Pub and Grille", "","33.648970", "-117.842303"},
            {"E-sports Arena", "","33.648868", "-117.842531"},
            {"Gateway Study Center", "","33.647492", "-117.841746"},
            {"Greenhouse", "","33.647213", "-117.845234"},
            {"Jamba", "","33.648827", "-117.842069"},
            {"Langson Library", "","33.647189", "-117.841138"},
            {"Pheonix Food Court", "", "33.645576", "-117.840749"},
            {"Starbucks (Student Center)", "","33.648453", "-117.842121"},
            {"Student Health Center", "", "33.645560", "-117.836077"},
            {"Student Services II", "","33.647986", "-117.842311"},
            {"The Hill", "","33.648549", "-117.841802"}, //MIght need modifications
            {"West Food Court", "", "33.649125", "-117.842482"}, //may need verification
            {"Zot-N-Go", "", "33.648426", "-117.842691"},

            //Engineering + ICS
            {"Calit2", "", "33.643203", "-117.841050"}, //Calit2 Building Structure
            {"DBH", "","33.643433", "-117.842055"}, //Donald Bren Hall
            {"DSC", "", "33.644163", "-117.840341"}, //Disability Services Center
            {"ECT", "", "33.643972", "-117.840190"}, //Engineering and Computing Trailer
            {"EG", "","33.643067", "-117.840122"}, //Engineering Gateway
            {"EH", "","33.643854", "-117.841060"}, //Engineering Hall
            {"ELF", "","33.643844", "-117.839686"}, //Engineering Laboratory Facility
            {"ELH", "", "33.644343", "-117.840686"}, //Engineering Lecture Hall
            {"ET", "", "33.644619", "-117.841349"},  //Engineering Tower
            {"ICS", "", "33.644396", "-117.841602"}, //Information and Computer Science
            {"ICF", "","33.644407", "-117.840010"}, //Interim Classroom Facility
            {"Java City Kiosk", "", "33.643445", "-117.841162"},
            {"MDEA", "", "33.643830", "-117.840558"}, //McDonnel Douglas Engineering Auditorium
            {"REC", "", "33.643945", "-117.840542"}, //Rockwell Engineering Center
            {"University Club", "", "33.642927", "-117.842503"},

            //Humanities + Arts
            {"ACT", "","33.650464", "-117.844992"}, //Art, Culture and Technology
            {"AITR", "", "33.649759", "-117.843953"}, //Arts Instruction and Technology Resource Center
            {"CAC", "","33.650033", "-117.845322"}, //Contemporary Arts Center
            {"CTT", "","33.649303", "-117.845215"}, //Claire Trevor Theater
            {"HG", "","33.648248", "-117.844432"}, //Humanities Gateway
            {"HH", "", "33.647317", "-117.844024"}, //Humanities Hall
            {"HIB", "", "33.648363", "-117.843919"}, //Humanities Instructional Building
            {"HICF", "","33.646949", "-117.846847"}, //Humanities Interim Classroom Facility
            {"IAB", "","33.648212", "-117.845517"}, //Intercollegiate Athletic Building
            {"KH", "", "33.647742", "-117.843600"}, //Krieger Hall
            {"MM", "","33.649330", "-117.844507"}, //Music and Media Building
            {"Nixon Theater", "","33.650238", "-117.844491"},
            {"Sculpture & Ceramic Studios", "","33.650238", "-117.844491"},
            {"WSH", "", "33.649559", "-117.844387"}, //Winifred Smith Hall

            //Biology
            {"Arts Computation Engineering", "","33.646467", "-117.846892"},
            {"BC's Cavern Food Court", "","33.645911", "-117.844431"},
            {"BS3", "", "33.645677", "-117.845646"}, //Biological Sciences Lecture Hall III
            {"HSLH", "", "33.645613", "-117.844692"}, //Howard Schneiderman Lecture Hall
            {"MH", "","33.645214", "-117.844811"}, //McGaugh Hall
            {"NS1", "","33.644626", "-117.845432"}, //Natural Sciences I
            {"NS2", "","33.644299", "-117.845163"},
            {"Science Library", "", "33.645818", "-117.846846"},
            {"SH", "", "33.646272", "-117.845070"}, //Steinhaus Hall
            {"Starbucks (Biological Sciences III)", "","33.645022", "-117.845602"},

            //Social Sciences + Business
            {"MPAA", "","33.647546", "-117.837099"}, //Multipurpose Academic and Administrative Building
            {"SB1", "", "33.646953", "-117.838121"}, //Merage School of Business I
            {"SB2", "", "33.646673", "-117.838033"}, //Merage School of Business II
            {"SE", "", "33.646277", "-117.838883"}, //Social Ecology I
            {"SE2", "", "33.646650", "-117.838948"}, //Social Ecology II
            {"SSH", "", "33.646256", "-117.840158"}, //Social Science Hall
            {"SSL", "", "33.645978", "-117.840049"}, //Social Sciences Lab
            {"SSLH", "", "33.647245", "-117.839700"}, //Social Science Lecture Hall
            {"SSPA", "", "33.646996", "-117.839568"}, //Social Science Plaza A
            {"SSPB", "", "33.646932", "-117.838976"}, //Social Science Plaza B
            {"SST", "", "33.646500", "-117.840187"}, //Social Science Tower
            {"SSTR", "","33.647018", "-117.840288"}, //Social Science Trailer
            {"Starbucks (School of Business)", "","33.646968", "-117.838394"},
            {"UCI Summer Session", "","33.646537", "-117.837317"},


            //Physical Sciences
            {"Cafe Expresso", "", "33.643938", "-117.843515"},
            {"CRH", "", "33.643759", "-117.844739"}, //Croul Hall
            {"FRH", "", "33.644152", "-117.843558"}, //Frederick Reines Hall
            {"MSTB", "","33.642174", "-117.844381"}, //Multipurpose Science and Technology Building
            {"PCB", "", "33.644496", "-117.842746"}, //Parkview Classroom Building
            {"PSLH", "", "33.643395", "-117.843973"}, //Physical Sciences Lecture Hall
            {"PSCB", "", "33.643432", "-117.843537"}, //Physical Sciences Classroom Building
            {"RH", "", "33.644513", "-117.844224"} //Rowland Hall

            /*
            Missing Places that need confirmation on indoor or outdoor
            - AIRB 1030
            - BS 2130
            - CAC G021

             */


            /*
            Missing Places That I Might Need
            - Hewitt Research Hall
            - Art Studio
             */

            /*
            Missing PLaces I Might NOT Need
            - Beckman Laser Institute (BLI)
            - Bren Events Ceter

             */
    };

    private double inputlong;
    private double inputlat;
    

    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private LocationEngine locationEngine;
    private MapActivityLocationCallback callback = new MapActivityLocationCallback(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.map);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        //Prompt the Safety Dialog
        open_safety_dialogue();

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Set the boundary area for the map camera
                mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);

                // Set the minimum zoom level of the map camera
                mapboxMap.setMinZoomPreference(15);

                enableLocationComponent(style);

                style.addSource(new GeoJsonSource("source-id"));

                style.addLayer(new FillLayer("layer-id", "source-id").withProperties(
                        PropertyFactory.fillColor(Color.parseColor("#8A8ACB"))
                ));


                for (int i = 0; i < location_database.length; i++) {
                    if (buildingSelection.equals(location_database[i][0]) || (buildingSelection.equals(location_database[i][0]) && buildingSelection.equals(location_database[i][1]))){
                        inputlat = Double.parseDouble(location_database[i][2]);
                        inputlong = Double.parseDouble(location_database[i][3]);
                        break;
                    }
                }


                Point destinationPoint = Point.fromLngLat(inputlong, inputlat);
                Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(), locationComponent.getLastKnownLocation().getLatitude());
                getRoute(originPoint, destinationPoint);

                //Create the "marker" for the destination point
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        MarkerOptions destination_marker = new MarkerOptions();
                        destination_marker.title("Destination");
                        destination_marker.position(new LatLng(inputlat,inputlong));
                        mapboxMap.addMarker(destination_marker);
                    }
                });


                button_arrived = findViewById(R.id.arrivedbutton);
                button_arrived.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Call the arrival dialogue
                        open_arrival_dialogue();
                    }
                });

                button_change = findViewById(R.id.changeMAP);
                button_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (button_change.getText().equals("SAT")){
                            mapboxMap.setStyle(Style.SATELLITE_STREETS);
                            button_change.setText("MAP");
                        }else{
                            mapboxMap.setStyle(Style.MAPBOX_STREETS);
                            button_change.setText("SAT");
                        }
                    }
                });


                button = findViewById(R.id.StartButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        boolean simulateRoute = false;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();


                        // Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(Map_for_selected_building.this, options);
                    }



                });

            }
        });

    }

    private void open_arrival_dialogue()
    {
        Arrival_Dialog arrivaldialog = new Arrival_Dialog();
        arrivaldialog.show(getSupportFragmentManager(),"arrived");
        /*
        Intent intent_go_back_from_selected_building_page = new Intent(this,MainActivity.class);
        startActivity(intent_go_back_from_selected_building_page);
        finish();

         */
    }

    private void open_safety_dialogue() {
        Safety_Dialog safetydialog = new Safety_Dialog();
        safetydialog.show(getSupportFragmentManager(),"safety");
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.updateRouteVisibilityTo(false);
                            navigationMapRoute.updateRouteArrowVisibilityTo(false);
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Location Permission Needed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onOKclicked() {

    }

    private static class MapActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<Map_for_selected_building> activityWeakReference;

        MapActivityLocationCallback(Map_for_selected_building activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            Map_for_selected_building activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }

                // If user reaches destination --> create the prompt telling them to go back to main menu
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            Map_for_selected_building activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}