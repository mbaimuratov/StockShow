package com.example.android.stockshow.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.stockshow.MainActivity
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.DetailsFragmentBinding
import com.example.android.stockshow.ui.models.DetailsViewModel
import com.example.android.stockshow.ui.models.SharedViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.math.abs


class DetailsFragment : Fragment(R.layout.details_fragment) {

    private lateinit var detailsFragmentBinding: DetailsFragmentBinding
    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var detailsViewModel: DetailsViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var currentResolutionButtonIndex = 4

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        val stockItem = args.stockItem

        (activity as MainActivity).supportActionBar?.title = stockItem.ticker
        (activity as MainActivity).supportActionBar?.subtitle = stockItem.name

        detailsViewModel.fetchCandles(stockItem, "M", 0)

        detailsFragmentBinding = DetailsFragmentBinding.bind(view)

        detailsFragmentBinding.apply {
            detailCurrentPriceTv.text = "$%.2f".format(stockItem.latestPrice)

            //calculate day delta and percent rise
            val stockDelta = stockItem.latestPrice - stockItem.previousClose
            val stockRisePercent = abs(stockDelta) / stockItem.previousClose * 100
            if (stockDelta < 0) {
                detailDayDeltaTv.setTextColor(Color.RED)
                detailDayDeltaTv.text = "-$%.2f (%.2f".format(-stockDelta, stockRisePercent) + "%)"
            } else {
                detailDayDeltaTv.text = "+$%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                detailDayDeltaTv.setTextColor(Color.GREEN)
            }

            buyBtn.text = "Buy for $%.2f".format(stockItem.latestPrice)
        }

        val resolutionButtonList = arrayListOf(
            detailsFragmentBinding.weekBtn,
            detailsFragmentBinding.monthBtn,
            detailsFragmentBinding.oneYearBtn,
            detailsFragmentBinding.fiveYearBtn,
            detailsFragmentBinding.allBtn,
        )

        setOnClickListenersToButtons(resolutionButtonList, stockItem)

        detailsViewModel.candle.observe(viewLifecycleOwner) { candle ->
            val entries = arrayListOf<Entry>()
            val closePriceList = candle.c
            var x = 0
            for (closePrice in closePriceList) {
                entries.add(Entry(x.toFloat(), closePrice.toFloat()))
                x += 10
            }
            val dataSet = LineDataSet(entries, stockItem.ticker)
            dataSet.setDrawCircles(false)
            dataSet.setDrawValues(false)
            dataSet.color = Color.BLACK
            dataSet.lineWidth = 3f

            dataSet.setDrawFilled(true)
            dataSet.fillDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.gradient_black)

            val lineData = LineData(dataSet)

            val lineChart = detailsFragmentBinding.lineChart

            //setting data
            lineChart.data = lineData
            lineChart.invalidate()

            //customizing
            lineChart.legend.isEnabled = false
            lineChart.setNoDataText("")
            lineChart.description.isEnabled = false
            lineChart.xAxis.isEnabled = false
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.isEnabled = false
        }
    }

    private fun setOnClickListenersToButtons(
        list: ArrayList<MaterialButton>,
        stockItem: StockItem
    ) {
        for (i in 0 until list.size) {
            list[i].setOnClickListener {
                if (currentResolutionButtonIndex != i) {

                    detailsViewModel.fetchCandles(
                        stockItem,
                        getResolution(i),
                        getFrom(i)
                    )

                    list[i].setTextColor(Color.WHITE)
                    list[i].backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.black
                    )

                    list[currentResolutionButtonIndex].setTextColor(Color.BLACK)
                    list[currentResolutionButtonIndex].backgroundTintList =
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.light_grey
                        )
                }
                currentResolutionButtonIndex = i
            }
        }
    }

    private fun getResolution(i: Int): String {
        return when (i) {
            0 -> "60"
            1 -> "60"
            2 -> "W"
            3 -> "M"
            4 -> "M"
            else -> "1"
        }
    }

    private fun getFrom(i: Int): Long {
        return when (i) {
            0 -> System.currentTimeMillis() / 1000 - (7 * 24 * 60 * 60)
            1 -> System.currentTimeMillis() / 1000 - (30 * 24 * 60 * 60)
            2 -> System.currentTimeMillis() / 1000 - (365 * 24 * 60 * 60)
            3 -> System.currentTimeMillis() / 1000 - (5 * 365 * 24 * 60 * 60)
            4 -> 0
            else -> -1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val stockItem = args.stockItem
        inflater.inflate(R.menu.detail_menu, menu)
        if (stockItem.isFavourite) {
            menu.findItem(R.id.action_add_to_favourite).icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_yellow_star)
        } else {
            menu.findItem(R.id.action_add_to_favourite).icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_border)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_to_favourite) {
            val stockItem = args.stockItem
            if (stockItem.isFavourite) {
                item.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_border)
            } else {
                item.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_yellow_star)
            }
            sharedViewModel.addOrRemoveFavourite(stockItem)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}