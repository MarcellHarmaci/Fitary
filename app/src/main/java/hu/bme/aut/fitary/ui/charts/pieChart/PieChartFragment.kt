package hu.bme.aut.fitary.ui.charts.pieChart

import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.fitary.R
import kotlinx.android.synthetic.main.fragment_chart_pie.*

class PieChartFragment : RainbowCakeFragment<PieChartViewState, PieChartViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_chart_pie

    override fun onStart() {
        super.onStart()

        pieChart.apply {
            isRotationEnabled = false
            description.isEnabled = false
            setDrawEntryLabels(false)
            legend.textSize = 14f
        }

        viewModel.connectView()
    }

    override fun onStop() {
        viewModel.disconnectView()

        super.onStop()
    }

    override fun render(viewState: PieChartViewState) {
        when (viewState) {
            is Loading -> {
                // TODO
            }
            is ExercisesLoaded -> renderPieChart(viewState.exercises)
        }.exhaustive
    }

    private fun renderPieChart(exercises: List<PieChartPresenter.Exercise>) {
        val pieEntries: MutableList<PieEntry> = mutableListOf()

        for (exercise in exercises) {
            pieEntries.add(
                PieEntry(
                    exercise.sumOfScore.toFloat(),
                    exercise.name
                )
            )
        }

        val dataSet = PieDataSet(pieEntries, "").apply {
            setColors(ColorTemplate.JOYFUL_COLORS, 255)
            valueTextSize = 14f
        }

        pieChart.apply {
            data = PieData(dataSet)
            notifyDataSetChanged()
            invalidate()
        }
    }

}