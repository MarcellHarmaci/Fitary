package hu.bme.aut.fitary.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.fitary.ui.barChart.BarChartViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BarChartViewModel::class)
    abstract fun bindBarChartViewModel(barChartViewModel: BarChartViewModel): ViewModel

}
