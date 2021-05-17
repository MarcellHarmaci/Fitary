package hu.bme.aut.fitary.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.fitary.MainActivity
import hu.bme.aut.fitary.R
import kotlinx.android.synthetic.main.fragment_charts.view.*

class ChartsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_charts, container, false)

        root.viewPagerCharts.adapter = ChartPagerAdapter(childFragmentManager)
        (context as MainActivity).setFloatingActionButtonVisible(false)

        return root
    }

    override fun onStop() {
        (activity as MainActivity).setFloatingActionButtonVisible(true)
        super.onStop()
    }

}