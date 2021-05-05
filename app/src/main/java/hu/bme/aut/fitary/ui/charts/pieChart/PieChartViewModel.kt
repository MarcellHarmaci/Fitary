package hu.bme.aut.fitary.ui.charts.pieChart

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject

class PieChartViewModel @Inject constructor(
    private val presenter: PieChartPresenter
) : RainbowCakeViewModel<PieChartViewState>(Loading) {

    fun connectView() {
        presenter.connectView()

        execute {
            presenter.exercisesChannel.consumeEach {
                viewState = ExercisesLoaded(it)
            }
        }
    }

    fun disconnectView() = presenter.disconnectView()

}