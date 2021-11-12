package com.wilde.caloriecounter2.composables.other

import android.util.Log
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import kotlin.math.min

//data class Priority(var priority: Int = 0)

interface PriorityRowScope {
    fun Modifier.priority(priority: Priority): Modifier
}

internal class PriorityImpl(val priority: Priority): ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) =
        priority
}

internal object PriorityRowScopeInstance: PriorityRowScope {
    override fun Modifier.priority(priority: Priority) = this.then(
        PriorityImpl(priority)
    )
}

//enum class PriorityId(val priority: Int?) {
//    MAXIMIZED(null),
//    MORE_SETTINGS_ICON(null),
//    IN_MORE_SETTINGS(null),
//    ALWAYS_SHOW(null),
//    SHOW_WHEN_NONE_MAXIMIZED(null),
//    IF_SPACE(null)
//}

//@Suppress("UNUSED")
//sealed interface Priority {
//    object InMoreSettings : Priority
//    class AlwaysShow(val priority: Int = 0): Priority
//    class ShowWhenNoExpanded(val priority: Int = 0): Priority
//    class IfSpace(val priority: Int = 0): Priority
//}
//
//sealed interface PriorityId {
//    object MoreSettingsButton : PriorityId
//    class Expanded(val hideOthers: Boolean = true) : PriorityId
//}

//sealed class PriorityId {
//    class MAXIMIZED: PriorityId()
//}

@Composable
fun PriorityRow(
    modifier: Modifier = Modifier,
    content: @Composable PriorityRowScope.() -> Unit
) {
    Layout(
        {
            IconButton(
                onClick = { /*TODO*/ },
                Modifier.layoutId(PriorityId.MoreSettingsButton)
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More Actions")
            }
            PriorityRowScopeInstance.content()
        },
        modifier
    ) { measurables: List<Measurable>, constraints: Constraints ->

        val priorities = arrayOfNulls<Priority>(measurables.size) //measurables.map { it.parentData as? Priority ?: Priority() }
        var maximized: Measurable? = null
        var settings: Measurable = measurables[0].apply { require(this.layoutId == PriorityId.MoreSettingsButton) }


        //IN_MORE_SETTINGS,
        //ALWAYS_SHOW,
        //SHOW_WHEN_NONE_MAXIMIZED,
        //IF_SPACE

        val inInMoreSettings = ArrayList<Measurable>(measurables.size)
        val inAlwaysShow = ArrayList<Measurable>(measurables.size)
        val inShowWhenNoExpanded = ArrayList<Measurable>(measurables.size)
        val inIfSpace = ArrayList<Measurable>(measurables.size)

        measurables.fastForEach { measurable ->
            when(measurable.parentData) {
                is Priority.InMoreSettings -> inInMoreSettings.add(measurable)
                is Priority.AlwaysShow -> inAlwaysShow.add(measurable)
                is Priority.ShowWhenNoExpanded -> inShowWhenNoExpanded.add(measurable)
                is Priority.IfSpace -> inIfSpace.add(measurable)
            }
        }

        inAlwaysShow.sortBy {
            (it.parentData as Priority.AlwaysShow).priority
        }
        inShowWhenNoExpanded.sortBy {
            (it.parentData as Priority.ShowWhenNoExpanded).priority
        }
        inIfSpace.sortBy {
            (it.parentData as Priority.IfSpace).priority
        }

        var width = 0
        var height = 0

        inAlwaysShow.forEach {

        }

        measurables.fastForEachIndexed { index: Int, measurable: Measurable ->
            when(measurable.layoutId) {
                is PriorityId.Expanded -> maximized = measurable
                //PriorityId.MORE_SETTINGS -> {}
                else -> {
                    priorities[index] = measurable.parentData as? Priority ?: Priority.IfSpace()
                    width += measurable.minIntrinsicWidth(constraints.maxHeight)
                    Log.d("PriorityRow", "Ind: $index - Width: ${measurable.minIntrinsicWidth(constraints.maxHeight)}")
                }
            }
        }


        // If maximized, measure that, otherwise measure all measurables. Also get max height.
        val placeables = maximized?.let {
            listOf(it.measure(constraints)).apply {
                height = this[0].height
                width = this[0].width
            }
        } ?: measurables.mapIndexed { index, it ->
            val placeable = it.measure(constraints)
            if (placeable.height > height) height = placeable.height

            Log.d("PriorityRow", "Ind: $index - Width: ${placeable.width} Height: ${placeable.height}")

            placeable
        }

        height = min(height, constraints.maxHeight)

        layout(width, height) {
            Log.d("PriorityRow", "layout: $width, $height")
            placeables.fastForEachIndexed { index, it ->
                width -= it.width
                it.placeRelative(width, (height - it.height) / 2)
                Log.d("PriorityRow", "Ind: $index - ${it.width}, ${it.height}}")
            }
        }
    }
}