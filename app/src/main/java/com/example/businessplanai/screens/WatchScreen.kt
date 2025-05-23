package com.example.businessplanai.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.businessplanai.AppDatabase
import com.example.businessplanai.viewModel.WatchViewModel
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography

@Composable
fun Watch(
    padding: PaddingValues,
    watchViewModel: WatchViewModel,
    navigation: NavHostController,
    db: AppDatabase,
    id: Int?,
    listState: LazyListState
) {
    LaunchedEffect(id) {
        watchViewModel.loadBusinessById(id)
        println(id)
    }
    val business = watchViewModel.business.collectAsState()
    Column(
        modifier = Modifier.padding(padding)
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        ) {
            items(1) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = business!!.value?.title ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            items(1) {
                Markdown(
                    content = business!!.value?.description ?: "",
                    colors = DefaultMarkdownColors(
                        text = MaterialTheme.colorScheme.onSurface,
                        codeText = Color(0xFFd32f2f),
                        inlineCodeText = Color(0xFF388E3C),
                        linkText = Color(0xFF2D71B3),
                        codeBackground = Color(0xFFF5F5F5),
                        inlineCodeBackground = Color(0xFF422929),
                        dividerColor = MaterialTheme.colorScheme.primaryContainer,
                        tableText = MaterialTheme.colorScheme.onBackground,
                        tableBackground = MaterialTheme.colorScheme.surface
                    ),
                    typography = DefaultMarkdownTypography(
                        h1 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        h2 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        h3 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        h4 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        h5 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        h6 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        text = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        code = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        inlineCode = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        quote = TextStyle(
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        paragraph = TextStyle(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        ordered = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        bullet = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        list = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        link = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textDecoration = TextDecoration.Underline
                        ),
                        textLink = TextLinkStyles(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            focusedStyle = SpanStyle(),
                            hoveredStyle = SpanStyle(),
                            pressedStyle = SpanStyle(),
                        ),
                        table = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                )
            }
        }
    }
}