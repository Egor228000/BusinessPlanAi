package com.example.businessplanai.screens

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businessplanai.R
import com.example.businessplanai.viewModel.WatchViewModel
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Watch(
    watchViewModel: WatchViewModel,
    id: Int,
    listState: LazyListState,
    onBack: () -> Unit,
    scope: CoroutineScope

) {
    val context = LocalContext.current.applicationContext as Application
    var expanded = remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        watchViewModel.loadBusinessById(id)
        println(id)
    }
    val business = watchViewModel.business.collectAsState()
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.onPrimary,)

    ) {
        TopAppBar(

            modifier = Modifier.alpha(0.7f),
            colors = TopAppBarDefaults.topAppBarColors(
                MaterialTheme.colorScheme.onPrimary
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBack()
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_24),
                        null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            },
            title = {
                Text(
                    stringResource(R.string.titleWatch),
                    color = MaterialTheme.colorScheme.background
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        expanded.value = true
                    }) {
                    Icon(
                        painter = painterResource(R.drawable.outline_arrow_downward_24),
                        null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    containerColor = MaterialTheme.colorScheme.onBackground
                ) {
                    DropdownMenuItem(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                expanded.value = false

                                watchViewModel.saveTextToDownloads(
                                    context,
                                    "Бизнес_план.pdf",
                                    "application/pdf",
                                    watchViewModel.getCurrentBusinessText()
                                )
                            }
                        }, text = {
                            Text(
                                "PDF",
                                color = MaterialTheme.colorScheme.background,
                            )
                        }, leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_edit_24),

                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                        }, colors = MenuDefaults.itemColors(
                            MaterialTheme.colorScheme.onBackground
                        )
                    )
                    DropdownMenuItem(
                        onClick = {
                            scope.launch(Dispatchers.IO) {

                                expanded.value = false
                                watchViewModel.saveTextToDownloads(
                                    context = context,
                                    fileName = "бизнес_план.docx",
                                    mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                                    content = watchViewModel.getCurrentBusinessText()
                                )
                            }
                        }, text = { Text("Word") }, leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.outline_delete_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background
                            )
                        }, colors = MenuDefaults.itemColors(
                            MaterialTheme.colorScheme.background
                        )
                    )
                }
            }
        )
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