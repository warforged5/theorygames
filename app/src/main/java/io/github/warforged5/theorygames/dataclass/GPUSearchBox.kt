package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPUSearchBox(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onGPUSelected: (GPUPerformanceData) -> Unit,
    onClearSelection: () -> Unit,
    selectedGPU: GPUPerformanceData? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(false) }
    var hasFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Get all available GPUs
    val allGPUs = remember { GameData.getAllGPUs() }

    // Filter GPUs based on search text
    val filteredGPUs = remember(searchText) {
        if (searchText.isBlank()) {
            allGPUs
        } else {
            allGPUs.filter { gpu ->
                gpu.fullName.contains(searchText, ignoreCase = true) ||
                        gpu.shortName.contains(searchText, ignoreCase = true)
            }
        }
    }

    // Show dropdown when focused and has text, or when there's a selection
    val showDropdown = (hasFocus && (searchText.isNotEmpty() || selectedGPU == null)) && enabled

    Column(modifier = modifier) {
        // Search input field
        OutlinedTextField(
            value = if (selectedGPU != null) selectedGPU.fullName else searchText,
            onValueChange = { newText ->
                if (selectedGPU != null) {
                    // If there's a selection, clear it when user starts typing
                    onClearSelection()
                }
                onSearchTextChanged(newText)
            },
            label = { Text("Search GPU") },
            placeholder = { Text("Type to search GPUs...") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    hasFocus = focusState.isFocused
                },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedGPU != null) {
                        // Show checkmark for selected GPU
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                onClearSelection()
                                focusRequester.requestFocus()
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear selection"
                            )
                        }
                    } else if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onSearchTextChanged("")
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    } else {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            }
        )

        // Dropdown with filtered GPUs
        AnimatedVisibility(
            visible = showDropdown && filteredGPUs.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    items(filteredGPUs.take(8)) { gpu ->
                        GPUSearchItem(
                            gpu = gpu,
                            searchText = searchText,
                            onClick = {
                                onGPUSelected(gpu)
                                focusManager.clearFocus()
                            }
                        )
                    }

                    if (filteredGPUs.size > 8) {
                        item {
                            Text(
                                text = "+${filteredGPUs.size - 8} more GPUs...",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Show "no results" when search has text but no matches
        if (showDropdown && searchText.isNotEmpty() && filteredGPUs.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "No GPUs found matching '$searchText'",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun GPUSearchItem(
    gpu: GPUPerformanceData,
    searchText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = gpu.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (gpu.shortName != gpu.fullName) {
                    Text(
                        text = "Also known as: ${gpu.shortName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Brand indicator
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when {
                        gpu.fullName.contains("RTX") || gpu.fullName.contains("GTX") ->
                            MaterialTheme.colorScheme.primaryContainer
                        gpu.fullName.contains("RX") ->
                            MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    }
                ) {
                    Text(
                        text = when {
                            gpu.fullName.contains("RTX") || gpu.fullName.contains("GTX") -> "NVIDIA"
                            gpu.fullName.contains("RX") -> "AMD"
                            else -> "GPU"
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            gpu.fullName.contains("RTX") || gpu.fullName.contains("GTX") ->
                                MaterialTheme.colorScheme.onPrimaryContainer
                            gpu.fullName.contains("RX") ->
                                MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${gpu.getAveragePerformance().toInt()} FPS avg",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}