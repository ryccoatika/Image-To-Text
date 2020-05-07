package com.ryccoatika.imagetotext.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ryccoatika.imagetotext.R

abstract class ItemSwipeShowMenu(private val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (dX == 0f) {
            c.drawColor(Color.WHITE)
            return
        }

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // add background color
        val rect =  RectF(
            (itemView.right + dX.toInt() - 40).toFloat(),
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat())

        val paint = Paint()
        paint.color = Color.parseColor("#f44336")
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL

        c.drawRoundRect(rect, 25f, 25f, paint)

        // add delete button
        val deleteIcon = context.getDrawable(R.drawable.ic_delete_black_24dp)
        deleteIcon?.let { icon ->
            val deleteIconTop = itemView.top + (itemHeight - icon.intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - icon.intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - icon.intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + icon.intrinsicHeight

            icon.setTint(Color.parseColor("#ffffff"))
            icon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            icon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }
}