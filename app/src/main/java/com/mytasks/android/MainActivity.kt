package com.mytasks.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection

data class Task(
    val id: Long,
    val title: String,
    val completed: Boolean = false
)

private val AppBackground = Color(0xFFF7F8FA)
private val Primary = Color(0xFF365CF5)
private val PrimarySoft = Color(0xFFE9EDFF)
private val TextPrimary = Color(0xFF171A24)
private val TextSecondary = Color(0xFF69707D)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTasksTheme {
                MyTasksApp()
            }
        }
    }
}

@Composable
fun MyTasksTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Primary,
            background = AppBackground,
            surface = Color.White,
            onSurface = TextPrimary
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTasksApp() {
    val tasks = remember {
        mutableStateListOf(
            Task(1, "خرید نان"),
            Task(2, "تماس با مشتری", completed = true),
            Task(3, "مطالعه Android")
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Scaffold(
            containerColor = AppBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "کارهای من",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "کارهای امروز را مرتب نگه دار",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = Primary,
                    contentColor = Color.White,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "افزودن کار")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 18.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                SummaryCard(tasks)

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "فهرست کارها",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(Modifier.height(10.dp))

                if (tasks.isEmpty()) {
                    EmptyState(
                        onAdd = { showAddDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = tasks,
                            key = { it.id }
                        ) { task ->
                            TaskCard(
                                task = task,
                                onToggle = { id ->
                                    val index = tasks.indexOfFirst { it.id == id }
                                    if (index >= 0) {
                                        tasks[index] = tasks[index].copy(
                                            completed = !tasks[index].completed
                                        )
                                    }
                                },
                                onDelete = { id ->
                                    tasks.removeAll { it.id == id }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title ->
                tasks.add(
                    Task(
                        id = System.currentTimeMillis(),
                        title = title.trim()
                    )
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun SummaryCard(tasks: List<Task>) {
    val completed = tasks.count { it.completed }
    val total = tasks.size
    val progress = if (total == 0) 0f else completed.toFloat() / total
    val animatedProgress by animateFloatAsState(progress, label = "progress")
    val progressText = (animatedProgress * 100).toInt().toString() + "%"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "پیشرفت امروز",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = if (total == 0) {
                            "هنوز کاری ثبت نشده"
                        } else {
                            completed.toString() + " از " + total.toString() + " کار انجام شده"
                        },
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(PrimarySoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = progressText,
                        color = Primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .background(
                        color = Color(0xFFE9EBF0),
                        shape = RoundedCornerShape(100)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(9.dp)
                        .background(
                            color = Primary,
                            shape = RoundedCornerShape(100)
                        )
                )
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    onToggle: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    val titleAlpha by animateFloatAsState(
        targetValue = if (task.completed) 0.55f else 1f,
        label = "titleAlpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onToggle(task.id) }) {
                if (task.completed) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "انجام شد",
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = "انجام نشده",
                        tint = Primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Text(
                text = task.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .alpha(titleAlpha),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = if (task.completed) FontWeight.Normal else FontWeight.SemiBold,
                    textDecoration = if (task.completed) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                ),
                color = TextPrimary
            )

            IconButton(onClick = { onDelete(task.id) }) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "حذف",
                    tint = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 45.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(PrimarySoft, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(Modifier.height(14.dp))

        Text(
            text = "لیست کارها خالی است",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "اولین کار خودت را اضافه کن.",
            color = TextSecondary,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(14.dp))

        Button(onClick = onAdd) {
            Text("افزودن کار")
        }
    }
}

@Composable
private fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "کار جدید",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                singleLine = true,
                label = { Text("عنوان کار") },
                placeholder = { Text("مثلاً تماس با مشتری") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("افزودن")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}
