package com.wilde.caloriecounter2.composables.other

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.wilde.caloriecounter2.R

@Composable
fun SearchTopAppBar(
    searchIcon: Painter,
    searchOpen: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    searchText: MutableState<String> = rememberSaveable { mutableStateOf("") },
    onOpen: (() -> Unit)?,
    onSearch: (String) -> Unit,
) {
    //var searchOpen by rememberSaveable { mutableStateOf(false) }
    //var requestFocus by remember { mutableStateOf(false) }
    Log.d("SearchTopAppBar", "Compose")

    if (searchOpen.value) {
        //var searchText by rememberSaveable { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }

        Row(
            Modifier
                .padding(4.dp)
                .height(IntrinsicSize.Min)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .padding(8.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //val test = VectorPainter
            //val searchIcon = painterResource(R.drawable.search_internet)
            Icon(
                searchIcon,
                stringResource(id = R.string.search_openfoodfacts_hint),
                /*Modifier
                    .width(searchIcon.intrinsicSize.width.dp)
                    .height(searchIcon.intrinsicSize.height.dp)*/
                //Modifier.fillMaxHeight()
            )
            val text = @Composable {
                with(LocalDensity.current) {
                    BasicTextField(
                        value = searchText.value,
                        onValueChange = { searchText.value = it },
                        Modifier
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = { onSearch(searchText.value) }
                        ),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.dp.toSp())
                    )
                }
            }
            text()
            Box(
                Modifier.size(24.dp)
            ) {
                IconButton(
                    onClick = {
                        if (searchText.value.isEmpty()) {
                            searchOpen.value = false
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
        LaunchedEffect(searchOpen.value) {
            if (searchOpen.value) {
                focusRequester.requestFocus()
            }
        }

    } else {
        IconButton(onClick = {
            onOpen?.invoke()
            searchOpen.value = true
        }) {
            Icon(
                //painterResource(id = R.drawable.search_internet),
                searchIcon,
                stringResource(id = R.string.search_openfoodfacts_hint)
            )
        }
    }
}