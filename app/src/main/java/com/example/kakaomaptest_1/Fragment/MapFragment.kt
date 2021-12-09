package com.example.kakaomaptest_1.Fragment

import android.content.Context
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaomaptest_1.Activity.MainActivity
import com.example.kakaomaptest_1.R
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.viewmodel.MNSViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.util.*


class MapFragment: Fragment(), MapView.MapViewEventListener, MapView.POIItemEventListener, LocationListener {

    private lateinit var rootView : View
    private lateinit var mapView : MapView
    private lateinit var mapViewContainer : ViewGroup
    private lateinit var imgBtnMyLoc : ImageButton
    private lateinit var imgBtnCreatePost : ImageButton
    private lateinit var mMNSViewModel: MNSViewModel
    private lateinit var mainFrame: SlidingUpPanelLayout
    private lateinit var callback : OnBackPressedCallback
    private lateinit var recyclerView: RecyclerView
    private lateinit var locationManager: LocationManager
    private lateinit var currentLoc: Location

    private var currentLocLoop = 3f //3초마다 갱신
    private var nearListLoop = 20f //30초마다 갱신
    private var postList = emptyList<Post>()
    val colorArray = arrayOf(R.drawable.pinred, R.drawable.pinblue, R.drawable.pingreen, R.drawable.pin, R.drawable.pinyellow)
    var nearPinList = emptyList<Post>()
    var LiveNearPinList : MutableLiveData<List<Post>> = MutableLiveData(nearPinList)


    override fun onResume() {
        super.onResume()
        imgBtnMyLoc.tag = 0
        imgBtnMyLoc.setColorFilter(Color.argb(100, 0, 0, 0))
        stopTracking()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //LoginFragment에서 감췄던 ActionBar 다시 show
        (requireActivity() as AppCompatActivity).supportActionBar!!.show()
        //LoginFragment에서 막아놨던 Drawer 다시 enable
        (requireActivity() as DrawerLocker).setDrawerEnabled(true)
        mapView = MapView(this.activity)
        rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)
        mainFrame = rootView.findViewById(R.id.main_frame)
        mapViewContainer = rootView.findViewById<ViewGroup>(R.id.map_view)
        mapViewContainer.addView(mapView)
        imgBtnMyLoc = rootView.findViewById<ImageButton>(R.id.iV_location)
        imgBtnMyLoc.tag = 0

        // 아이콘 이미지 변경
        imgBtnCreatePost = requireActivity().findViewById(R.id.imgBtn_createPost)
        imgBtnCreatePost.visibility = VISIBLE
        imgBtnCreatePost.setImageResource(R.drawable.location_w)

        mMNSViewModel = ViewModelProvider(this).get(MNSViewModel::class.java)
        mMNSViewModel.readAllPostData.observe(viewLifecycleOwner, { post ->
            this.postList = post
            setMarker()
        })

