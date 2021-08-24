package com.wilde.caloriecounter2.flow

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.solver.widgets.ConstraintWidget
import androidx.constraintlayout.solver.widgets.HelperWidget
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.VirtualLayout

class MyFlow : Flow {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun loadParameters(
        constraint: ConstraintSet.Constraint?,
        child: HelperWidget?,
        layoutParams: ConstraintLayout.LayoutParams?,
        mapIdToWidget: SparseArray<ConstraintWidget>?
    ) {
        super.loadParameters(constraint, child, layoutParams, mapIdToWidget)
    }

    class MyFlowLayoutParams : ConstraintLayout.LayoutParams {
        constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

        constructor(width: Int, height: Int) : super(width, height)

        constructor(source: ViewGroup.LayoutParams) : super(source)
    }

}