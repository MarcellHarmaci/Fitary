package hu.bme.aut.fitary.ui.viewWorkout

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.bumptech.glide.Glide
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.ui.viewWorkout.adapter.ExerciseListAdapter
import kotlinx.android.synthetic.main.fragment_view_workout.*

class ViewWorkoutFragment : RainbowCakeFragment<ViewWorkoutViewState, ViewWorkoutViewModel>() {

    private lateinit var exerciseAdapter: ExerciseListAdapter

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_view_workout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workoutId = arguments?.getString("workout_id")
        viewModel.loadWorkout(workoutId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseAdapter = ExerciseListAdapter()
        rvWorkoutExercises.adapter = exerciseAdapter
    }

    override fun render(viewState: ViewWorkoutViewState) {
        when (viewState) {
            is Loading -> {
                pbLoading.visibility = View.VISIBLE
                tvLoadFailed.visibility = View.GONE
            }
            is LoadingFailed -> {
                pbLoading.visibility = View.GONE
                tvLoadFailed.visibility = View.VISIBLE
            }
            is WorkoutLoaded -> {
                pbLoading.visibility = View.GONE
                tvLoadFailed.visibility = View.GONE

                tvTitle.text = viewState.workout.title
                tvUsername.text = viewState.workout.author
                tvScoreSum.text = getString(R.string.sum_equals, viewState.workout.score)

                exerciseAdapter.submitList(viewState.workout.exercises)

                context?.let {
                    if (viewState.workout.avatar != null) {
                        val avatarAsBitmap = BitmapFactory.decodeByteArray(
                            viewState.workout.avatar,
                            0,
                            viewState.workout.avatar.size
                        )

                        Glide.with(it)
                            .asBitmap()
                            .load(avatarAsBitmap)
                            .circleCrop()
                            .into(ivProfile)
                    } else {
                        Glide.with(it)
                            .load(R.drawable.ic_launcher_background)
                            .circleCrop()
                            .into(ivProfile)
                    }
                }

                btnCreateCopy.setOnClickListener {
                    val navController = findNavController()
                    viewModel.createCopy(navController)
                }
            }
        }.exhaustive
    }

}