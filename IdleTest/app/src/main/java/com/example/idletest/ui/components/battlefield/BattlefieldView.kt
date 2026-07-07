package com.example.idletest.ui.components.battlefield

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.idletest.domain.model.Enemy
import com.example.idletest.domain.model.EnemyType
import com.example.idletest.domain.model.GameState
import kotlin.math.absoluteValue

private val Grass = Color(0xFF6BC04A)
private val GrassDark = Color(0xFF4FAE39)
private val PathMain = Color(0xFFD5AD69)
private val PathShadow = Color(0xFFC2924E)
private val TowerFront = Color(0xFFF3F6F4)
private val TowerSide = Color(0xFFC7D0CF)
private val TowerTop = Color(0xFFFFFFFF)
private val LaserOuter = Color(0xFFFF9E00)
private val LaserInner = Color(0xFFFF5A00)
private val HpRed = Color(0xFFEA3C3C)
private val HpBack = Color(0xFF3C3C3C)

@Composable
fun BattlefieldView(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "battlefield_motion")

    val pulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tower_pulse"
    )

    val laserPulse by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 180),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grass)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawMapBase()
            drawRoads()
            drawMapProps()
            drawEnemies(gameState.enemies)
            drawCoreTower(gameState = gameState, pulse = pulse)
            drawTowerLasers(gameState = gameState, laserPulse = laserPulse)
        }
    }
}

private fun DrawScope.drawMapBase() {
    drawRect(Grass)

    repeat(42) { index ->
        val x = ((index * 83) % size.width.toInt()).toFloat()
        val y = ((index * 47) % size.height.toInt()).toFloat()

        if (!isNearRoad(Offset(x, y))) {
            drawBush(center = Offset(x, y), scale = 0.55f + (index % 4) * 0.12f)
        }
    }
}

private fun DrawScope.drawRoads() {
    val center = battlefieldCenter()
    val roadWidthShadow = 78.dp.toPx()
    val roadWidth = 64.dp.toPx()

    laneStarts().forEach { start ->
        drawLine(
            color = PathShadow,
            start = start,
            end = center,
            strokeWidth = roadWidthShadow
        )
    }

    laneStarts().forEach { start ->
        drawLine(
            color = PathMain,
            start = start,
            end = center,
            strokeWidth = roadWidth
        )
    }

    drawCircle(
        color = PathMain,
        radius = 34.dp.toPx(),
        center = center
    )
}

private fun DrawScope.drawMapProps() {
    /*
    drawStoneCluster(origin = Offset(size.width * 0.20f, size.height * 0.46f), scale = 0.85f)
    drawStoneCluster(origin = Offset(size.width * 0.73f, size.height * 0.43f), scale = 0.72f)
    drawStoneCluster(origin = Offset(size.width * 0.86f, size.height * 0.74f), scale = 0.55f)
     */

    drawTree(base = Offset(size.width * 0.72f, size.height * 0.12f), scale = 1.0f)
    drawTree(base = Offset(size.width * 0.29f, size.height * 0.88f), scale = 1.15f)
    drawTree(base = Offset(size.width * 0.61f, size.height * 0.82f), scale = 1.2f)
    drawTree(base = Offset(size.width * 0.91f, size.height * 0.67f), scale = 0.85f)

    /*
    drawTilePile(origin = Offset(size.width * 0.80f, size.height * 0.26f), count = 6, scale = 0.62f)
    drawTilePile(origin = Offset(size.width * 0.11f, size.height * 0.72f), count = 5, scale = 0.55f)
     */
}

private fun DrawScope.drawEnemies(enemies: List<Enemy>) {
    enemies.sortedByDescending { it.distanceToCore }.forEach { enemy ->
        val position = enemyPosition(enemy)
        val progress = enemyProgress(enemy)
        val scale = 0.70f + progress * 0.45f

        when (enemy.type) {
            EnemyType.BASIC -> drawEnemyCube(position, scale, Color(0xFFE8ECEC), Color(0xFFC4CCCD), Color(0xFFA5B0B2))
            EnemyType.FAST -> drawEnemySphere(position, scale, Color(0xFFF7F8F4), Color(0xFFD7DEDF))
            EnemyType.TANK -> drawEnemyCube(position, scale * 1.22f, Color(0xFFCED7DA), Color(0xFF9BA7AA), Color(0xFF7F898D))
            EnemyType.BOSS -> drawEnemyCube(position, scale * 1.55f, Color(0xFFEAEAEA), Color(0xFFB9C0C4), Color(0xFF8E989E))
        }

        drawEnemyHpBar(enemy = enemy, center = position, width = 42.dp.toPx() * scale)
        drawSmallFloatingText(
            text = compactNumber(enemy.hp),
            position = Offset(position.x - 10.dp.toPx(), position.y - 39.dp.toPx() * scale),
            color = Color.White,
            textSize = 10.dp.toPx()
        )
    }
}

