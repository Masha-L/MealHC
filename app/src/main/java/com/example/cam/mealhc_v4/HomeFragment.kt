package com.example.cam.mealhc_v4
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cam.mealhc_v4.R

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, fragment_container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, fragment_container, false)

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}

