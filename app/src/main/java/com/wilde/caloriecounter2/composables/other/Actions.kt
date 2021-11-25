package com.wilde.caloriecounter2.composables.other

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.wilde.caloriecounter2.R

sealed interface Priority {
    object InMoreSettings : Priority
    class AlwaysShow(val priority: Int = 0): Priority
    class ShowWhenNoExpanded(val priority: Int = 0): Priority
    class IfSpace(val priority: Int = 0): Priority
}

sealed interface PriorityId {
    object MoreSettingsButton : PriorityId
    class Expanded(val hideOthers: Boolean = true) : PriorityId
}


private interface Action {
    val icon: @Composable () -> Unit
    val title: StringLike
    val priority: Priority
}

private class ActionButton(
    override val icon: @Composable () -> Unit,
    override val title: StringLike,
    override val priority: Priority,
    val onClick: () -> Unit
) : Action

private class ActionSearchable(
    override val icon: @Composable () -> Unit,
    override val title: StringLike,
    override val priority: Priority,
    val onSearch: (String) -> Unit
) : Action


//@Composable
//fun ExpandedSearch() {
//    with(LocalDensity.current) {
//        BasicTextField(
//            value = searchText.value,
//            onValueChange = { searchText.value = it },
//            Modifier
//                .focusRequester(focusRequester)
//                .weight(1f, false)
//            ,
//            keyboardOptions = KeyboardOptions(
//                imeAction = ImeAction.Search,
//            ),
//            keyboardActions = KeyboardActions(
//                onSearch = { onSearch(searchText.value) }
//            ),
//            singleLine = true,
//            textStyle = LocalTextStyle.current.copy(fontSize = 18.dp.toSp())
//        )
//    }
//}

sealed interface StringLike {
    @Composable
    fun asString(): kotlin.String

    class String(val value: kotlin.String) : StringLike {
        @Composable
        override fun asString(): kotlin.String {
            return value
        }
    }

    class Resource(val value: Int) : StringLike {
        @Composable
        override fun asString(): kotlin.String {
            return stringResource(value)
        }
    }
}

interface ActionsScope {
    fun actionButton(
        icon: @Composable () -> Unit,
        title: StringLike,
        priority: Priority,
        onClick: () -> Unit
    )

    fun actionSearchable(
        icon: @Composable () -> Unit,
        title: StringLike,
        priority: Priority,
        onSearch: (String) -> Unit
    )
}

private class ActionsScopeImpl : ActionsScope {
    var actions: MutableList<Action> = mutableListOf()

    override fun actionButton(
        icon: @Composable () -> Unit,
        title: StringLike,
        priority: Priority,
        onClick: () -> Unit
    ) {
        actions.add(ActionButton(icon, title, priority, onClick))
    }

    override fun actionSearchable(
        icon: @Composable () -> Unit,
        title: StringLike,
        priority: Priority,
        onSearch: (String) -> Unit
    ) {
        actions.add(ActionSearchable(icon, title, priority, onSearch))
    }
}

//@Composable
//fun actions(
//    actions: @Composable ActionsScope.() -> Unit
//): List<Action> {
//
//    val aS = ActionsScopeImpl()
//
//    actions(aS)
//
//    return aS.actions
//}

