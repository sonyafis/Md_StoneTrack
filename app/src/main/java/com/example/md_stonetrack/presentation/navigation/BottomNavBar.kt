package com.example.md_stonetrack.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.md_stonetrack.R

data class BottomNavItem(val route: String, val icon: ImageVector? = null, val iconResId: Int? = null)

@Composable
fun BottomNavigationBar(navController: NavController, selected: String,  modifier: Modifier = Modifier ) {
    val items = listOf(
        BottomNavItem("orders_screen", iconResId = R.drawable.home),
        BottomNavItem("history_screen", iconResId = R.drawable.history),
//        BottomNavItem("about_screen", iconResId = R.drawable.about_us),
        BottomNavItem("feedback_screen", iconResId = R.drawable.feedback),
        BottomNavItem("profile_screen", iconResId = R.drawable.profile),
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(90.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.route == selected
                val iconTint = if (isSelected) colorResource(id = R.color.darkpurple) else colorResource(id = R.color.purple)

                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != item.route) {
                            navController.navigate(item.route)
                        }
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    if (item.iconResId != null) {
                        Icon(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = item.route,
                            tint = iconTint,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}