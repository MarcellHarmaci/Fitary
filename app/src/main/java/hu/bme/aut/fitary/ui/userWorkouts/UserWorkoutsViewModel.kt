package hu.bme.aut.fitary.ui.userWorkouts

import android.view.MenuItem
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.fitary.R
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class UserWorkoutsViewModel @Inject constructor(
    private val presenter: UserWorkoutsPresenter
) : RainbowCakeViewModel<UserWorkoutsViewState>(Loading) {

    init {
        executeNonBlocking {
            presenter.workouts.collect {
                viewState = UserWorkoutsLoaded(it)
            }
        }
    }

    fun onPopupItemSelected(item: MenuItem) = execute {
        val position = item.intent.getIntExtra("position", 0)
        // TODO position might be RecyclerView.NO_POSITION! Check when used!

        when (item.itemId) {
            R.id.item_edit_workout -> {

            }
            R.id.item_delete_workout -> {
                // TODO Delete workout
            }
            R.id.item_copy_workout -> {
                // TODO Create a copy of workout and open for editing
            }
        }
    }

}