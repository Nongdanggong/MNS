package com.example.kakaomaptest_1.Fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.text.SimpleDateFormat
import java.util.*

class MapFragment: Fragment(), MapView.MapViewEventListener, MapView.POIItemEventListener {

    private lateinit var rootView : View
    private lateinit var mapView : MapView
    private lateinit var mapViewContainer : ViewGroup
    private lateinit var imgBtn_myLoc : ImageButton
    private lateinit var imgBtn_createPost : ImageButton
    private lateinit var currentLoc : Bundle
    private lateinit var mMNSViewModel: MNSViewModel
    private var postList = emptyList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        imgBtn_myLoc.tag = 0
        imgBtn_myLoc.setColorFilter(Color.argb(100, 0, 0, 0))
        stopTracking()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
        rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = MapView(this.activity)
        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)
        mapViewContainer = rootView.findViewById<ViewGroup>(R.id.map_view)
        mapViewContainer.addView(mapView)
        imgBtn_myLoc = rootView.findViewById<ImageButton>(R.id.iV_location)
        imgBtn_myLoc.tag = 0
        currentLoc = Bundle()
        imgBtn_createPost = requireActivity().findViewById<ImageButton>(R.id.imgBtn_createPost)
        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.readAllPostData.observe(viewLifecycleOwner, Observer { post ->
            this.postList = post
            setMarker()
        })

        imgBtn_myLoc.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                if(checkLocationService() && imgBtn_myLoc.tag == 0) {
                    startTracking()
                    imgBtn_myLoc.tag = 1
                    imgBtn_myLoc.setColorFilter(Color.argb(200, 255, 0, 0))
                } else {
                    if(imgBtn_myLoc.tag == 1) {
                        imgBtn_myLoc.tag = 0
                        imgBtn_myLoc.setColorFilter(Color.argb(100, 0, 0, 0))
                        stopTracking()
                    }
                    else {
                        Toast.makeText(context, "GPS를 사용해주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        imgBtn_createPost.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                if(imgBtn_myLoc.tag != 1){
                    Toast.makeText(context, "하단 버튼을 클릭해 현 위치를 불러오세요", Toast.LENGTH_SHORT).show()
                } else {
                    setMPBundle()
                    findNavController().navigate(R.id.action_mapFragment_to_postCreateFragment, currentLoc)
                }
            }
        })
        
        return rootView
    }

    private fun setMarker() {
        var colorArray = arrayOf(R.drawable.pinred, R.drawable.pinblue, R.drawable.pingreen, R.drawable.pin, R.drawable.pinyellow)
        var title: String
        var lati: Double
        var long: Double
        var markerType: Int
        var postId: Int

        //Toast.makeText(context, postList.size.toString(), Toast.LENGTH_SHORT).show()
        for(i in postList) {
            title = i.title
            lati = i.lati
            long = i.longi
            markerType = i.markerType
            postId = i.key
            val tempPoint = MapPoint.mapPointWithGeoCoord(lati, long)
            val tempItem = MapPOIItem()
            tempItem.tag = postId
            tempItem.markerType = MapPOIItem.MarkerType.CustomImage
            tempItem.customImageResourceId = colorArray[markerType]
            tempItem.customImageAnchorPointOffset = MapPOIItem.ImageOffset(32, 0)
            tempItem.mapPoint = tempPoint
            tempItem.itemName = title
            mapView.addPOIItem(tempItem)
        }
    }

    private fun setMPBundle() {
        val a: MapPoint
        a = this.mapView.mapCenterPoint

        currentLoc.putDouble("lati", a.mapPointGeoCoord.latitude)
        currentLoc.putDouble("long", a.mapPointGeoCoord.longitude)
    }

    private fun checkLocationService(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun startTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    private fun stopTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    override fun onMapViewInitialized(p0: MapView?) {
        return
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        return
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        return
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

//        if(imgBtn_markerSwitch.tag == 1) {
//            if(checkSelected(p0)) {
//                return
//            }
//            setMarker(p0, p1)
//        } else {
//            return
//        }
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
        return
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        return
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        return
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        return
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        return
    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        var temp = p1!!.tag

        var form = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        for(i in postList) {
            if(i.key == temp) {
                var bundle = Bundle()
                bundle = bundleOf(
                    "key" to i.key,
                    "userid" to i.userCreatorId,
                    "title" to i.title,
                    "markerType" to i.markerType,
                    "lati" to i.lati,
                    "longi" to i.longi,
                    "uri" to i.photoUri,
                    "text" to i.text,
                    "date" to form.format(i.date)
                )

                findNavController().navigate(R.id.action_mapFragment_to_postReadFragment, bundle)
            }
        }

        return
    }

//    private fun deleteMarker(p0: MapView?, p1: MapPOIItem?) {
//        var builder = AlertDialog.Builder(context)
//        var itemName = p1!!.itemName
//
//        builder.setTitle(itemName)
//        builder.setMessage("이 마커를 삭제하시겠습니까?")
//        builder.setPositiveButton("삭제") { dialog, which ->
//            p0!!.removePOIItem(p1)
//            Toast.makeText(context, "\"" + itemName + "\"이 삭제되었습니다", Toast.LENGTH_SHORT).show()
//            dialog.dismiss()
//        }
//        builder.setNegativeButton("취소") {dialog, which ->
//            dialog.dismiss()
//        }
//        builder.show()
//    }
//
//    private fun setMarker(p0: MapView?, p1: MapPoint?) {
//        var inflater = LayoutInflater.from(context)
//        var marker = MapPOIItem()
//        var builder = AlertDialog.Builder(context)
//        var view = inflater.inflate(R.layout.dialog_url, null)
//        var eText = view.findViewById<EditText>(R.id.eText_marker)
//
//        marker.tag = 1
//        marker.mapPoint = p1
//        marker.markerType = MapPOIItem.MarkerType.BluePin
//        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
//
//        builder.setView(view)
//        builder.setPositiveButton("입력") { dialog, which ->
//            if(eText.text.toString() == "") {
//                marker.itemName = "Default"
//                p0!!.addPOIItem(marker)
//            } else {
//                var markerName = eText.text.toString()
//                marker.itemName = markerName
//                p0!!.addPOIItem(marker)
//            }
//            Toast.makeText(context, marker.itemName, Toast.LENGTH_SHORT).show()
//            dialog.dismiss()
//        }
//        builder.setNegativeButton("취소") { dialog, which ->
//            dialog.dismiss()
//        }
//        builder.show()
//    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
        return
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        return
    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        return
    }
}