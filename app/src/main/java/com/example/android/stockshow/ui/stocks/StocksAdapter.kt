package com.example.android.stockshow.ui.stocks

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.StockItemBinding
import kotlin.math.abs

class StocksAdapter() : ListAdapter<StockItem, StocksAdapter.StockViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.ticker == newItem.ticker
        }
    }

    inner class StockViewHolder(private val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(stockItem: StockItem) {
            binding.run {
                companyNameTv.text = stockItem.name
                tickerTv.text = stockItem.ticker
                currentPriceTv.text = "${stockItem.latestPrice} P"

                Glide.with(binding.logoIv.context)
                    .load(stockItem.logo)
                    .into(binding.logoIv)

                val stockDelta = stockItem.latestPrice - stockItem.previousClose
                val stockRisePercent = abs(stockDelta) / stockItem.previousClose * 100
                if (stockDelta < 0) {
                    currentPriceTv.text = "-$$stockDelta ($stockRisePercent%)"
                    currentPriceTv.setTextColor(Color.RED)
                } else {
                    currentPriceTv.text = "+$$stockDelta ($stockRisePercent%)"
                    currentPriceTv.setTextColor(Color.GREEN)
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
        holder.bind(stock)
    }
}