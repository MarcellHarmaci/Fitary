package hu.bme.aut.fitary.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

import hu.bme.aut.fitary.R
import kotlinx.android.synthetic.main.fragment_charts.view.*

class ChartsFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private val pieEntries: MutableList<PieEntry> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_charts, container, false)

        pieChart = root.pieChart

        pieEntries.add(PieEntry(10.toFloat(), "Pull-up"))
        pieEntries.add(PieEntry(20.toFloat(), "Chin-up"))
        pieEntries.add(PieEntry(30.toFloat(), "Push-up"))
        pieEntries.add(PieEntry(40.toFloat(), "Sit-up"))

        val dataSet = PieDataSet(pieEntries, "Proportion of exercises")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()

        return root
    }

}