package hu.bme.aut.fitary.ui.charts.pieChart

import com.github.mikephil.charting.data.PieEntry

sealed class PieChartViewState

object Loading : PieChartViewState()

data class ExercisesLoaded(
    val exercises: List<PieEntry>
) : PieChartViewState()
