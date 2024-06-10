package com.stopsmoke.kekkek.presentation.rankingMap

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentRankingMapBinding

class RankingMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentRankingMapBinding? = null
    private val binding: FragmentRankingMapBinding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

//    private val placesClient: PlacesClient by lazy {
//        Places.createClient(requireContext())
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    }

    private fun setGoogleMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapView_rankingMap) as SupportMapFragment
        mapFragment.getMapAsync(this@RankingMapFragment)
        mapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.setAllGesturesEnabled(false)
        }
    }

    private fun setAppbar() {
        val ivMyWritingMapBack =
            requireActivity().findViewById<ImageView>(R.id.iv_rankingList_back)
        val tvMyWritingMapTitle =
            requireActivity().findViewById<TextView>(R.id.tv_rankingList_title)

        ivMyWritingMapBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tvMyWritingMapTitle.text = "내 지역 설정"
    }


    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

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
            return
        }

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                // 반경 1km를 표시하는 원 추가
                googleMap.addCircle(
                    CircleOptions()
                        .center(currentLatLng)
                        .radius(1000.0) // 반경 1km
                        .strokeWidth(2f)
                        .strokeColor(0xFF0000FF.toInt())
                        .fillColor(0x220000FF) // 반투명 파란색
                )
            } else {
                Toast.makeText(requireContext(), "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "위치를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
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

