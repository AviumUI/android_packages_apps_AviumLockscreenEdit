/*
 * Copyright (C) 2025 The AviumUI Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.avium.lockscreenedit.list

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.avium.lockscreenedit.R
import org.avium.lockscreenedit.config.StyleConfig
import org.avium.lockscreenedit.utils.SystemSettingsManager
import org.avium.lockscreenedit.viewmodel.LockscreenViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LockscreenListScreen(
    navController: NavController,
    viewModel: LockscreenViewModel = viewModel(),
    activity: ComponentActivity
) {
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.isSystemBarsVisible = false
    }

    val context = LocalContext.current

    val initialPage = remember {
        val currentStyleId = SystemSettingsManager.getCurrentStyleId()
        val index = StyleConfig.availableStyles.indexOfFirst { it.id == currentStyleId }
        if (index >= 0) index else 0
    }

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { StyleConfig.getStyleCount() }
    )

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            viewModel.importAndApplyCustomZip(context, it)
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { activity.finish() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(text = stringResource(id = R.string.exit), color = Color.White)
                }
                Button(
                    onClick = {
                        val selectedStyle = viewModel.clockStyles[pagerState.currentPage]
                        if (selectedStyle.id == 99) {
                            filePickerLauncher.launch(arrayOf("application/zip"))
                        } else {
                            viewModel.onApply(context, selectedStyle.id)
                            activity.finish() // Apply and exit
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                ) {
                    Text(
                        text = if (viewModel.clockStyles[pagerState.currentPage].id == 99) {
                            stringResource(id = R.string.custom_zip_import)
                        } else {
                            stringResource(id = R.string.apply)
                        },
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = viewModel.clockStyles[pagerState.currentPage].nameResId),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = if (isLandscape) 80.dp else 48.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val style = viewModel.clockStyles[page]
                    val previewResId = if (isLandscape && style.id != 99) {
                        getPadPreviewResource(style.previewResId)
                    } else {
                        style.previewResId
                    }
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                val pageOffset = (
                                        (pagerState.currentPage - page) + pagerState
                                            .currentPageOffsetFraction
                                        ).absoluteValue

                                val scale = lerp(
                                    start = 0.85f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                                scaleX = scale
                                scaleY = scale

                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                            .fillMaxWidth()
                            .aspectRatio(if (isLandscape) 19f/9f else 9f/19f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Black)
                    ) {
                        if (style.id == 99) {
                            CustomZipPreview()
                        } else {
                            Image(
                                painter = painterResource(id = previewResId),
                                contentDescription = stringResource(id = style.nameResId),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                            )
                        }
                        if (pagerState.currentPage == page && style.id != 99) {
                            Button(
                                onClick = { navController.navigate("edit/${style.id}") },
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 24.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.customize),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun getPadPreviewResource(phonePreviewResId: Int): Int {
    return when (phonePreviewResId) {
        R.drawable.preview_style_1 -> R.drawable.preview_style_pad_1
        R.drawable.preview_style_2 -> R.drawable.preview_style_pad_2
        R.drawable.preview_style_3 -> R.drawable.preview_style_pad_3
        R.drawable.preview_style_4 -> R.drawable.preview_style_pad_4
        R.drawable.preview_style_5 -> R.drawable.preview_style_pad_5
        R.drawable.preview_style_6 -> R.drawable.preview_style_pad_6
        R.drawable.preview_style_8 -> R.drawable.preview_style_pad_8
        R.drawable.preview_style_9 -> R.drawable.preview_style_pad_9
        R.drawable.preview_style_10 -> R.drawable.preview_style_pad_10
        R.drawable.preview_style_11 -> R.drawable.preview_style_pad_11
        R.drawable.preview_style_12 -> R.drawable.preview_style_pad_12
        R.drawable.preview_style_13 -> R.drawable.preview_style_pad_13
        R.drawable.preview_style_14 -> R.drawable.preview_style_pad_14
        R.drawable.preview_style_15 -> R.drawable.preview_style_pad_15
        R.drawable.preview_style_16 -> R.drawable.preview_style_pad_16
        R.drawable.preview_style_17 -> R.drawable.preview_style_pad_17
        else -> phonePreviewResId
    }
}

@Composable
fun CustomZipPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.custom_zip_import),
            tint = Color(0xFF007AFF),
            modifier = Modifier.size(80.dp)
        )
    }
}
