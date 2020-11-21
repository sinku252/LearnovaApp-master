package com.sambhav.tws.utils.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State
        ) {
            if(parent.getChildAdapterPosition(view) == 0){
                outRect.left = verticalSpaceHeight
            }
            outRect.right = verticalSpaceHeight
            outRect.bottom = verticalSpaceHeight
            outRect.top = verticalSpaceHeight
        }
    }