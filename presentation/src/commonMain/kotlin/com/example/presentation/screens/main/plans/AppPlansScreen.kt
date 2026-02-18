package com.example.presentation.screens.main.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.main.nutrition.TextPrimary
import com.example.presentation.screens.main.nutrition.TextSecondary
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.BaseGreen
import com.example.presentation.theme.DeepGreen
import com.example.presentation.theme.SurfaceGreen
import com.example.presentation.theme.transparent

@Composable
fun AppPlansScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onConfirm: (com.example.presentation.screens.main.plans.Plan) -> Unit = {}
) {
    val tabs = listOf("Plans", "Points", "Compare Plans")
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val plans = remember {
        listOf(
            _root_ide_package_.com.example.presentation.screens.main.plans.Plan(
                "Basic",
                "$5.99 / month",
                "Save 15% • +150 pts monthly",
                monthly = 5.99,
                yearly = 4.99
            ),
            _root_ide_package_.com.example.presentation.screens.main.plans.Plan(
                "Performance",
                "$12.99 / month",
                "Save 15% • +400 pts monthly",
                monthly = 12.99,
                yearly = 9.99
            ),
            _root_ide_package_.com.example.presentation.screens.main.plans.Plan(
                "Elite",
                "$24.99 / month",
                "Save 30% • +1500 pts monthly",
                monthly = 24.99,
                yearly = 19.99
            ),
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize().background(Brush.verticalGradient(listOf(BaseGreen, DeepGreen))),
        containerColor = transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(6.dp))
                Text("Upgrade to unlock everything", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                // tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = AccentGreen
                        )
                    }
                ) {
                    tabs.forEachIndexed { i, t ->
                        Tab(selected = selectedTab == i, onClick = { selectedTab = i }, text = { Text(t, color = if (selectedTab == i) AccentGreen else TextSecondary) })
                    }
                }

                Spacer(Modifier.height(16.dp))

                // bullet list of features
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        "Level & XP System with Daily Goals",
                        "Custom Meal Planning",
                        "Custom Workout Planning",
                        "Missions to Earn Points",
                        "Timed Progress Tracking",
                        "Goal Setting Builder"
                    ).forEach { feat ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(feat, color = TextPrimary)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // plans list
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    plans.forEach { plan ->
                        _root_ide_package_.com.example.presentation.screens.main.plans.PlanCard(
                            plan = plan,
                            onSelect = { onConfirm(it) })
                    }
                }

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = { /* default confirm last or open billing */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
                ) {
                    Text("Confirm Selection", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

data class Plan(
    val title: String,
    val monthlyLabel: String,
    val detail: String,
    val monthly: Double,
    val yearly: Double
)

@Composable
fun PlanCard(plan:Plan, onSelect: (Plan) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceGreen),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(plan) }
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(plan.title, color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(plan.monthlyLabel, color = AccentGreen, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(plan.detail, color = TextSecondary, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${plan.yearly} / year", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("${plan.monthly} / month", color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}