        // ********** 임시 SlidingDrawer Adapter 사용 **********
        // fragment_map.xml에 slidingdrawer에 있는 recyclerView
        recyclerView = rootView.findViewById<RecyclerView>(R.id.rV_fragment_map)
        // Adapter에 nearPinList 매개변수 전달
        val mAdapter = NearPinAdapter()
        recyclerView.adapter = mAdapter
        val layout = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)

        // ********** nearPinList에 달 Observer, LiveNearPinList 변경되면 어뎁터 새로 호출한다. **********
        val nearPinListObserver = Observer<List<Post>> { newList ->
            // Adapter에 nearPinList 매개변수 전달
            mAdapter.setData(newList)
            mAdapter.notifyDataSetChanged()
        }
        LiveNearPinList.observe(viewLifecycleOwner,nearPinListObserver)

        // 현재 위치 잡아주는 버튼
        imgBtnMyLoc.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                if(checkLocationService() && imgBtnMyLoc.tag == 0) {
                    startTracking()
                    imgBtnMyLoc.tag = 1
                    imgBtnMyLoc.setColorFilter(Color.argb(200, 255, 0, 0))
                    getLocation()
                } else {
                    if(imgBtnMyLoc.tag == 1) {
                        imgBtnMyLoc.tag = 0
                        imgBtnMyLoc.setColorFilter(Color.argb(100, 0, 0, 0))
                        stopTracking()
                    }
                    else {
                        Toast.makeText(context, "GPS를 사용해주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        
        // post 생성 버튼
        imgBtnCreatePost.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                if(imgBtnMyLoc.tag != 1){
                    Toast.makeText(context, "하단 버튼을 클릭해 현 위치를 불러오세요", Toast.LENGTH_SHORT).show()
                } else {
                    imgBtnCreatePost.setImageResource(R.drawable.edit)
                    val locBundle = setMPBundle(currentLoc)
                    (requireActivity() as MainActivity).setDrawerEnabled(false)
                    findNavController().navigate(R.id.action_mapFragment_to_postCreateFragment, locBundle)
                }
            }
        })
        
        return rootView
    }


    // ********** 두 마커간의 거리 구하는 함수들 (m로 반환) **********
    // getDistance로만 사용하면 된다.
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    private fun getDistance(lati1: Double, long1: Double, lati2: Double, long2: Double): Int {
        val theta = long1 - long2
        var dist =
            Math.sin(deg2rad(lati1)) * Math.sin(deg2rad(lati2)) + Math.cos(deg2rad(lati1)) * Math.cos(
                deg2rad(lati2)
            ) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344 * 1000
        return dist.toInt()
    }

    // ********** 현재 위치 기준으로 주변 마커 정보 불러오는 코드 구현 **********
    private fun listNearMarker(currentLoc: Location) {
        val lati: Double
        val long: Double
        val newNearPinList = arrayListOf<Post>()

        // 현재 위치 정보 받아오는 부분

        // currentLoc 없을 때 조건문 생성해야함
        lati = currentLoc.latitude
        long = currentLoc.longitude
        // 좌표 잘 잡히는지 확인하기 위한 Toast. 차후에 제거할 예정

        // postList에 있는 post 중에서 근처 post만 걸러내는 for문
        for (i in postList) {

            val distance = getDistance(lati, long, i.lati, i.longi)
            // 10000m 반경에 있는 post들 추출
            if (distance < 100) {
                newNearPinList.add(i)
                // 확인하기 위한 Toast. 차후에 제거할 예정
//                Toast.makeText(
//                    context,
//                    "글 제목 :" + i.title + " 거리 :" + distance.toString() + "m",
//                    Toast.LENGTH_SHORT
//                ).show()
            } else {
                // 확인하기 위한 Toast. 차후에 제거할 예정
//                Toast.makeText(context,distance.toString(),Toast.LENGTH_SHORT).show()
            }
        }
        // nearPinList를 주변 마커들을 담은 list로 갱신해줌
        LiveNearPinList.setValue(newNearPinList)
    }
    // 마커 생성 함수
    private fun setMarker() {
        var title: String
        var lati: Double
        var long: Double
        var markerType: Int
        var postId: Int

        //현재시각
        val now = Calendar.getInstance()

        //Toast.makeText(context, postList.size.toString(), Toast.LENGTH_SHORT).show()
        for(i in postList) {

            val p_date = i.date
            val calcuDate = (now.time.time - p_date.time) / (60 * 60 * 24 * 1000)

            if(calcuDate >= 1)
                continue

            title = i.title
            lati = i.lati
            long = i.longi
            markerType = i.pinType
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

    // 현재 위치 정보 받아와서 bundle 객체 생성하는 부분
    private fun setMPBundle(loc : Location): Bundle {
        val bundle = Bundle()
        bundle.putDouble("lati", loc.latitude)
        bundle.putDouble("long", loc.longitude)

        return bundle
    }

    private fun checkLocationService(): Boolean {
        val locationManager : LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getLocation() {
        try {
            locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0.0F, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // 현재 위치 잡아주는 함수 ( Tracking 시작 )
    private fun startTracking() {
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
    }

    // Tracking 멈추는 함수
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
        return
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
        val temp = p1!!.tag

        for(i in postList) {
            if(i.key == temp) {
                val bundle = Bundle()
                bundle.putInt("key", i.key)
                (requireActivity() as MainActivity).setDrawerEnabled(false)
                val imgBtn = (requireActivity() as MainActivity).findViewById<ImageButton>(R.id.imgBtn_back)
                imgBtn.visibility = VISIBLE
                findNavController().navigate(R.id.action_mapFragment_to_postReadFragment, bundle)
            }
        }

        return
    }

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


    //핸드폰의 뒤로가기 버튼을 눌렀을 때 이전 단계 fragment로 이동하기 위함 이동하면 기능 상실
    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(mainFrame.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    mainFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                } else {
                    findNavController().navigate(R.id.action_mapFragment_to_loginFragment)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onLocationChanged(location: Location) {
        if(currentLocLoop == 3f) {
            currentLoc = location
            currentLocLoop = 0f
        } else {
            currentLocLoop += 0.5f
        }

        if(nearListLoop == 20f) {
            listNearMarker(location)
            nearListLoop = 0f
        } else {
            nearListLoop += 0.5f
        }
    }

}