@Composable
fun ActionsRow(
    modifier: Modifier = Modifier,
    actionScope: ActionsScope.() -> Unit
) {
    Log.d("ActionsRow", "Start")

    val expanded: MutableState<ActionSearchable?> = remember { mutableStateOf<ActionSearchable?>(null) }
    val moreOpen = remember{ mutableStateOf(false) }
    val actions by remember { derivedStateOf {
        ActionsScopeImpl().apply(actionScope).actions
    }}

    SubcomposeLayout() { constraints ->

//        val inInMoreSettings = ArrayList<Action>(actions.size)
//        val inAlwaysShow = ArrayList<Action>(actions.size)
//        val inShowWhenNoExpanded = ArrayList<Action>(actions.size)
//        val inIfSpace = ArrayList<Action>(actions.size)


        val inInMoreSettings = ArrayList<Int>(actions.size)
        val inAlwaysShow = ArrayList<Int>(actions.size)
        val inShowWhenNoExpanded = ArrayList<Int>(actions.size)
        val inIfSpace = ArrayList<Int>(actions.size)


        //var expanded: ActionSearchable? = null

        var searchableExists = false

//        actions.mapIndexed { i: Int, action: Action ->
//            object {
//                val origKey = i
//                val value = action
//            }
//        }

        actions.forEachIndexed { index, action ->
            if (action == expanded.value) {
                 searchableExists = true
            } else {
                when (action.priority) {
                    is Priority.InMoreSettings -> inInMoreSettings.add(index)
                    is Priority.AlwaysShow -> inAlwaysShow.add(index)
                    is Priority.ShowWhenNoExpanded -> inShowWhenNoExpanded.add(index)
                    is Priority.IfSpace -> inIfSpace.add(index)
                }
            }
        }

        if (!searchableExists) {
            expanded.value = null
        }

        inAlwaysShow.sortBy {
            (actions[it].priority as Priority.AlwaysShow).priority
        }
        inShowWhenNoExpanded.sortBy {
            (actions[it].priority as Priority.ShowWhenNoExpanded).priority
        }
        inIfSpace.sortBy {
            (actions[it].priority as Priority.IfSpace).priority
        }


        var width = 0
        var height = 0

        val settingsMenu = subcompose("Settings") {
            IconButton(onClick = { moreOpen.value = !moreOpen.value }) {
                Icon(Icons.Filled.MoreVert, "More")
            }
        }[0].measure(constraints)

        val expandedMeasurable = if (expanded.value != null) {
            subcompose("Expanded") {
                if (expanded.value != null) {
                    Row(
                        Modifier
                            .padding(4.dp)
                            .height(IntrinsicSize.Min)
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        expanded.value!!.icon()
                        val searchText = remember { mutableStateOf("") }
                        val focusRequester = remember { FocusRequester() }

                        with(LocalDensity.current) {
                            BasicTextField(
                                value = searchText.value,
                                onValueChange = { searchText.value = it },
                                Modifier
                                    .focusRequester(focusRequester)
                                    .weight(1f, false)
                                ,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Search,
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = { expanded.value!!.onSearch(searchText.value) }
                                ),
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(fontSize = 18.dp.toSp())
                            )
                        }
                        LaunchedEffect(expanded.value) {
                            focusRequester.requestFocus()
                        }
                        Box(
                            Modifier.size(24.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (searchText.value.isEmpty()) {
                                        //searchOpen.value = false
                                        expanded.value = null
                                    } else {
                                        searchText.value = ""
                                    }
                                },
                                Modifier.requiredSize(48.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    stringResource(id = R.string.close),
                                    Modifier
                                        .width(Icons.Filled.Close.defaultWidth)
                                        .height(Icons.Filled.Close.defaultHeight)
                                )
                            }
                        }
                    }
                }
            }[0].measure(constraints).also {
                width += it.width
                if (it.height > height) {
                    height = it.height
                }
            }
        } else { null }


        val placeables = ArrayList<Pair<Placeable, Int>>(actions.size)
        val inSettingsMeasurables = ArrayList<Pair<Measurable, Int>>(actions.size)
        //val measurables = MutableList<Measurable?>(actions.size) { null }
        val size = inAlwaysShow.size + inShowWhenNoExpanded.size + inIfSpace.size
        var pos = -1
        var restInSettings = false

        val actionBarIterators = if (expandedMeasurable == null) {
            listOf(
                inAlwaysShow.iterator(),
                inShowWhenNoExpanded.iterator(),
                inIfSpace.iterator()
            ).iterator()
        } else {
            listOf(
                inAlwaysShow.iterator()
            ).iterator()
        }

        val settingsMenuIterators = if (expandedMeasurable == null) {
            listOf(
                inInMoreSettings.iterator()
            ).iterator()
        } else {
            listOf(
                inShowWhenNoExpanded.iterator(),
                inIfSpace.iterator(),
                inInMoreSettings.iterator()
            ).iterator()
        }
//            listOf(
//            inAlwaysShow.iterator(),
//            inShowWhenNoExpanded.iterator(),
//            inIfSpace.iterator()
//        ).iterator()

        val moreSettings = ArrayList<Int>(actions.size)

        while (actionBarIterators.hasNext()) {
            val iterator = actionBarIterators.next()
            while (iterator.hasNext()) {
                val curr = iterator.next()
                val action = actions[curr]
                ++pos

                //Log.d("ActionsRow", curr.title)

                if (!restInSettings) {
                    val measurable = subcompose(action.title) {
                        val onClick: () -> Unit = when (action) {
                            is ActionSearchable -> {
                                { expanded.value = action }
                            }
                            is ActionButton -> {
                                action.onClick
                            }
                            else -> {
                                {}
                            }
                        }
                        IconButton(onClick = onClick) {
                            action.icon()
                        }
                    }[0]

                    val mWidth = measurable.maxIntrinsicWidth(constraints.maxHeight)

                    if (width + mWidth + settingsMenu.width < constraints.maxWidth || (pos == size - 1 && inInMoreSettings.size == 0 && width + mWidth < constraints.maxWidth)) {
                        val placeable =
                            measurable.measure(constraints.copy(maxWidth = constraints.maxWidth - width))
                        placeables.add(Pair(placeable, curr))
                        width += placeable.width
                        if (placeable.height > height) {
                            height = placeable.height
                        }
                    } else {
                        restInSettings = true
                        //break@loop
                    }
                }
                if (restInSettings) {
//                    val measurable = subcompose(action.title) {
//                        Row {
//                            action.icon()
//                            Text(action.title)
//                        }
//                    }[0]
//                    inSettingsMeasurables.add(Pair(measurable, curr))
                    moreSettings.add(curr)
                }
            }
        }


        while (settingsMenuIterators.hasNext()) {
            val iterator = settingsMenuIterators.next()
            while (iterator.hasNext()) {
                restInSettings = true

                val curr = iterator.next()
//                val action = actions[curr]
//
//                val measurable = subcompose(action.title) {
//
//                    Row {
//                        action.icon()
//                        Text(action.title, maxLines = 1)
//                    }
//                }[0]

                moreSettings.add(curr)

                //inSettingsMeasurables.add(Pair(measurable, curr))
            }
        }



