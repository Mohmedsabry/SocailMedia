package com.example.socailmedia.presentation.compenent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.socailmedia.presentation.home.NavigationItem
import com.example.socailmedia.presentation.home.Screens
import com.example.socailmedia.presentation.home.navigationList
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun NavigationBarItems(
    selectedItem: Int = 0,
    list: List<NavigationItem> = navigationList,
    onClick: (item: NavigationItem) -> Unit
) {
    NavigationBar {
        list.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItem,
                onClick = {
                    onClick(item)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier =
                        Modifier.size(20.sdp),
                    )
                },
                label = {
                    Text(text = item.title.title, fontSize = 16.ssp)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xffE1204D),
                    selectedTextColor = Color(0xffE1204D),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavPrev() {
    Column {
        var item by remember {
            mutableStateOf(NavigationItem(Screens.HOME, 0))
        }
        NavigationBarItems {
            item = it
        }
        Text(text = "item ${item.title}")
    }
}