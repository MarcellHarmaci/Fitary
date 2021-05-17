package hu.bme.aut.fitary.ui.charts.barChart

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class BarChartViewModel @Inject constructor(
    private val barChartPresenter: BarChartPresenter
) : RainbowCakeViewModel<BarChartViewState>(Loading) {

    fun load() = execute {
        viewState = BarChartReady(barChartPresenter.getData())
    }
}