private fun DrawScope.drawCoreTower(
    gameState: GameState,
    pulse: Float
) {
    val center = battlefieldCenter()
    val towerHeight = 86.dp.toPx()
    val towerWidth = 30.dp.toPx()

    val depthX = 10.dp.toPx()
    val depthY = 8.dp.toPx()

    val topY = center.y - towerHeight

    drawCircle(
        color = Color.White.copy(alpha = 0.12f * pulse),
        radius = 58.dp.toPx() * pulse,
        center = Offset(center.x + 4.dp.toPx(), center.y - 26.dp.toPx())
    )

    val shadowCenter = Offset(
        x = center.x + depthX / 2f,
        y = center.y + 10.dp.toPx()
    )

    drawOvalShadow(
        center = shadowCenter,
        width = 76.dp.toPx(),
        height = 24.dp.toPx()
    )

    val front = Rect(
        left = center.x - towerWidth / 2f,
        top = topY,
        right = center.x + towerWidth / 2f,
        bottom = center.y
    )

    val side = Path().apply {
        moveTo(front.right, front.top)
        lineTo(front.right + depthX, front.top - depthY)
        lineTo(front.right + depthX, front.bottom - depthY)
        lineTo(front.right, front.bottom)
        close()
    }

    val cap = Path().apply {
        moveTo(front.left, front.top)
        lineTo(front.right, front.top)
        lineTo(front.right + depthX, front.top - depthY)
        lineTo(front.left + depthX, front.top - depthY)
        close()
    }

    drawPath(path = side, color = TowerSide)
    drawRect(color = TowerFront, topLeft = front.topLeft, size = front.size)
    drawPath(path = cap, color = TowerTop)

    drawPath(
        path = side,
        color = Color(0xFF9EAAAA),
        style = Stroke(width = 1.2f)
    )
    drawRect(
        color = Color(0xFFDDE4E3),
        topLeft = front.topLeft,
        size = front.size,
        style = Stroke(width = 1.2f)
    )
    drawPath(
        path = cap,
        color = Color(0xFFDCE2E1),
        style = Stroke(width = 1.2f)
    )

    val hpPercent = if (gameState.coreMaxHp > 0) {
        gameState.coreHp.toFloat() / gameState.coreMaxHp.toFloat()
    } else {
        0f
    }.coerceIn(0f, 1f)

    val barWidth = 112.dp.toPx()
    val barHeight = 8.dp.toPx()
    val barTopLeft = Offset(center.x - barWidth / 2f, center.y + 23.dp.toPx())

    drawRoundRect(
        color = Color(0xAA1E2A33),
        topLeft = barTopLeft,
        size = Size(barWidth, barHeight),
        cornerRadius = CornerRadius(20f)
    )
    drawRoundRect(
        color = if (hpPercent > 0.35f) Color(0xFF21C46A) else Color(0xFFE84848),
        topLeft = barTopLeft,
        size = Size(barWidth * hpPercent, barHeight),
        cornerRadius = CornerRadius(20f)
    )
}

private fun DrawScope.drawTowerLasers(
    gameState: GameState,
    laserPulse: Float
) {
    val towerMuzzle = Offset(
        x = battlefieldCenter().x + 4.dp.toPx(),
        y = battlefieldCenter().y - 69.dp.toPx()
    )

    val target = gameState.enemies
        .filter { enemy -> enemy.distanceToCore <= gameState.tower.range }
        .minByOrNull { enemy -> enemy.distanceToCore }

    target?.let { enemy ->
        val enemyPosition = enemyPosition(enemy)

        drawLine(
            color = LaserOuter.copy(alpha = 0.50f + laserPulse * 0.35f),
            start = towerMuzzle,
            end = enemyPosition,
            strokeWidth = 5.5.dp.toPx()
        )

        drawLine(
            color = LaserInner.copy(alpha = 0.85f),
            start = towerMuzzle,
            end = enemyPosition,
            strokeWidth = 2.0.dp.toPx()
        )
    }
}

