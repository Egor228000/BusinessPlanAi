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
import com.example.businessplanai.viewModel.WatchViewModel
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography

@Composable
fun Watch(
    padding: PaddingValues,
    watchViewModel: WatchViewModel,
    navigation: NavHostController,
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
            .padding(start = 16.dp, end = 16.dp)

    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
        ) {
            items(1) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = business!!.value?.title ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background
                )
            }
            items(1) {
                Markdown(
                    content = business!!.value?.description ?: "",
                    colors = DefaultMarkdownColors(
                        text = MaterialTheme.colorScheme.background,
                        codeText = Color(0xFFd32f2f),
                        inlineCodeText = Color(0xFF388E3C),
                        linkText = Color(0xFF2D71B3),
                        codeBackground = Color(0xFFF5F5F5),
                        inlineCodeBackground = Color(0xFF422929),
                        dividerColor = MaterialTheme.colorScheme.surface,
                        tableText = MaterialTheme.colorScheme.background,
                        tableBackground = MaterialTheme.colorScheme.onBackground
                    ),
                    typography = DefaultMarkdownTypography(
                        h1 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        h2 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        h3 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        h4 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        h5 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        h6 = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        text = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        code = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        inlineCode = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        quote = TextStyle(
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.background
                        ),
                        paragraph = TextStyle(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        ordered = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        bullet = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        list = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.background
                        ),
                        link = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.background,
                            textDecoration = TextDecoration.Underline
                        ),
                        textLink = TextLinkStyles(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.background
                            ),
                            focusedStyle = SpanStyle(),
                            hoveredStyle = SpanStyle(),
                            pressedStyle = SpanStyle(),
                        ),
                        table = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.background
                        )
                    )
                )
            }
        }
    }
}