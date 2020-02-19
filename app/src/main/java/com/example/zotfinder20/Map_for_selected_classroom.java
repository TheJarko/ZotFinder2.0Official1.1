package com.example.zotfinder20;

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



public class Map_for_selected_classroom extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener {

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

    //private String buildingSelection = selected_classroom.getData();
    private String classroomSelection = selected_classroom.getData();


    //Outdoor locations have their own specialized latitudes and longitudes
    //Indoor locations share a latitude and longitude based on their location with respect to the nearest entrance,
    //     which is the latitude and longitude of reference
    private String[][] classroom_location_database = {
            //Template for copy and paste purposes
            {"", "","33.", "-117."},

            //Misc.
            //{"GENERAL ALP INDOORS", "","33.647113", "-117.844949"},
            //WE MIGHT HAVE TO FORCE THE USER TO ENTER THROUGH ONE DOOR


            // ****** ENGINEERING + ICS ****** //

            //Engineering + ICS (OUTDOOR)
            {"DBH 1100", "","33.643531", "-117.842098"},
            {"DBH 1200", "","33.643524", "-117.841841"},
            {"DBH 1300", "","33.643418", "-117.841828"},
            {"DBH 1600", "","33.643405", "-117.841680"}, //This one is inaccurate
            {"DBH 1420", "","33.643146", "-117.842172"},
            {"DBH 1422", "","33.643059", "-117.842157"},
            {"EH 1200", "","33.643854", "-117.841060"},
            {"ICS 209", "","33.644225", "-117.841564"},
            {"ICS 213", "","33.644193", "-117.841632"},



            // Engineering + ICS (INDOOR)
            //{"GENERAL DBH INDOORS", "",""33.64343", "-117.841727"},
            {"DBH 1500", "","33.64343", "-117.841727"},
            //Add the remaining DBH classrooms under the same coordinates

            //{"GENERAL ICS INDOORS", "","33.644503", "-117.842047"},
            // FOR THIS ONE, TRY TO ADD A PROMPT TELLING THE USER TO ENTER THE BOTTOM DOOR

            // ****** SOCIAL SCIENCES ******//

            //Social Sciences (OUTDOOR)
            {"SE2 1304", "","33.", "-117."},
            {"SE2 1306", "","33.", "-117."},
            {"SSH 100", "","33.646257", "-117.840089"},
            {"SSL 206", "","33.645814", "-117.840171"},
            {"SSL 228", "","33.645953", "-117.840213"},
            {"SSL 248", "","33.645988", "-117.839851"},
            {"SSL 270", "","33.645849", "-117.839834"},
            {"SSLH 100", "","33.647172", "-117.839802"},
            {"SSPA 1100", "","33.646728", "-117.839532"},
            {"SST 220A", "","33.646443", "-117.840331"}, //Technically an indoor location but so near an outdoor location to be considered one, unless we access the building trough the 1st floor
            {"SST 220B", "","33.646439", "-117.840597"},
            {"SSTR 100", "","33.646988", "-117.840327"},
            {"SSTR 101", "","33.647041", "-117.840230"},
            {"SSTR 102", "","33.646970", "-117.840162"},
            {"SSTR 103", "","33.646920", "-117.840270"},


            //Social Sciences (INDOOR)
            // 1 indoor location in SST (SST 238)
            //{"SSPA 1165 AND 1170", "","33.646943", "-117.839551"},
            //{"GENERAL SST 1st FLOOR INDOORS", "","33.646132", "-117.839942"}
            //{"GENERAL SST 2nd FLOOR INDOORS", "","33.646282", "-117.839748"}
                    // Create a prompt telling the user to go up the stairs?

            // ****** BIOLOGY ******

            //Biology (OUTDOOR)
            {"BS3 1200", "","33.645677", "-117.845646"},
            {"HSLH 100", "","33.645613", "-117.844692"},

            //Biology (INDOOR)

           // ****** HUMANITIES + ARTS ******

            //Humanities + Arts (OUTDOOR)
            {"HIB 100", "","33.648339", "-117.843387"},
            {"HIB 110", "","33.648292", "-117.843562"},
            {"HICF 100K", "","33.646865", "-117.846750"},
            {"HICF 100L", "","33.646837", "-117.846877"},
            {"HICF 100M", "","33.646959", "-117.846776"},
            {"HICF 100N", "","33.646925", "-117.846900"},
            {"HICF 100P", "","33.647036", "-117.846797"},
            {"HICF 100Q", "","33.647015", "-117.846913"},
            {"WSH 100", "","33.649555", "-117.844441"},

            //Humanities + Arts (INDOOR)
            //{"GENERAL HH 200 SERIES", "","33.647457", "-117.844205"},
            //{"GENERAL HH 100 SERIES", "","33.647433", "-117.843750"},

            // ***** PHYSICAL SCIENCES ******

            //Physical Sciences (OUTDOOR)
            {"PCB 1100", "","33.644585", "-117.842652"},
            {"PCB 1200", "","33.644574", "-117.842833"},
            {"PCB 1300", "","33.644574", "-117.842961"},
            {"PSCB 120", "","33.643457", "-117.843562"},
            {"PSCB 140", "","33.643402", "-117.843427"},
            {"PSCB 210", "","33.", "-117."},
            {"PSCB 220", "","33.", "-117."},
            {"PSCB 230", "","33.", "-117."},
            {"PSCB 240", "","33.", "-117."},

            {"", "","33.", "-117."}
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


                for (int i = 0; i < classroom_location_database.length; i++) {
                    if (classroomSelection.equals(classroom_location_database[i][0])){
                        inputlat = Double.parseDouble(classroom_location_database[i][2]);
                        inputlong = Double.parseDouble(classroom_location_database[i][3]);
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
                        NavigationLauncher.startNavigation(Map_for_selected_classroom.this, options);


                        //provide an if statement if a user has reached destination here?
                    }
                });
            }
        });
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

    private static class MapActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<Map_for_selected_classroom> activityWeakReference;

        MapActivityLocationCallback(Map_for_selected_classroom activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            Map_for_selected_classroom activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
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
            Map_for_selected_classroom activity = activityWeakReference.get();
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