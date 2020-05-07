package com.ryccoatika.imagetotext.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.scannedtext.ScannedTextActivity
import com.ryccoatika.imagetotext.db.TextScannedEntity
import kotlinx.android.synthetic.main.list_item.view.*

class HomeListAdapter : RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>() {

    var textScannedList = listOf<TextScannedEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        return HomeListViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int = textScannedList.count()

    class HomeListViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindData(textScanned: TextScannedEntity) {
            itemView.tv_text_scanned.text = textScanned.text

            itemView.setOnClickListener {
                val intentScannedText = Intent(itemView.context, ScannedTextActivity::class.java)
                intentScannedText.putExtra(ScannedTextActivity.EXTRA_TEXT_SCANNED, textScanned)
                itemView.context.startActivity(intentScannedText)
            }
            itemView.setOnLongClickListener { 
                val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Scanned Text", textScanned.text))
                Snackbar.make(itemView, itemView.context.getString(R.string.text_copied), Snackbar.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.bindData(textScannedList[position])
    }
}