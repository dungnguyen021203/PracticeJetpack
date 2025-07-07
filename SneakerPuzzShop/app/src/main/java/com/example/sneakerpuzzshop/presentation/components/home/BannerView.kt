package com.example.sneakerpuzzshop.presentation.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sneakerpuzzshop.presentation.viewmodel.HomeViewModel
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun BannerView(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val banners by viewModel.banner.collectAsState()

    if(banners.isNotEmpty()) {
        Column(modifier = modifier) {
            val pager = rememberPagerState(0) { banners.size }
            HorizontalPager(
                state = pager,
                pageSpacing = 24.dp,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                AsyncImage(
                    model = banners[it],
                    contentDescription = "Banner",
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))

                )
            }

            DotsIndicator(
                dotCount = banners.size,
                type = ShiftIndicatorType(
                    DotGraphic(
                        color = Color(0xFF73B9F2),
                        size = 6.dp
                    )
                ),
                pagerState = pager
            )
        }
    }
}