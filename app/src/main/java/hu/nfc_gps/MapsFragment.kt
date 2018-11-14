package hu.nfc_gps

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import hu.nfc_gps.models.LocationModel
import java.util.*

class MapsFragment : Fragment() {

    companion object {
        private const val MY_REQUEST_CODE = 100
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var ref: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
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
        ref = FirebaseDatabase.getInstance().reference.child("Tamas").child("Locations")
    }

    private fun startLocationMonitoring() {
        requestNeededPermission()
    }

    private fun stopLocationMonitoring() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

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
                            Calendar.getInstance().time.toString()
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
                //TODO a Toast-okat ki kell cserélni SnackBar-okra
                Toast.makeText(activity, "Szükséges a GPS-hez", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(activity, "Meg van az engedély", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity as Activity, "Nincs mege az engedély", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}