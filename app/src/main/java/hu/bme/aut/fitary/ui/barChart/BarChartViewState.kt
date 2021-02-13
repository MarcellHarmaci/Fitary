package hu.bme.aut.fitary.ui.barChart

sealed class BarChartViewState

object Loading : BarChartViewState()

// TODO Contain data necessary for the view
data class BarChartReady(val data: String = "") : BarChartViewState()