package com.king.appdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.king.appdemo.R
import com.king.appdemo.core.pojo.Friend
import com.king.appdemo.core.pojo.Location
import com.king.appdemo.core.widget.BaseFragment
import com.king.appdemo.vm.DetailViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.realm.RealmResults
import kotlinx.android.synthetic.main.item_friend.*


class DetailFragment: BaseFragment(), OnMapReadyCallback {

    companion object Factory {
        const val USER_ID: String = "USER_ID"
        const val IS_SHOW_ALL: String = "IS_SHOW_ALL"


        fun createInstance(id: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, id)
                }
            }
        }

        fun createInstance(showAll: Boolean): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_SHOW_ALL, showAll)
                }
            }
        }
    }

    private var mMap: GoogleMap? = null
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = DetailViewModel(getAppEngine())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            viewModel.init(it.getString(USER_ID)!!, it.getBoolean(IS_SHOW_ALL, false))
        }
        listEvent()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        viewModel.dispose()
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.setOnMarkerClickListener {
            viewModel.clickMarker(it)
            true
        }
        viewModel.setMapReady()
    }

    fun listEvent(){
        var disposableLoadFriend: Disposable = viewModel.updateUI.subscribe{
            updateUI(it)
        }
        var disposeAddMarker = viewModel.addMarkers.subscribe{
            addMarker(it)
        }
        var disposeFocusChange = viewModel.focusMarkerChange.subscribe{
            focusMarkerChange ->
                focusMarkerChange.oldMarker?.let {
                    it.title = null
                }
                focusMarkerChange.newMarker?.let {
                    it.title = focusMarkerChange.selectedFriend?.name
                }
            updateUI(focusMarkerChange.selectedFriend)
        }
        compositeDisposable.add(disposeFocusChange)
        compositeDisposable.add(disposableLoadFriend)
        compositeDisposable.add(disposeAddMarker)

    }

    fun updateUI(friend: Friend?){
        friend?.let {
            txtName.text = friend.name
            Glide.with(context!!)
                .load(friend.picture)
                .apply(RequestOptions.circleCropTransform())
                .into(imgPicture)
        }
    }

    fun addMarker(friends: RealmResults<Friend>?){
        friends?.let {
            for (friend: Friend in friends) {
                val location: Location? = friend.location
                val latlng = LatLng(location?.latitude!!, location.longitude!!)
                val marker = mMap?.addMarker(MarkerOptions().position(latlng))
                marker?.tag = friend
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(latlng))
            }
        }
    }
}