private fun DrawScope.drawEnemyCube(
    center: Offset,
    scale: Float,
    top: Color,
    left: Color,
    right: Color
) {
    drawOvalShadow(
        center = Offset(center.x, center.y + 13.dp.toPx() * scale),
        width = 39.dp.toPx() * scale,
        height = 14.dp.toPx() * scale
    )

    drawIsoBlock(
        origin = center,
        width = 31.dp.toPx() * scale,
        height = 21.dp.toPx() * scale,
        top = top,
        left = left,
        right = right
    )
}

private fun DrawScope.drawEnemySphere(
    center: Offset,
    scale: Float,
    color: Color,
    shade: Color
) {
    drawOvalShadow(
        center = Offset(center.x, center.y + 14.dp.toPx() * scale),
        width = 36.dp.toPx() * scale,
        height = 12.dp.toPx() * scale
    )

    drawCircle(
        color = shade,
        radius = 16.dp.toPx() * scale,
        center = Offset(center.x + 4.dp.toPx() * scale, center.y + 4.dp.toPx() * scale)
    )
    drawCircle(
        color = color,
        radius = 15.dp.toPx() * scale,
        center = center
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.55f),
        radius = 5.dp.toPx() * scale,
        center = Offset(center.x - 5.dp.toPx() * scale, center.y - 5.dp.toPx() * scale)
    )
}

private fun DrawScope.drawEnemyHpBar(enemy: Enemy, center: Offset, width: Float) {
    val hpPercent = if (enemy.maxHp > 0) {
        enemy.hp.toFloat() / enemy.maxHp.toFloat()
    } else {
        0f
    }.coerceIn(0f, 1f)

    val height = 5.dp.toPx()
    val topLeft = Offset(center.x - width / 2f, center.y - 30.dp.toPx())

    drawRect(color = HpBack, topLeft = topLeft, size = Size(width, height))
    drawRect(color = HpRed, topLeft = topLeft, size = Size(width * hpPercent, height))
}

/*
private fun DrawScope.drawStoneCluster(origin: Offset, scale: Float) {
    val block = 19.dp.toPx() * scale
    val positions = listOf(
        Offset(0f, 0f), Offset(block * 0.78f, -block * 0.04f), Offset(block * 1.55f, 0f),
        Offset(block * 0.35f, block * 0.58f), Offset(block * 1.12f, block * 0.56f), Offset(block * 1.90f, block * 0.58f),
        Offset(block * 0.78f, block * 1.12f)
    )

    positions.forEach { offset ->
        drawIsoBlock(
            origin = origin + offset,
            width = block,
            height = block * 0.66f,
            top = Color(0xFFD2D6D7),
            left = Color(0xFFA9B1B4),
            right = Color(0xFF838C90)
        )
    }
}
*/
/*
private fun DrawScope.drawTilePile(origin: Offset, count: Int, scale: Float) {
    repeat(count) { index ->
        val offset = Offset(
            x = (index % 3) * 18.dp.toPx() * scale,
            y = (index / 3) * 12.dp.toPx() * scale
        )
        drawIsoBlock(
            origin = origin + offset,
            width = 18.dp.toPx() * scale,
            height = 11.dp.toPx() * scale,
            top = Color.White,
            left = Color(0xFFBFC8C9),
            right = Color(0xFF9AA4A6)
        )
    }
}
*/

private fun DrawScope.drawTree(base: Offset, scale: Float) {
    val trunkWidth = 10.dp.toPx() * scale
    val trunkHeight = 23.dp.toPx() * scale

    drawRect(
        color = Color(0xFF8B5A2B),
        topLeft = Offset(base.x - trunkWidth / 2f, base.y - trunkHeight),
        size = Size(trunkWidth, trunkHeight)
    )

    drawCircle(Color(0xFF0E7625), radius = 22.dp.toPx() * scale, center = Offset(base.x, base.y - 35.dp.toPx() * scale))
    drawCircle(Color(0xFF1DA83A), radius = 15.dp.toPx() * scale, center = Offset(base.x - 10.dp.toPx() * scale, base.y - 42.dp.toPx() * scale))
    drawCircle(Color(0xFF0A5D20), radius = 13.dp.toPx() * scale, center = Offset(base.x + 13.dp.toPx() * scale, base.y - 29.dp.toPx() * scale))
}

private fun DrawScope.drawBush(center: Offset, scale: Float) {
    drawCircle(GrassDark, radius = 8.dp.toPx() * scale, center = center)
    drawCircle(Color(0xFF238E34), radius = 6.dp.toPx() * scale, center = center + Offset(6.dp.toPx() * scale, 1.dp.toPx() * scale))
    drawCircle(Color(0xFF34B549), radius = 4.dp.toPx() * scale, center = center + Offset(-4.dp.toPx() * scale, -3.dp.toPx() * scale))
}

