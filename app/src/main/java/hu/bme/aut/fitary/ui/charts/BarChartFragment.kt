package hu.bme.aut.fitary.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.WorkoutsActivity
import hu.bme.aut.fitary.data.Workout
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_bar_chart.view.*

class BarChartFragment : Fragment() {

    private lateinit var barChart: HorizontalBarChart
    private var x = 1f
    private val barEntries: MutableList<BarEntry> = mutableListOf()
    private val labels: MutableList<String?> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bar_chart, container, false)

        labels.add("")
        barChart = root.barChart
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setDrawValueAboveBar(false)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
//        barChart.xAxis.yOffset = 30f
//        barChart.xAxis.xOffset = 30f

        initWorkoutsListener()

        (context as WorkoutsActivity).fab.visibility = View.INVISIBLE
        return root
    }

    override fun onPause() {
        (context as WorkoutsActivity).fab.visibility = View.VISIBLE
        super.onPause()
    }

    private fun updateBarChart(newWorkout: Workout) {
        var sumReps = 0
        for (exercise in newWorkout.exercises)
            sumReps += exercise.reps

        barEntries.add(BarEntry(x++, sumReps.toFloat()))
        labels.add(newWorkout.userName)

        val dataSet = BarDataSet(barEntries, "")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        dataSet.valueTextSize = 14f

        val barData = BarData(dataSet)
        barData.barWidth = 0.6f

        barChart.data = barData
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.invalidate()
    }

    private fun initWorkoutsListener() {
        FirebaseDatabase.getInstance()
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newWorkout = dataSnapshot.getValue<Workout>(Workout::class.java)

                    if (newWorkout != null) {
                        updateBarChart(newWorkout)
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}