package com.example.cam.mealhc_v4
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.cam.mealhc_v4.R
import com.example.cam.mealhc_v4.R.id.spinner

class FoodFragment : Fragment() {

    //val spinner: Spinner = view.findViewById(R.id.spinner)

    override fun onCreateView(inflater: LayoutInflater, fragment_container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_food, fragment_container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.planets_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    */}
    companion object {
        fun newInstance(): FoodFragment = FoodFragment()
    }


}
