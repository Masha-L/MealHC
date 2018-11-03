package com.example.cam.mealhc_v4
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.CheckBox
import com.example.cam.mealhc_v4.R
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity 
import android.widget.TextView 
import android.view.MenuItem
    class SettingsFragment : Fragment(){



 

    override fun onCreateView(inflater: LayoutInflater, fragment_container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_settings, fragment_container, false)   /*  {
                        super.onCreate(savedInstanceState)
                dairyCheck = getView().findViewById(R.id.dairyCheck)
            }        */

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }
    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
       /*
    private fun changePreference(dairy: CheckBox, checked:Boolean) {
        val checked = dairy.isChecked()

        App.prefs!!.dairyAllergy = checked
    }
            */
    private val mOnClickListener = View.OnClickListener { item ->

          when (item.id)
    {
             R.id.dairyCheck-> {



             }
    }
}

    }
