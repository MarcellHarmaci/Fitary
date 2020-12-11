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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.WorkoutsActivity
import hu.bme.aut.fitary.data.Workout
import hu.bme.aut.fitary.extensions.addEntry
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_chart_pie.view.*

class PieChartFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private val pieEntries: MutableList<PieEntry> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chart_pie, container, false)

        pieChart = root.pieChart
        pieChart.legend.textSize = 14f
        pieChart.setDrawEntryLabels(false)
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = false

        initWorkoutsListener()

        return root
    }

    private fun updatePieChart(newWorkout: Workout) {
        for (exercise in newWorkout.exercises)
            pieEntries.addEntry(
                PieEntry(
                    exercise.reps.toFloat(),
                    exercise.name
                )
            )

        val dataSet = PieDataSet(pieEntries, "")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        dataSet.valueTextSize = 14f

        pieChart.data = PieData(dataSet)
        pieChart.invalidate()
    }

    private fun initWorkoutsListener() {
        FirebaseDatabase.getInstance()
            .getReference("workouts")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newWorkout = dataSnapshot.getValue<Workout>(Workout::class.java)
                    val currentUser = FirebaseAuth.getInstance().currentUser

                    if (newWorkout != null && newWorkout.uid == currentUser?.uid) {
                        updatePieChart(newWorkout)
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