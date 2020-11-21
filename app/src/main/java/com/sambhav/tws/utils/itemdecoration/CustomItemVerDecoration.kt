package com.sambhav.tws.utils.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemVerDecoration(private val verticalSpaceHeight: Int) :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State
        ) {
            if(parent.getChildAdapterPosition(view) == 0){
                outRect.top = verticalSpaceHeight
            }
            outRect.bottom = verticalSpaceHeight
            outRect.right = verticalSpaceHeight
            outRect.left = verticalSpaceHeight
        }
    }