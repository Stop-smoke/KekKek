package com.agvber.kekkek.presentation.ranking.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.agvber.kekkek.R
import com.agvber.kekkek.databinding.FragmentRankingMapBinding
import java.io.IOException
import java.util.Locale


class RankingMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentRankingMapBinding? = null
    private val binding: FragmentRankingMapBinding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private lateinit var placesClient: PlacesClient

    private var marker: Marker? = null
    private var circle: Circle? = null

    private var radius = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Places.initialize(requireContext(), null)
        placesClient = Places.createClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() = with(binding) {
        setAppbar()
        setGoogleMap()

        dotClick()
    }


    private fun dotClick() {
        val dotToTextMap: Map<TextView, Int> = mapOf(
            binding.tvRankingMap5KmSelectDot to 5,
            binding.tvRankingMap10KmSelectDot to 10,
            binding.tvRankingMap15KmSelectDot to 15,
            binding.tvRankingMap20KmSelectDot to 20,
        )

        val listDot: List<TextView> = listOf(
            binding.tvRankingMap5KmSelectDot,
            binding.tvRankingMap10KmSelectDot,
            binding.tvRankingMap15KmSelectDot,
            binding.tvRankingMap20KmSelectDot,
        )

        dotToTextMap.forEach { dotToText ->
            dotToText.key.setOnClickListener {
                listDot.forEach { tv ->
                    tv.setBackgroundResource(R.drawable.default_dot)
                }
                dotToText.key.setBackgroundResource(R.drawable.selected_dot)
                radius = dotToText.value * 1000

                setMapMark()
            }
        }
    }

    private fun setGoogleMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView_rankingMap) as SupportMapFragment
        mapFragment.getMapAsync(this@RankingMapFragment)
    }


    private fun setAppbar() {
        binding.includeRankingMapAppBar.ivRankingMapBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.uiSettings.isScrollGesturesEnabled = false
        googleMap.uiSettings.isZoomGesturesEnabled = false

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }

        googleMap.isMyLocationEnabled = true

        setMapMark()
    }

    private fun setMapMark() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)

                    val zoomLevel = when (radius) {
                        in 0..5000 -> 13f // 5km 반경일 때
                        in 5001..10000 -> 12f // 10km 반경일 때
                        in 10001..15000 -> 11f // 15km 반경일 때
                        else -> 10f // 20km 이상일 때
                    }

                    marker?.remove()
                    circle?.remove()

                    marker =
                        googleMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            zoomLevel
                        )
                    )

                    circle = googleMap.addCircle(
                        CircleOptions()
                            .center(currentLatLng)
                            .radius(radius.toDouble()) // 반경
                            .strokeWidth(2f)
                            .strokeColor(0xFF0000FF.toInt())
                            .fillColor(0x220000FF) // 반투명 파란색
                    )

                    fetchNearbyPlaces(currentLatLng)
                } else {
                    Toast.makeText(requireContext(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "위치를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchNearbyPlaces(currentLatLng: LatLng) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }

        val request = FindCurrentPlaceRequest.builder(listOf(Place.Field.LAT_LNG, Place.Field.NAME))
            .build()

        val placeResponse = placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                if (response != null) {
                    val placesList = mutableListOf<String>()

                    for (placeLikelihood in response.placeLikelihoods) {
                        val place = placeLikelihood.place
                        val placeLatLng = place.latLng
                        if (placeLatLng != null) {
                            val distance = FloatArray(1)
                            Location.distanceBetween(
                                currentLatLng.latitude, currentLatLng.longitude,
                                placeLatLng.latitude, placeLatLng.longitude, distance
                            )
                            if (distance[0] <= radius) {
                                getAdminArea(placeLatLng) { adminArea ->
                                    placesList.add(adminArea)
                                }
                            }
                        }
                    }
                    displayPlaces(placesList)
                }
            } else {
                Log.e("PlacesAPI", "${task.exception}")
            }
        }
    }

    private fun getAdminArea(latLng: LatLng, callback: (String) -> Unit) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.let { addressList ->
                for (address in addressList) {
                    val city = address.locality
                    val district = address.subLocality
                    val adminArea = if (!city.isNullOrBlank()) {
                        city
                    } else {
                        district
                    }
                    if (!adminArea.isNullOrBlank()) {
                        callback(adminArea)
                        return
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun displayPlaces(placesList: List<String>) {
        val placesList = placesList.toSet().toList().filter { it != "Unknown Area" }
        Log.d("address", placesList.toString())
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RankingMapFragment()
    }
}

