package hu.bme.aut.fitary.ui.charts.barChart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.domainModel.DomainWorkout
import kotlinx.android.synthetic.main.fragment_chart_bar.view.*

class BarChartFragment : Fragment() {//RainbowCakeFragment<BarChartViewState, BarChartViewModel>() {

//    override fun provideViewModel(): BarChartViewModel {
//        TODO("Not yet implemented")
//    }
//
//    override fun render(viewState: BarChartViewState) {
//        TODO("Not yet implemented")
//    }

    private lateinit var barChart: HorizontalBarChart
    private var x = 1f
    private val barEntries: MutableList<BarEntry> = mutableListOf()
    private val labels: MutableList<String?> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chart_bar, container, false)

        barChart = root.barChart
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setDrawValueAboveBar(false)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.TOP_INSIDE

        return root
    }

    private fun updateBarChart(newDomainWorkout: DomainWorkout) {
        var sumReps = 0
        for (exercise in newDomainWorkout.domainExercises)
            sumReps += exercise.reps

        barEntries.add(BarEntry(x++, sumReps.toFloat()))
        labels.add(newDomainWorkout.username)

        val dataSet = BarDataSet(barEntries, "")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        dataSet.valueTextSize = 14f

        val barData = BarData(dataSet)
        barData.barWidth = 0.6f

        barChart.data = barData
        // TODO Labels don't appear to the correct bars
        // Tried specifying the number of labels, but
        // it only makes them misaligned and doesn't solve the problem
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.invalidate()
    }

}