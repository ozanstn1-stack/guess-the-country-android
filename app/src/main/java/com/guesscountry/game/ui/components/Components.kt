package com.guesscountry.game.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guesscountry.game.R
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.ui.game.AnswerStatus
import com.guesscountry.game.ui.theme.GameColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameScreenBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = 190.dp, y = (-90).dp)
                .background(GameColors.Mint.copy(alpha = 0.05f), CircleShape),
        )
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-80).dp, y = 520.dp)
                .background(GameColors.Coral.copy(alpha = 0.04f), CircleShape),
        )
        content()
    }
}

@Composable
fun AppTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .semantics { contentDescription = "Geri" },
            ) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = null, tint = GameColors.Text)
            }
            Spacer(Modifier.width(4.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = GameColors.Text,
            modifier = Modifier.weight(1f),
        )
        trailing?.invoke()
    }
}

@Composable
fun OfflineBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = GameColors.Mint.copy(alpha = 0.13f),
        contentColor = GameColors.Mint,
    ) {
        Text(
            text = "OFFLINE-FIRST",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GameColors.Mint,
            contentColor = GameColors.Ink,
            disabledContainerColor = GameColors.SurfaceMuted,
            disabledContentColor = GameColors.TextMuted,
        ),
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ModeCard(
    type: QuestionType,
    title: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = GameColors.Surface),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.18f)),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(tint.copy(alpha = 0.16f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = modeIcon(type),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(3.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = GameColors.TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text("›", color = tint, fontSize = 28.sp)
        }
    }
}

@Composable
fun StatPill(
    label: String,
    value: String,
    tint: Color = GameColors.Mint,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = tint.copy(alpha = 0.12f),
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = GameColors.TextMuted)
            Spacer(Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, color = tint)
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GameColors.Surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = tint)
            Spacer(Modifier.height(3.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = GameColors.TextMuted)
        }
    }
}

@Composable
fun QuestionVisual(
    type: QuestionType,
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(178.dp)
            .background(GameColors.SurfaceRaised, RoundedCornerShape(28.dp)),
        contentAlignment = Alignment.Center,
    ) {
        when (type) {
            QuestionType.FLAG -> Text(
                text = label.ifBlank { "🌍" },
                fontSize = 82.sp,
            )
            QuestionType.MAP -> androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.map_placeholder),
                contentDescription = "Ülke haritası illüstrasyonu",
                modifier = Modifier.fillMaxSize().padding(16.dp),
            )
            QuestionType.FAMOUS_PLACE -> androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.landmark_placeholder),
                contentDescription = "Ünlü yer illüstrasyonu",
                modifier = Modifier.fillMaxSize().padding(16.dp),
            )
            QuestionType.CAPITAL -> Icon(
                imageVector = Icons.Outlined.LocationCity,
                contentDescription = "Başkent illüstrasyonu",
                tint = GameColors.Amber,
                modifier = Modifier.size(76.dp),
            )
            QuestionType.CURRENCY -> Icon(
                imageVector = Icons.Outlined.AttachMoney,
                contentDescription = "Para birimi illüstrasyonu",
                tint = GameColors.Mint,
                modifier = Modifier.size(76.dp),
            )
        }
    }
}