//        if (inInMoreSettings.size != 0) {
//            restInSettings = true
//
//            inInMoreSettings.forEach { curr ->
//                val action = actions[curr]
//                val measurable = subcompose(action.title) {
//                    Row {
//                        action.icon()
//                        Text(action.title)
//                    }
//                }[0]
//                inSettingsMeasurables.add(Pair(measurable, curr))
//            }
//        }

        // Sort action bar items back into order they were defined in
        placeables.sortBy {
            it.second
        }
        // Sort action bar more menu items back into order they were defined in
        inSettingsMeasurables.sortBy {
            it.second
        }

        var moreMenuWidth = 0
        inSettingsMeasurables.map {
            moreMenuWidth += it.first.maxIntrinsicWidth(Int.MAX_VALUE)
        }



//        val place = subcompose("Settings") {
//            IconButton(
//                onClick = { /*TODO*/ },
//                Modifier.layoutId(PriorityId.MoreSettingsButton)
//            ) {
//                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More Actions")
//            }
//        }[0].measure(constraints)


        if (restInSettings) {
            width += settingsMenu.width

            if (settingsMenu.height > height) {
                height = settingsMenu.height
            }
        }

        val moreSettingsMeasurable = subcompose("moreSettingsMenu") {
            Box(
                Modifier.requiredSize(settingsMenu.width.toDp(), settingsMenu.height.toDp())
            ) {
                DropdownMenu(
                    expanded = moreOpen.value,
                    onDismissRequest = { moreOpen.value = false },

                ) {
                    moreSettings.forEach {
                        val action = actions[it]
                        DropdownMenuItem(onClick = {
                            moreOpen.value = false
                            when (action) {
                                is ActionSearchable -> expanded.value = action
                                is ActionButton -> action.onClick()
                            }
                        }) {
                            actions[it].icon()
                            Text(actions[it].title.asString(), maxLines = 1)
                        }
                    }
                }
            }
        }[0]?.measure(constraints)



        //Log.d("ActionsRow", "Settings Size: ${moreSettingsMeasurable.size}")

        //Log.d("ActionsRow", "PreLayout")
        layout(width, height) {
            //Log.d("ActionsRow", "Layout: $width $height")
            var placeX = expandedMeasurable?.width ?: 0

            if (expandedMeasurable != null) {
                //Log.d("ActionsRow", "Expandable ${expandedMeasurable.width} ${expandedMeasurable.height}")
                expandedMeasurable.placeRelative(0, (height - expandedMeasurable.height) / 2)
            } /*else {
                Log.d("ActionsRow", "No Expandable")
            }*/

            placeables.forEach {
                //Log.d("ActionsRow", "placeable: ${it.width} ${it.height} $placeX ${(height - it.height) / 2}")
                it.first.placeRelative(placeX, (height - it.first.height) / 2)
                placeX += it.first.width
            }
            if (restInSettings) {
                //Log.d("ActionsRow", "More Settings")
                Log.d("ActionsRow", "setButton layout: $placeX ${settingsMenu.width} ${settingsMenu.height}")
                settingsMenu.placeRelative(placeX, (height - settingsMenu.height) / 2)
            }
            if (moreOpen.value) {
                Log.d("ActionsRow", "setMenu layout: $placeX ${settingsMenu.width} ${settingsMenu.height} ${moreSettingsMeasurable.measuredWidth} ${moreSettingsMeasurable.measuredHeight}")
                Log.d("ActionsRow", "setMenu layout calc: ${placeX + settingsMenu.width - moreSettingsMeasurable.width} ${(height - settingsMenu.height) / 2 + settingsMenu.height}")
                //moreSettingsMeasurable.placeRelative(placeX + settingsMenu.width - moreSettingsMeasurable.width, (height - settingsMenu.height) / 2 + settingsMenu.height)
                moreSettingsMeasurable.placeRelative(placeX, (height - settingsMenu.height) / 2)
                //moreSettingsMeasurable.
            }
        }
    }
}