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
import kotlinx.android.synthetic.main.item_friend.*


class DetailFragment: BaseFragment(), OnMapReadyCallback {

    companion object Factory {
        const val USER_ID: String = "USER_ID"

        fun createInstance(id: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, id)
                }
            }
        }
    }

    private var lastMarker: Marker? = null
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
        viewModel.init(arguments?.getString(USER_ID)!!)
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
            var friend: Friend? = it.tag as Friend?
            lastMarker?.let {
                it.title = null
            }
            lastMarker = it
            it.title = friend?.name
            true
        }
        updateUI(viewModel.getFriend.value)
    }

    fun listEvent(){
        var disposableLoadFriend: Disposable = viewModel.getFriend.subscribe{
            updateUI(it)
        }
        compositeDisposable.add(disposableLoadFriend)
    }

    fun updateUI(friend: Friend?){
        friend?.let {
            txtName.text = friend.name
            Glide.with(context!!)
                .load(friend.picture)
                .apply(RequestOptions.circleCropTransform())
                .into(imgPicture)

            mMap?.let {
                var location: Location? = friend.location
                val latlng = LatLng(location?.latitude!!, location?.longitude!!)
                var marker = mMap?.addMarker(MarkerOptions().position(latlng))
                marker?.tag = friend
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                return
            }
        }
    }
}