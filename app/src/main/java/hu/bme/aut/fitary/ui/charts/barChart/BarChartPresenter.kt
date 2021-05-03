package hu.bme.aut.fitary.ui.charts.barChart

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class BarChartPresenter @Inject constructor() {

    // TODO
    //  Fetch data necessary for view and format accordingly
    //  Bundle them into a return object if necessary (?)
    suspend fun getData(): String = withIOContext {
        ""
    }
}