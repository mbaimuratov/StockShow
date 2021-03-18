package com.example.android.stockshow.ui.stocks

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.StockItemBinding
import kotlin.math.abs

class StocksAdapter : ListAdapter<StockItem, StocksAdapter.StockViewHolder>(DiffCallback) {

    inner class StockViewHolder(private val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(stockItem: StockItem, position: Int) {
            binding.run {
                companyNameTv.text = stockItem.name
                tickerTv.text = stockItem.ticker
                currentPriceTv.text = "$%.2f ".format(stockItem.latestPrice)

                Glide.with(binding.logoIv.context)
                    .load(stockItem.logo)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(binding.logoIv)

                val stockDelta = stockItem.latestPrice - stockItem.previousClose
                val stockRisePercent = abs(stockDelta) / stockItem.previousClose * 100
                if (stockDelta < 0) {
                    dayDeltaTv.setTextColor(Color.RED)
                    dayDeltaTv.text = "-%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                } else {
                    dayDeltaTv.text = "+%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                    dayDeltaTv.setTextColor(Color.GREEN)
                }

                if (position % 2 == 0) {
                    root.setBackgroundColor(Color.parseColor("#F0F4F7"))
                } else {
                    root.setBackgroundColor(Color.parseColor("#ffffff"))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = getItem(position)
        holder.bind(stock, position)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.ticker == newItem.ticker
        }
    }
}