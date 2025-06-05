package com.example.snappyshop.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import androidx.compose.ui.graphics.Color
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator

@Composable
fun BannerView(modifier: Modifier = Modifier) {

    var bannerList by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("banners")
            .get().addOnCompleteListener {
                bannerList = it.result.get("urls") as List<String>
            }
    }

    Column(modifier = modifier) {
        val pager = rememberPagerState(0) {
            bannerList.size
        }

        HorizontalPager(state = pager, pageSpacing = 24.dp, modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            AsyncImage(
                model = bannerList[it],
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        DotsIndicator(
            dotCount = bannerList.size,
            type = ShiftIndicatorType(
                DotGraphic(
                    color = MaterialTheme.colorScheme.primary,
                    size = 6.dp
                )
            ),
            pagerState = pager
        )

    }
}

