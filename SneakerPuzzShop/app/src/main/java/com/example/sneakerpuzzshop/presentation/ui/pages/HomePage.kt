package com.example.sneakerpuzzshop.presentation.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import com.example.sneakerpuzzshop.presentation.components.home.BannerView
import com.example.sneakerpuzzshop.presentation.components.home.CategoryView
import com.example.sneakerpuzzshop.presentation.components.home.HeaderView

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        HeaderView(modifier)

        // Spacer(modifier = Modifier.height(10.dp))
        // SearchBarItem()

        Spacer(modifier = Modifier.height(20.dp))
        CategoryView(modifier)
        BannerView(modifier = Modifier.height(200.dp))
    }
}