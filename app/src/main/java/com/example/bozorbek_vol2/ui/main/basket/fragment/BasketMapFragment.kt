package com.example.bozorbek_vol2.ui.main.basket.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_basket_map.*


class BasketMapFragment : BaseBasketFragment() {

    lateinit var client: FusedLocationProviderClient
    private lateinit var latitude:String
    private lateinit var longitude:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket_map, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        send_location.setOnClickListener {
            val action = BasketMapFragmentDirections.actionBasketMapFragmentToBasketFragment(latitude = latitude, longitude = longitude)
            findNavController().navigate(action)
        }

        when (GooglePlayServicesUtil.isGooglePlayServicesAvailable(requireActivity())) {
            ConnectionResult.SUCCESS -> {

                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocationFromMap()
                }
                else{
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
                }


            }
            ConnectionResult.SERVICE_MISSING -> Toast.makeText(
                activity,
                "Google Play Services Missing",
                Toast.LENGTH_SHORT
            ).show()
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ->             //The user has to update play services
                Toast.makeText(activity, "Update Google Play Services", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(
                activity,
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(requireActivity()),
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && (grantResults.size > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED))
        {
            getLocationFromMap()
        }
        else{
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationFromMap() {

        val supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER))
        {
            client.lastLocation.addOnCompleteListener { task ->
                val location1 = task.result
                if (location1 != null)
                {
                    supportMapFragment.getMapAsync { google_map ->
                        val location = LatLng(location1.latitude, location1.longitude)
                        val marketOptions = MarkerOptions().position(location).title("Я нахожусь здесь")
                        latitude = location1.latitude.toString()
                        longitude = location1.longitude.toString()
                        google_map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
                        google_map.addMarker(marketOptions)

                        google_map.setOnMapClickListener {latLang->
                            val marketOptions = MarkerOptions()
                            marketOptions.position(latLang)
                            marketOptions.title("${latLang.latitude} : ${latLang.longitude}")
                            latitude = latLang.latitude.toString()
                            longitude = latLang.longitude.toString()
                            google_map.clear()
                            google_map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 18f))
                            google_map.addMarker(marketOptions)
                        }
                    }
                }
                else
                {
                    val locationRequest = LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000)
                        .setFastestInterval(1000)
                        .setNumUpdates(1)

                    val locationCallback = object : LocationCallback(){
                        override fun onLocationResult(p0: LocationResult) {
                            super.onLocationResult(p0)
                            val location = p0.lastLocation
                        }
                    }

                    client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper()!!)
                }
            }
        }
        else{
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }


    }


}