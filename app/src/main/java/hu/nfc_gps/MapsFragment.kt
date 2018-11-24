package hu.nfc_gps

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import hu.nfc_gps.models.LocationModel
import kotlinx.android.synthetic.main.fragment_maps.*
import java.util.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val MY_REQUEST_CODE = 100
        private const val DELTA_TIME = 14_400_000
        private const val LOCATION_KEY = "Locations"
    }

    lateinit var googleMap: GoogleMap
    lateinit var mapView: MapView
    lateinit var myView: View

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_maps, container, false)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = myView.findViewById(R.id.map) as MapView

        mapView.apply {
            onCreate(null)
            onResume()
            getMapAsync(this@MapsFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationMonitoring()
    }

    override fun onPause() {
        super.onPause()
        stopLocationMonitoring()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()

        ref = database.reference.child(auth.uid.toString()).child(LOCATION_KEY)

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val location = dataSnapshot.getValue(LocationModel::class.java)
                location?.apply {
                    val otherTime = Timestamp.toDouble()

                    if (System.currentTimeMillis() - otherTime > DELTA_TIME) {
                        dataSnapshot.ref.removeValue()
                    } else {
                        googleMap.apply {
                            addMarker(
                                MarkerOptions().position(LatLng(Latitude.toDouble(), Longitude.toDouble())).title(
                                    Date(Timestamp.toLong()).toString()
                                )
                            )
                        }
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                //empty
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("myTag", "A hiba: ${error.message}")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                //empty
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                //empty
            }
        })
    }

    private fun startLocationMonitoring() = requestNeededPermission()

    private fun stopLocationMonitoring() = fusedLocationClient.removeLocationUpdates(locationCallback)

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult ?: return
            for (location in locationResult.locations) {
                val key = ref.push().key
                key?.let {
                    ref.child(it).setValue(
                        LocationModel(
                            location.longitude.toString(),
                            location.latitude.toString(),
                            System.currentTimeMillis().toString()
                        )
                    )
                }
            }
        }
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                activity as Activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity as Activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Snackbar.make(mapsFragment, "Szükséges a GPS-hez", Snackbar.LENGTH_INDEFINITE).show()
            }

            ActivityCompat.requestPermissions(
                activity as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MY_REQUEST_CODE
            )
        } else {
            val locationRequest = LocationRequest().apply {
                interval = 10000
                fastestInterval = 8000
                priority = PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, getString(R.string.permissionGrantedText), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity as Activity, getString(R.string.permissionDeniedText), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)

        this.googleMap = googleMap
        this.googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }
}