private fun DrawScope.drawOvalShadow(center: Offset, width: Float, height: Float) {
    drawOval(
        color = Color.Black.copy(alpha = 0.18f),
        topLeft = Offset(center.x - width / 2f, center.y - height / 2f),
        size = Size(width, height)
    )
}

private fun DrawScope.drawIsoBlock(
    origin: Offset,
    width: Float,
    height: Float,
    top: Color,
    left: Color,
    right: Color
) {
    val halfWidth = width / 2f
    val halfHeight = height / 2f

    val topPath = Path().apply {
        moveTo(origin.x, origin.y - halfHeight)
        lineTo(origin.x + halfWidth, origin.y)
        lineTo(origin.x, origin.y + halfHeight)
        lineTo(origin.x - halfWidth, origin.y)
        close()
    }

    val leftPath = Path().apply {
        moveTo(origin.x - halfWidth, origin.y)
        lineTo(origin.x, origin.y + halfHeight)
        lineTo(origin.x, origin.y + height * 1.18f)
        lineTo(origin.x - halfWidth, origin.y + height * 0.68f)
        close()
    }

    val rightPath = Path().apply {
        moveTo(origin.x + halfWidth, origin.y)
        lineTo(origin.x, origin.y + halfHeight)
        lineTo(origin.x, origin.y + height * 1.18f)
        lineTo(origin.x + halfWidth, origin.y + height * 0.68f)
        close()
    }

    drawPath(leftPath, left)
    drawPath(rightPath, right)
    drawPath(topPath, top)
}

private fun DrawScope.drawSmallFloatingText(
    text: String,
    position: Offset,
    color: Color,
    textSize: Float
) {
    drawContext.canvas.nativeCanvas.drawText(
        text,
        position.x,
        position.y,
        android.graphics.Paint().apply {
            isAntiAlias = true
            this.color = android.graphics.Color.argb(
                (color.alpha * 255).toInt(),
                (color.red * 255).toInt(),
                (color.green * 255).toInt(),
                (color.blue * 255).toInt()
            )
            this.textSize = textSize
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            setShadowLayer(3f, 1f, 1f, android.graphics.Color.BLACK)
        }
    )
}

private fun DrawScope.enemyPosition(enemy: Enemy): Offset {
    val start = laneStarts()[enemyLane(enemy)]
    val center = battlefieldCenter()
    val progress = enemyProgress(enemy)

    return Offset(
        x = start.x + (center.x - start.x) * progress,
        y = start.y + (center.y - start.y) * progress
    )
}

private fun enemyProgress(enemy: Enemy): Float {
    return (1f - enemy.distanceToCore.toFloat() / 300f).coerceIn(0.02f, 0.98f)
}

private fun enemyLane(enemy: Enemy): Int {
    return enemy.id.hashCode().absoluteValue % 4
}

private fun DrawScope.battlefieldCenter(): Offset {
    return Offset(size.width * 0.55f, size.height * 0.49f)
}

private fun DrawScope.laneStarts(): List<Offset> {
    return listOf(
        Offset(-size.width * 0.08f, size.height * 0.20f),
        Offset(size.width * 1.08f, size.height * 0.18f),
        Offset(-size.width * 0.08f, size.height * 0.86f),
        Offset(size.width * 1.08f, size.height * 0.82f)
    )
}

private fun DrawScope.isNearRoad(point: Offset): Boolean {
    val center = battlefieldCenter()
    return laneStarts().any { start ->
        val distance = distancePointToSegment(point, start, center)
        distance < 55.dp.toPx()
    }
}

private fun distancePointToSegment(point: Offset, start: Offset, end: Offset): Float {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val lengthSquared = dx * dx + dy * dy

    if (lengthSquared == 0f) {
        return (point - start).getDistance()
    }

    val t = (((point.x - start.x) * dx + (point.y - start.y) * dy) / lengthSquared)
        .coerceIn(0f, 1f)
    val projection = Offset(start.x + t * dx, start.y + t * dy)

    return (point - projection).getDistance()
}

private fun compactNumber(value: Int): String {
    return when {
        value >= 1_000_000 -> "${value / 1_000_000}M"
        value >= 1_000 -> "${value / 1_000}K"
        else -> value.toString()
    }
}
