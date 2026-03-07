package com.example.presentation.screens.ui.main.plans

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Dados de exemplo ---
data class Plan(
    val title: String,
    val monthlyLabel: String,
    val detail: String,
    val monthly: Double,
    val yearly: Double
)

data class PointPackage(
    val title: String,
    val points: Int,
    val price: Double
)

// --- Screen principal com tabs ---
@Composable
fun AppPlansScreen(
    navigateToProfile: () -> Unit = {},
    userPoints: Int = 1200 // exemplo de saldo
) {
    val cs = MaterialTheme.colorScheme

    val tabs = listOf("Plans", "Points")
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    val plans = remember {
        listOf(
            Plan("Basic", "$5.99 / month", "Save 15% • +150 pts monthly", monthly = 5.99, yearly = 4.99),
            Plan("Performance", "$12.99 / month", "Save 15% • +400 pts monthly", monthly = 12.99, yearly = 9.99),
            Plan("Elite", "$24.99 / month", "Save 30% • +1500 pts monthly", monthly = 24.99, yearly = 19.99)
        )
    }

    val pointPackages = remember {
        listOf(
            PointPackage("Bronze Pack", 500, 4.99),
            PointPackage("Silver Pack", 1200, 9.99),
            PointPackage("Gold Pack", 3000, 19.99)
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(cs.background, cs.surface))),
        containerColor = cs.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigateToProfile() }) {
                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Back", tint = cs.onSurface)
                }
                Spacer(Modifier.width(6.dp))
                Text(
                    "Upgrade to unlock everything",
                    color = cs.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        bottomBar = {
            if (selectedTab == 0) {
                Button(
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.primary,
                        contentColor = cs.onPrimary
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Confirm Selection", fontWeight = FontWeight.Bold)
                }
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                // TabRow
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = cs.primary
                        )
                    }
                ) {
                    tabs.forEachIndexed { i, t ->
                        Tab(
                            selected = selectedTab == i,
                            onClick = { selectedTab = i },
                            text = {
                                Text(
                                    t,
                                    color = if (selectedTab == i) cs.primary else cs.onSurface.copy(alpha = 0.75f)
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Conteúdo condicional por aba
                when (selectedTab) {
                    0 -> PlansContent(plans = plans, onSelect = { /* disparar fluxo compra/seleção */ })
                    1 -> PointsContent(userPoints = userPoints, packages = pointPackages, onBuy = { pkg ->
                        /* fluxo de compra de pts */
                    })
                }
            }
        }
    )
}

// --- Conteúdo da aba "Plans" ---
@Composable
fun PlansContent(plans: List<Plan>, onSelect: (Plan) -> Unit) {
    // estado local de seleção (pode vir do ViewModel também)
    var selectedTitle by rememberSaveable { mutableStateOf<String?>(null) }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            // features / bullets summarizadas
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
                        Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(feat, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        items(plans) { plan ->
            PlanCard(
                plan = plan,
                isSelected = selectedTitle == plan.title,
                onSelect = {
                    selectedTitle = plan.title
                    onSelect(plan)
                }
            )
        }

        item {
            Spacer(Modifier.height(80.dp)) // para dar espaço com o bottom bar
        }
    }
}

// --- Conteúdo da aba "Points" ---
@Composable
fun PointsContent(userPoints: Int, packages: List<PointPackage>, onBuy: (PointPackage) -> Unit) {
    val cs = MaterialTheme.colorScheme

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Your balance", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("$userPoints pts", color = cs.primary, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Spacer(Modifier.height(12.dp))
            Text("Buy more points to unlock features and boosts.")
            Spacer(Modifier.height(12.dp))
        }

        items(packages) { pkg ->
            Card(
                colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBuy(pkg) }
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pkg.title, fontWeight = FontWeight.Bold, color = cs.onSurface)
                        Spacer(Modifier.height(6.dp))
                        Text("${pkg.points} pts", color = cs.onSurface.copy(alpha = 0.8f))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("$${pkg.price}", fontWeight = FontWeight.SemiBold, color = cs.onSurface)
                        Spacer(Modifier.height(6.dp))
                        Button(onClick = { onBuy(pkg) }, shape = RoundedCornerShape(6.dp)) {
                            Text("Buy")
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(80.dp))
        }
    }
}

// --- Card do plano (apresenta seleção visual) ---
@Composable
fun PlanCard(plan: Plan, isSelected: Boolean = false, onSelect: (Plan) -> Unit) {
    val cs = MaterialTheme.colorScheme
    val container = if (isSelected) cs.primary.copy(alpha = 0.12f) else cs.surfaceVariant

    Card(
        colors = CardDefaults.cardColors(containerColor = container),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(plan) }
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(plan.title, color = cs.onSurface, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(plan.monthlyLabel, color = cs.primary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(plan.detail, color = cs.onSurface.copy(alpha = 0.75f))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${plan.yearly} / year", color = cs.onSurface, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("${plan.monthly} / month", color = cs.onSurface.copy(alpha = 0.75f))
            }
        }
    }
}