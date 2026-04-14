package com.kangyu.wellnessdemo.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

internal data class AuthPalette(
    val accent: Color,
    val accentSoft: Color,
    val accentStrong: Color,
    val backgroundStart: Color,
    val backgroundEnd: Color,
    val surface: Color,
    val outline: Color,
    val title: Color,
    val body: Color
)

internal val LoginAuthPalette = AuthPalette(
    accent = Color(0xFF2A67E8),
    accentSoft = Color(0xFFD9E7FF),
    accentStrong = Color(0xFF0E3B9B),
    backgroundStart = Color(0xFFF7FAFF),
    backgroundEnd = Color(0xFFEAF1FF),
    surface = Color(0xFFFDFEFF),
    outline = Color(0xFFD7E0EE),
    title = Color(0xFF17202A),
    body = Color(0xFF5A6473)
)

internal val SignupAuthPalette = AuthPalette(
    accent = Color(0xFF0E8A6C),
    accentSoft = Color(0xFFD6F5EC),
    accentStrong = Color(0xFF065A46),
    backgroundStart = Color(0xFFF5FCF9),
    backgroundEnd = Color(0xFFE8F6F1),
    surface = Color(0xFFFDFEFD),
    outline = Color(0xFFD4E4DD),
    title = Color(0xFF17202A),
    body = Color(0xFF5A6473)
)

@Composable
internal fun AuthScreen(
    palette: AuthPalette,
    eyebrow: String,
    title: String,
    subtitle: String? = null,
    highlights: List<String> = emptyList(),
    formTitle: String,
    formDescription: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        palette.backgroundStart,
                        Color.White,
                        palette.backgroundEnd
                    )
                )
            )
    ) {
        AuthBackgroundOrbs(palette = palette)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 560.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    AuthHero(
                        palette = palette,
                        eyebrow = eyebrow,
                        title = title,
                        subtitle = subtitle,
                        highlights = highlights
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = formTitle,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = palette.title
                            )
                            Text(
                                text = formDescription,
                                style = MaterialTheme.typography.bodyLarge,
                                color = palette.body
                            )
                        }
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthHero(
    palette: AuthPalette,
    eyebrow: String,
    title: String,
    subtitle: String?,
    highlights: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = palette.accentSoft.copy(alpha = 0.84f)
        ) {
            Text(
                text = eyebrow,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = palette.accentStrong
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = palette.title
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = palette.body
                )
            }
        }

        if (highlights.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                highlights.forEach { item ->
                    AuthHighlightChip(
                        text = item,
                        palette = palette
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthHighlightChip(
    text: String,
    palette: AuthPalette
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.74f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = palette.body
        )
    }
}

@Composable
internal fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        isError = isError,
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.92f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.72f),
            disabledContainerColor = Color.White.copy(alpha = 0.52f),
            errorContainerColor = Color.White.copy(alpha = 0.92f),
            focusedIndicatorColor = accentColor,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            focusedLabelColor = accentColor,
            cursorColor = accentColor,
            focusedLeadingIconColor = accentColor,
            unfocusedLeadingIconColor = accentColor.copy(alpha = 0.75f),
            focusedTrailingIconColor = accentColor,
            unfocusedTrailingIconColor = accentColor.copy(alpha = 0.72f)
        )
    )
}

@Composable
internal fun AuthPrimaryButton(
    text: String,
    accentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = Color.White,
            disabledContainerColor = accentColor.copy(alpha = 0.34f),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        ),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Composable
internal fun AuthFooterAction(
    prompt: String,
    actionLabel: String,
    accentColor: Color,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prompt,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(
            onClick = onActionClick,
            colors = ButtonDefaults.textButtonColors(contentColor = accentColor)
        ) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BoxScope.AuthBackgroundOrbs(palette: AuthPalette) {
    DecorativeOrb(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = (-70).dp, y = (-36).dp),
        size = 220.dp,
        brush = Brush.radialGradient(
            colors = listOf(
                palette.accent.copy(alpha = 0.22f),
                Color.Transparent
            )
        ),
        shape = CircleShape
    )

    DecorativeOrb(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = 80.dp, y = (-20).dp),
        size = 280.dp,
        brush = Brush.radialGradient(
            colors = listOf(
                palette.accentSoft.copy(alpha = 0.92f),
                Color.Transparent
            )
        ),
        shape = CircleShape
    )

    DecorativeOrb(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .offset(x = (-30).dp, y = 70.dp),
        size = 180.dp,
        brush = Brush.radialGradient(
            colors = listOf(
                palette.accent.copy(alpha = 0.16f),
                Color.Transparent
            )
        ),
        shape = CircleShape
    )
}

@Composable
private fun DecorativeOrb(
    modifier: Modifier,
    size: androidx.compose.ui.unit.Dp,
    brush: Brush,
    shape: Shape
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(brush)
    )
}
