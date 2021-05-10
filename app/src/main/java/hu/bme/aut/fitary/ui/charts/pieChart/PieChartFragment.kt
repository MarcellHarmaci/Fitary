package hu.bme.aut.fitary.ui.charts.pieChart

import android.graphics.Color
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
            legend.isEnabled = false

            setDrawEntryLabels(true)
            setEntryLabelColor(Color.BLACK)
            
            extraLeftOffset = 30f
            extraRightOffset = 30f
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

        var otherScore = 0.0

        for ((index, exercise) in exercises.sortedByDescending { it.sumOfScore }.withIndex()) {
            if (index < 5) {
                pieEntries.add(
                    PieEntry(
                        exercise.sumOfScore.toFloat(),
                        exercise.name
                    )
                )
            } else {
                otherScore += exercise.sumOfScore
            }
        }

        if (otherScore != 0.0) {
            pieEntries.add(
                PieEntry(
                    otherScore.toFloat(),
                    "Other"
                )
            )
        }

        val colors = resources.getIntArray(R.array.beachColors).toMutableList()
        if (otherScore == 0.0)
            colors.removeLast()

        val dataSet = PieDataSet(pieEntries, "").apply {
            setColors(colors.toIntArray(), 255)
            valueTextSize = 14f
            valueTextColor = Color.BLACK

            valueLinePart1Length = 0.5f
            valueLinePart2Length = 0.1f

            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        }

        val pieData = PieData(dataSet).apply {
            setValueTextColor(Color.BLACK)
        }

        pieChart.apply {
            data = pieData
            notifyDataSetChanged()
            animateXY(1500, 1500, Easing.EaseInOutCirc)
        }
    }

}