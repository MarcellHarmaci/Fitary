package hu.bme.aut.fitary.ui.charts.pieChart

sealed class PieChartViewState

object Loading : PieChartViewState()

data class ExercisesLoaded(
    val exercises: List<PieChartPresenter.Exercise>
) : PieChartViewState()
