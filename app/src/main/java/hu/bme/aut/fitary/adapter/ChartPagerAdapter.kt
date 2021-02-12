package hu.bme.aut.fitary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.fitary.ui.barChart.BarChartFragment
import hu.bme.aut.fitary.ui.pieChart.PieChartFragment

class ChartPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PieChartFragment()
            1 -> BarChartFragment()
            else -> PieChartFragment()
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Your exercises"
            1 -> "Reps by workout"
            else -> "Your exercises"
        }
    }
}