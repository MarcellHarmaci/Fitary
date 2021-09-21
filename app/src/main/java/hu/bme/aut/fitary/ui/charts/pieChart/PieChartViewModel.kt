package hu.bme.aut.fitary.ui.charts.pieChart

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class PieChartViewModel @Inject constructor(
    private val presenter: PieChartPresenter
) : RainbowCakeViewModel<PieChartViewState>(Loading) {

    private val flowCollection: Job = executeCancellable(blocking = false) {
        presenter.exercises.collect {
            viewState = ExercisesLoaded(it)
        }
    }

    fun cancelFlowCollection() {
        flowCollection.cancel()
    }

}