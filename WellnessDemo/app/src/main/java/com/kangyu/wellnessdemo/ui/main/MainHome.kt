package com.kangyu.wellnessdemo.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.carevoice.cvandroid.navigation.AppComposeNavigator
import com.carevoice.cvdesign.designsystem.modifier.containWindowInseTop
import com.carevoice.cvdesign.designsystem.theme.CommonTheme
import com.carevoice.mindfulnesslibrary.WellnessTool
import com.carevoice.mindfulnesslibrary.bridgeview.WellnessMainScreen
//import com.carevoice.mindfulnesslibrary.navigation.WellnessAreaScreens
//import com.carevoice.mindfulnesslibrary.navigation.WellnessHubScreens
import kotlinx.coroutines.launch


// 底部导航数据类
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val index: Int
)

@Composable
fun MainHome(
    navHostController: NavHostController,
    composeNavigator: AppComposeNavigator,
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0, pageCount = { 3 })
    
    // 底部导航项目
    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, 0),
        BottomNavItem("Wellness", Icons.Filled.SelfImprovement, 1),
        BottomNavItem("Profile", Icons.Filled.Person, 2)
    )
    
    Column(modifier = Modifier.fillMaxSize().containWindowInseTop(false)) {
        // 主要内容区域
        Box(modifier = Modifier.weight(1f)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> HomeScreen()
                    1 -> WellnessScreen()
                    2 -> ProfileScreen()
                }
            }
        }
        
        // 底部导航栏
        BottomNavigationBar(
            items = bottomNavItems,
            currentPage = pagerState.currentPage,
            onItemClick = { index ->
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
    }
}

// 预览功能
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun WellnessScreenPreview() {
    MaterialTheme {
        WellnessScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, 0),
        BottomNavItem("Wellness", Icons.Filled.SelfImprovement, 1),
        BottomNavItem("Profile", Icons.Filled.Person, 2)
    )
    
    MaterialTheme {
        BottomNavigationBar(
            items = bottomNavItems,
            currentPage = 0,
            onItemClick = { }
        )
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 顶部欢迎文本
        Text(
            text = "Welcome to CareVoice",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3),
            modifier = Modifier.padding(top = 20.dp)
        )
        
        Text(
            text = "Start your wellness journey",
            fontSize = 16.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(20.dp))

        // Wellness SDK 入口卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.SelfImprovement,
                    contentDescription = "Wellness",
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Wellness SDK",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Enter the complete wellness management experience",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        // 启动 Wellness SDK
                        WellnessTool.startHubViewActivity(context)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enter Wellness SDK")
                }
            }
        }
    }
}

@Composable
fun WellnessScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WellnessMainScreen()
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9C27B0),
            modifier = Modifier.padding(top = 20.dp)
        )
        
        Text(
            text = "Manage your personal information and settings",
            fontSize = 16.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // 用户信息卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF9C27B0)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Username",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "mingxiang.luo@thecarevoice.com",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { /* TODO: 实现设置功能 */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Account Settings")
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentPage: Int,
    onItemClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                BottomNavButton(
                    item = item,
                    isSelected = currentPage == item.index,
                    onClick = { onItemClick(item.index) }
                )
            }
        }
    }
}

@Composable
fun BottomNavButton(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) Color(0xFF2196F3) else Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = item.title,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF2196F3) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}