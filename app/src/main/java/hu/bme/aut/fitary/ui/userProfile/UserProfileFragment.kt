package hu.bme.aut.fitary.ui.userProfile

import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.fitary.R
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : RainbowCakeFragment<UserProfileViewState, UserProfileViewModel>() {

    override fun provideViewModel()= getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_user_profile

    override fun render(viewState: UserProfileViewState) {
        when (viewState) {
            is Loading -> {
                // TODO Display something to indicate loading
            }
            is UserProfileLoaded -> {
                viewState.userProfile.let {
                    tvUsernameDisplay.text = it.username
                    tvNumberOfWorkoutsDisplay.text = it.numberOfWorkouts.toString()
                    tvScoreOfWorkoutsDisplay.text = it.fullScore.toString()
                }
            }
        }.exhaustive
    }

}