@Composable
fun AnswerButton(
    text: String,
    index: Int,
    selected: Boolean,
    correct: Boolean,
    answered: Boolean,
    onClick: () -> Unit,
) {
    val status = when {
        !answered -> null
        correct -> AnswerStatus.CORRECT
        selected -> AnswerStatus.INCORRECT
        else -> null
    }
    val background by animateFloatAsState(
        targetValue = if (selected && answered) 1f else 0f,
        animationSpec = tween(220),
        label = "answerScale",
    )
    val container = when (status) {
        AnswerStatus.CORRECT -> GameColors.Success.copy(alpha = 0.17f)
        AnswerStatus.INCORRECT -> GameColors.Error.copy(alpha = 0.17f)
        AnswerStatus.IDLE, null -> GameColors.Surface
    }
    val outline = when (status) {
        AnswerStatus.CORRECT -> GameColors.Success
        AnswerStatus.INCORRECT -> GameColors.Error
        AnswerStatus.IDLE, null -> GameColors.Outline
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .scale(1f + background * 0.012f)
            .clickable(enabled = !answered, onClick = onClick)
            .testTag("answer_option_$index")
            .border(1.dp, outline.copy(alpha = if (answered) 0.9f else 0.55f), RoundedCornerShape(18.dp))
            .semantics { contentDescription = "Seçenek ${index + 1}: $text" },
        shape = RoundedCornerShape(18.dp),
        color = container,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(outline.copy(alpha = 0.16f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = ('A'.code + index).toChar().toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = outline,
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (status == AnswerStatus.CORRECT) {
                Icon(Icons.Outlined.Check, contentDescription = "Doğru", tint = GameColors.Success)
            }
        }
    }
}

@Composable
fun FeedbackBanner(
    status: AnswerStatus,
    gainedPoints: Int,
    modifier: Modifier = Modifier,
) {
    val isCorrect = status == AnswerStatus.CORRECT
    val tint = if (isCorrect) GameColors.Success else GameColors.Error
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = tint.copy(alpha = 0.13f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Outlined.Check else Icons.Outlined.Public,
                contentDescription = null,
                tint = tint,
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = if (isCorrect) {
                    if (gainedPoints > 0) "+$gainedPoints puan" else "Doğru cevap"
                } else {
                    "Doğru cevap yeşil alanda"
                },
                style = MaterialTheme.typography.labelLarge,
                color = tint,
            )
        }
    }
}

@Composable
fun ConfettiBurst(
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    var started by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (visible && started) 1f else 0f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "confetti",
    )
    LaunchedEffect(visible) {
        if (visible) started = true
    }
    AnimatedVisibility(visible = visible, modifier = modifier) {
        Box(Modifier.fillMaxSize()) {
            repeat(14) { index ->
                val angle = (index * 2.4).toDouble()
                val x = (cos(angle) * (55 + (index % 3) * 18)).dp
                val y = ((-progress * (100 + index * 7)) + sin(angle) * 12).dp
                Box(
                    modifier = Modifier
                        .offset(x = x, y = y)
                        .size(if (index % 2 == 0) 7.dp else 5.dp)
                        .alpha(1f - progress)
                        .background(
                            listOf(GameColors.Mint, GameColors.Amber, GameColors.Coral, GameColors.Sky)[index % 4],
                            RoundedCornerShape(2.dp),
                        )
                        .rotate(index * 13f),
                )
            }
        }
    }
}

private fun Modifier.rotate(degrees: Float): Modifier = this.then(
    Modifier.graphicsLayer(rotationZ = degrees),
)

private fun modeIcon(type: QuestionType): ImageVector = when (type) {
    QuestionType.FLAG -> Icons.Outlined.Flag
    QuestionType.CAPITAL -> Icons.Outlined.LocationCity
    QuestionType.MAP -> Icons.Outlined.Map
    QuestionType.FAMOUS_PLACE -> Icons.Outlined.PhotoCamera
    QuestionType.CURRENCY -> Icons.Outlined.AttachMoney
}

fun modeTint(type: QuestionType): Color = when (type) {
    QuestionType.FLAG -> GameColors.Mint
    QuestionType.CAPITAL -> GameColors.Amber
    QuestionType.MAP -> GameColors.Sky
    QuestionType.FAMOUS_PLACE -> GameColors.Coral
    QuestionType.CURRENCY -> GameColors.Lilac
}

@Composable
fun modeTitle(type: QuestionType): String = stringResource(
    when (type) {
        QuestionType.FLAG -> R.string.mode_flags
        QuestionType.CAPITAL -> R.string.mode_capitals
        QuestionType.MAP -> R.string.mode_maps
        QuestionType.FAMOUS_PLACE -> R.string.mode_places
        QuestionType.CURRENCY -> R.string.mode_currencies
    },
)

@Composable
fun modeSubtitle(type: QuestionType): String = stringResource(
    when (type) {
        QuestionType.FLAG -> R.string.mode_flags_desc
        QuestionType.CAPITAL -> R.string.mode_capitals_desc
        QuestionType.MAP -> R.string.mode_maps_desc
        QuestionType.FAMOUS_PLACE -> R.string.mode_places_desc
        QuestionType.CURRENCY -> R.string.mode_currencies_desc
    },
)
