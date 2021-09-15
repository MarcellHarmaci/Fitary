package hu.bme.aut.fitary.ui.charts.pieChart

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class PieChartViewModel @Inject constructor(
    private val presenter: PieChartPresenter
) : RainbowCakeViewModel<PieChartViewState>(Loading) {

    init {
        presenter.exercises.observeForever {
            viewState = ExercisesLoaded(it)
        }
    }

}