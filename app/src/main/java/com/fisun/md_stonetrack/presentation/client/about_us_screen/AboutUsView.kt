package com.fisun.md_stonetrack.presentation.client.about_us_screen

import AppFontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fisun.md_stonetrack.R
import com.fisun.md_stonetrack.presentation.navigation.BottomNavigationBar

@Composable
fun AboutUsView(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.purple))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "На главную",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "На главную",
                        fontFamily = AppFontFamily,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.logo_home),
                    contentDescription = "Логотип",
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(colorResource(id = R.color.light_gray))
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text(text = "О компании MD-STONE", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = stringResource(R.string.AboutUs_1),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                    )

                    Column(modifier = Modifier.padding(start = 8.dp, bottom = 32.dp)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list1_1), fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list1_2), fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list1_3), fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list1_4), fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list1_5), fontSize = 18.sp)
                    }

                    Text(
                        text = stringResource(R.string.AboutUs_2),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 32.dp)
                    )

                    Text(
                        text = stringResource(R.string.AboutUs_3),
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(id = R.color.purple),
                        fontSize = 22.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 16.dp),
                        fontWeight = FontWeight.Bold
                    )

                    Column(modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list2_1), fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list2_2), fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list2_3), fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.AboutUs_list2_4), fontSize = 18.sp)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .background(colorResource(id = R.color.purple))
        ) {
            BottomNavigationBar(navController, selected = "about_us_screen")
        }
    }
}