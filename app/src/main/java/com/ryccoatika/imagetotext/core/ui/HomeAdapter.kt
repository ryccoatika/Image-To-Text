package com.ryccoatika.imagetotext.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import kotlinx.android.synthetic.main.scanned_text_item.view.*

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeListViewHolder>() {

    private var listTextScanned = ArrayList<TextScanned>()

    private lateinit var onItemClick: (TextScanned) -> Unit
    private lateinit var onItemLongClick: (TextScanned) -> Boolean

    fun setOnItemClickListener(listener: (TextScanned) -> Unit) {
        this.onItemClick = listener
    }

    fun setOnItemLongClickListener(listener: (TextScanned) -> Boolean) {
        this.onItemLongClick = listener
    }

    fun setListTextScanned(listTextScanned: List<TextScanned>) {
        this.listTextScanned.clear()
        this.listTextScanned.addAll(listTextScanned)
        notifyDataSetChanged()
    }

    fun getTextScanned(position: Int): TextScanned = listTextScanned[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        return HomeListViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.scanned_text_item, parent, false)
        )
    }

    override fun getItemCount(): Int = listTextScanned.size

    inner class HomeListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindData(textScanned: TextScanned) {
            itemView.tv_text.text = textScanned.text

            itemView.setOnClickListener { onItemClick(textScanned) }
            itemView.setOnLongClickListener { onItemLongClick(textScanned) }
        }
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.bindData(listTextScanned[position])
    }
}