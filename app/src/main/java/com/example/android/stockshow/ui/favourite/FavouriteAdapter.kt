package com.example.android.stockshow.ui.favourite

import android.annotation.SuppressLint
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.stockshow.data.response.Stock
import com.example.android.stockshow.databinding.StockItemBinding
import kotlin.math.abs


class FavouriteAdapter() :
    ListAdapter<Stock, FavouriteAdapter.StockViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem.ticker == newItem.ticker
        }
    }

    inner class StockViewHolder(private val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(stock: Stock) {
            binding.run {
                companyNameTv.text = stock.name
                tickerTv.text = stock.ticker
                currentPriceTv.text = "${stock.latestPrice} P"

                Glide.with(binding.logoIv.context)
                    .load(stock.logo)
                    .into(binding.logoIv)

                val stockDelta = stock.latestPrice - stock.previousClose
                val stockRisePercent = abs(stockDelta) / stock.previousClose * 100
                if (stockDelta < 0) {
                    currentPriceTv.text = "-$$stockDelta ($stockRisePercent%)"
                    currentPriceTv.setTextColor(RED)
                } else {
                    currentPriceTv.text = "+$$stockDelta ($stockRisePercent%)"
                    currentPriceTv.setTextColor(GREEN)
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