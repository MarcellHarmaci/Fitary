package hu.bme.aut.fitary.ui.charts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.WorkoutsActivity
import hu.bme.aut.fitary.adapter.ChartPagerAdapter
import kotlinx.android.synthetic.main.fragment_charts.view.*

class ChartsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_charts, container, false)

        root.viewPagerCharts.adapter = ChartPagerAdapter(childFragmentManager)
        (context as WorkoutsActivity).setFloatingActionButtonVisible(false)

        return root
    }

    override fun onPause() {
        (context as WorkoutsActivity).setFloatingActionButtonVisible(true)
        super.onPause()
    }

}