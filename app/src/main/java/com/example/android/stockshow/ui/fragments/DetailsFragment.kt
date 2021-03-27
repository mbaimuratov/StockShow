package com.example.android.stockshow.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.android.stockshow.R
import com.example.android.stockshow.databinding.DetailsFragmentBinding

class DetailsFragment : Fragment(R.layout.details_fragment) {

    private lateinit var detailsFragmentBinding: DetailsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsFragmentBinding = DetailsFragmentBinding.bind(view)


    }
}