package com.barryburgle.gameapp.ui.output.chart

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.barryburgle.gameapp.manager.SessionManager
import com.barryburgle.gameapp.ui.theme.Shapes
import com.barryburgle.gameapp.ui.utilities.text.title.SmallTitleText
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

data class LabeledBarEntry(
    val entry: BarEntry,
    val label: String
)

class FluidLineChartRenderer(
    chart: LineDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : LineChartRenderer(chart, animator, viewPortHandler) {

    private inline fun withClipping(c: Canvas, drawBlock: () -> Unit) {
        val saveCount = c.save()
        val canvasWidth = c.width.toFloat()
        val canvasHeight = c.height.toFloat()

        val contentLeft = mViewPortHandler.contentLeft()
        val clipRight = contentLeft + ((canvasWidth - contentLeft) * mAnimator.phaseX)

        c.clipRect(0f, 0f, clipRight, canvasHeight)

        val originalPhaseX = mAnimator.phaseX
        mAnimator.phaseX = 1f

        drawBlock()

        mAnimator.phaseX = originalPhaseX
        c.restoreToCount(saveCount)
    }

    override fun drawData(c: Canvas) {
        withClipping(c) { super.drawData(c) }
    }

    override fun drawValues(c: Canvas) {
        withClipping(c) { super.drawValues(c) }
    }

    override fun drawExtras(c: Canvas) {
        withClipping(c) { super.drawExtras(c) }
    }
}

@Composable
fun OutputLineChart(
    labeledEntries: List<LabeledBarEntry>,
    description: String = "",
    integerValues: Boolean,
    movingAverageWindow: Int = 4,
    movingAverageActive: Boolean = true,
    legendActive: Boolean = true,
    transparentBackgroundActive: Boolean = false,
    paddingOn: Boolean = true,
    isScrollable: Boolean = false,
    modifier: Modifier = Modifier,
    showLabels: Boolean = false
) {
    val normalizedBarEntryList = labeledEntries.mapIndexed { index, labeledEntry ->
        BarEntry((index).toFloat(), labeledEntry.entry.y)
    }
    val defaultSurfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    val surfaceColor = if (transparentBackgroundActive) Color.TRANSPARENT else defaultSurfaceColor
    val composeBackgroundColor =
        if (transparentBackgroundActive) androidx.compose.ui.graphics.Color.Transparent else MaterialTheme.colorScheme.surface

    val onSurfaceColor = MaterialTheme.colorScheme.onPrimary.toArgb()
    val commonLineWidth = 1f
    val inChartValueTextSize = 12f
    val inChartLabelTextSize = 10f
    Column(
        modifier = modifier
            .background(
                composeBackgroundColor,
                Shapes.large
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = if (paddingOn) Modifier
                    .padding(5.dp)
                    .fillMaxSize() else Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                if (description != "") {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SmallTitleText(description)
                        }
                    }
                }
                val gradientColors = intArrayOf(
                    MaterialTheme.colorScheme.onSurface.toArgb(),
                    surfaceColor
                )
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
                    factory = { context ->
                        val barChart =
                            styleLineChart(
                                LineChart(context),
                                surfaceColor,
                                onSurfaceColor,
                                inChartValueTextSize,
                                legendActive,
                                isScrollable
                            )
                        barChart.renderer = FluidLineChartRenderer(barChart, barChart.animator, barChart.viewPortHandler)
                        if (showLabels) {
                            val xAxisLabels = labeledEntries.map { it.label }
                            if (xAxisLabels.isNotEmpty()) {
                                barChart.xAxis.apply {
                                    isEnabled = true
                                    valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                                    position = XAxis.XAxisPosition.BOTTOM
                                    textColor = onSurfaceColor
                                    textSize = inChartLabelTextSize
                                    setDrawGridLines(false)
                                    granularity = 1f
                                    isGranularityEnabled = true
                                    setLabelCount(25, false)
                                }
                                barChart.setXAxisRenderer(object : XAxisRenderer(barChart.viewPortHandler, barChart.xAxis, barChart.getTransformer(YAxis.AxisDependency.LEFT)) {
                                    override fun drawLabel(
                                        c: Canvas?,
                                        formattedLabel: String?,
                                        x: Float,
                                        y: Float,
                                        anchor: MPPointF?,
                                        angleDegrees: Float
                                    ) {
                                        if (formattedLabel == null || c == null) return
                                        val lines = formattedLabel.split("\n")
                                        var currentY = y
                                        for (line in lines) {
                                            Utils.drawXAxisValue(c, line, x, currentY, mAxisLabelPaint, anchor, angleDegrees)
                                            currentY += mAxisLabelPaint.textSize + Utils.convertDpToPixel(2f)
                                        }
                                    }
                                })
                            }
                        }
                        val formatter: ValueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        val leftAxis: YAxis = barChart.getAxisLeft()
                        leftAxis.setValueFormatter(formatter)
                        val dataset =
                            LineDataSet(normalizedBarEntryList, description).apply {
                                color = onSurfaceColor
                                valueTextColor = onSurfaceColor
                                valueTextSize = inChartValueTextSize
                                setDrawValues(true)
                                if (integerValues) {
                                    valueFormatter = formatter
                                }
                                lineWidth = commonLineWidth
                                isHighlightEnabled = true
                                setDrawHighlightIndicators(false)
                                setDrawCircles(true)
                                circleRadius = 2f
                                circleColors = listOf(onSurfaceColor)
                                circleHoleColor = onSurfaceColor
                                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                setDrawFilled(true)
                                fillDrawable =
                                    GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        gradientColors
                                    )
                            }
                        val averageDataset =
                            LineDataSet(
                                SessionManager.computeAverageBarEntryList(normalizedBarEntryList),
                                "Average"
                            ).apply {
                                color = Color.YELLOW
                                lineWidth = commonLineWidth
                                setDrawValues(false)
                                setDrawCircles(false)
                                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                enableDashedLine(15f, 10f, 0f)
                            }
                        val dataSetsList = mutableListOf<LineDataSet>(dataset, averageDataset)
                        if (movingAverageActive) {
                            val movingAverageDataset =
                                LineDataSet(
                                    SessionManager.computeMovingAverage(
                                        normalizedBarEntryList,
                                        minOf(movingAverageWindow, normalizedBarEntryList.size)
                                    ),
                                    "Last ${movingAverageWindow} average"
                                ).apply {
                                    color = Color.RED
                                    lineWidth = commonLineWidth
                                    setDrawValues(false)
                                    setDrawCircles(false)
                                    mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                                }
                            dataSetsList.add(movingAverageDataset)
                        }
                        val barData = LineData(dataSetsList.toList())
                        barChart.data = barData
                        if (isScrollable) {
                            barChart.setVisibleXRangeMaximum(12f)
                            barChart.moveViewToX(normalizedBarEntryList.size.toFloat())
                        }
                        barChart.animateX(900, Easing.EaseInCubic)
                        barChart
                    })
            }
        }
    }
}

fun styleLineChart(
    lineChart: LineChart,
    surfaceColor: Int,
    onSurfacecolor: Int,
    inChartTextSize: Float,
    legendActive: Boolean,
    isScrollable: Boolean = false
): LineChart {
    lineChart.apply {
        setBackgroundColor(surfaceColor)
        axisRight.isEnabled = false
        axisLeft.apply {
            isEnabled = false
        }
        xAxis.apply {
            isEnabled = false
        }
        setTouchEnabled(isScrollable)
        isDragEnabled = isScrollable
        isScaleXEnabled = isScrollable
        isScaleYEnabled = false
        setPinchZoom(false)
        description = null
        legend.isEnabled = legendActive
        legend.textColor = onSurfacecolor
        legend.textSize = inChartTextSize
        extraRightOffset = 20f
        extraBottomOffset = 30f
    }
    return lineChart
}