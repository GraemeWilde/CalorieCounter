package com.wilde.caloriecounter2.composables.other

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
import androidx.compose.ui.unit.dp
import com.wilde.caloriecounter2.R

/**
 * An object to represent the precedence of an action bar item for an [ActionsRow]. This is used by
 * the [ActionsRow] to determine which items will take up the space on the [ActionsRow] and which
 * should be moved into the overflow menu.
 */
sealed interface Priority {
    /**
     * The [Priority] that specifies that the item should always be in the overflow menu.
     */
    object InMoreSettings : Priority

    /**
     * The [Priority] that specifies that the item should always be shown on the action bar
     */
    class AlwaysShow(val priority: Int = 0): Priority

    /**
     * The [Priority] that specifies that the item should be shown when there is no expanded item
     * (ex. no expanded search box)
     */
    class ShowWhenNoExpanded(val priority: Int = 0): Priority

    /**
     * The [Priority] that specifies that the item should be shown if there is space on the action
     * bar
     */
    class IfSpace(val priority: Int = 0): Priority
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

/**
 * Used to set a title for an ActionBar item. Title can either be a string, or a string resource.
 */
sealed interface StringLike {
    @Composable
    fun asString(): kotlin.String

    /**
     * A string
     */
    class String(val value: kotlin.String) : StringLike {
        @Composable
        override fun asString(): kotlin.String {
            return value
        }
    }

    /**
     * A string resource
     */
    class Resource(val value: Int) : StringLike {
        @Composable
        override fun asString(): kotlin.String {
            return stringResource(value)
        }
    }
}

/**
 * Receiver scope for [ActionsRow], used to create the different types of action buttons that
 * can be used in an [ActionsRow]
 */
interface ActionsScope {
    /**
     * DSL function to create an action bar button that will execute the provided callback when
     * clicked
     *
     * @param icon A composable that will be shown as the icon in the actionbar/overflow menu
     *
     * @param title The title for the action button. Used when displayed in the overflow menu.
     *
     * @param priority The [Priority] is used to determine which buttons there is enough space for
     * on the action bar, and which should be moved into the overflow menu
     *
     * @param onClick The callback to execute when the button is clicked
     */
    fun actionButton(
        icon: @Composable () -> Unit,
        title: StringLike,
        priority: Priority,
        onClick: () -> Unit
    )

    /**
     * DSL function to create an action bar button that will expand into a search textbox when
     * clicked. After the user enters text and starts a search, the provided callback is executed.
     *
     * Note: There can only be one expanded item expanded at a time, and it always shows at the
     * start of the action bar.
     *
     * @param icon A composable that will be shown as the icon in the actionbar/overflow menu
     *
     * @param title The title for the action button. Used when displayed in the overflow menu.
     *
     * @param priority The [Priority] is used to determine which buttons there is enough space for
     * on the action bar, and which should be moved into the overflow menu
     *
     * @param onSearch The callback to execute when the a search is requested
     */
    fun actionSearchable(
        icon: @Composable () -> Unit,
        title: StringLike,
        priority: Priority,
        onSearch: (String) -> Unit
    )
}

/**
 * Implementation of the [ActionsScope] for the [ActionsRow]. Should only be used internally by
 * [ActionsRow]
 */
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

/**
 * Composable to create a responsive action bar that will dynamically change which items are shown
 * on the action bar based on the amount of space available.
 *
 * @param buttons DSL callback where the buttons that should be on the action bar and in the
 * overflow more menu are defined.
 */
@Composable
fun ActionsRow(
    buttons: ActionsScope.() -> Unit
) {
    val expanded: MutableState<ActionSearchable?> = remember { mutableStateOf<ActionSearchable?>(null) }
    val moreOpen = remember{ mutableStateOf(false) }
    val actions by remember { derivedStateOf {
        ActionsScopeImpl().apply(buttons).actions
    }}

    SubcomposeLayout() { constraints ->

        val inInMoreSettings = ArrayList<Int>(actions.size)
        val inAlwaysShow = ArrayList<Int>(actions.size)
        val inShowWhenNoExpanded = ArrayList<Int>(actions.size)
        val inIfSpace = ArrayList<Int>(actions.size)

        var searchableExists = false

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
                                    .weight(1f, false),
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
        } else {
            null
        }


        val placeables = ArrayList<Pair<Placeable, Int>>(actions.size)
        val inSettingsMeasurables = ArrayList<Pair<Measurable, Int>>(actions.size)
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
                inAlwaysShow.iterator(),
                inIfSpace.iterator()
            ).iterator()
        }

        val settingsMenuIterators = if (expandedMeasurable == null) {
            listOf(
                inInMoreSettings.iterator()
            ).iterator()
        } else {
            listOf(
                inShowWhenNoExpanded.iterator(),
                //inIfSpace.iterator(),
                inInMoreSettings.iterator()
            ).iterator()
        }

        // List of all items in overflow menu
        val moreSettings = ArrayList<Int>(actions.size)

        // Calculate which actions fit on action bar, and which must be pushed into overflow
        while (actionBarIterators.hasNext()) {
            val iterator = actionBarIterators.next()
            while (iterator.hasNext()) {
                val curr = iterator.next()
                val action = actions[curr]
                ++pos

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
                    }
                }
                if (restInSettings) {
                    moreSettings.add(curr)
                }
            }
        }

        // Populate actions that are for sure in overflow (because of configuration)
        while (settingsMenuIterators.hasNext()) {
            val iterator = settingsMenuIterators.next()
            while (iterator.hasNext()) {
                restInSettings = true

                val curr = iterator.next()

                moreSettings.add(curr)
            }
        }

        // Sort action bar items back into order they were defined in
        placeables.sortBy {
            it.second
        }
        // Sort overflow menu items back into order they were defined in
        inSettingsMeasurables.sortBy {
            it.second
        }


        // If there are overflow menu items
        if (restInSettings) {
            width += settingsMenu.width

            if (settingsMenu.height > height) {
                height = settingsMenu.height
            }
        }

        // Create overflow menu items
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
        }[0].measure(constraints)


        layout(width, height) {
            var placeX = expandedMeasurable?.width ?: 0

            expandedMeasurable?.placeRelative(0, (height - expandedMeasurable.height) / 2)

            placeables.forEach {
                it.first.placeRelative(placeX, (height - it.first.height) / 2)
                placeX += it.first.width
            }
            if (restInSettings) {
                settingsMenu.placeRelative(placeX, (height - settingsMenu.height) / 2)
            }
            if (moreOpen.value) {
                moreSettingsMeasurable.placeRelative(placeX, (height - settingsMenu.height) / 2)
            }
        }
    }
}