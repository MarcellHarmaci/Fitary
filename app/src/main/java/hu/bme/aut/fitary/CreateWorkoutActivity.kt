package hu.bme.aut.fitary

import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.adapter.ExerciseAdapter
import hu.bme.aut.fitary.data.Exercise
import hu.bme.aut.fitary.data.Workout
import hu.bme.aut.fitary.extensions.validateNonEmpty
import kotlinx.android.synthetic.main.activity_create_workout.*

class CreateWorkoutActivity : BaseActivity(), ExerciseDialog.ExerciseResultHandler {
    private val exercises: MutableList<Exercise> = mutableListOf()
    private lateinit var exercisesAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        exercisesAdapter = ExerciseAdapter(this, exercises)
        listViewExercises.adapter = exercisesAdapter
        listViewExercises.setOnItemClickListener { parent, view, position, id ->
            val exercise = parent.getItemAtPosition(position) as Exercise

            val exerciseDialog = ExerciseDialog(position, exercise)
            exerciseDialog.show(supportFragmentManager, "Edit exercise")
        }

        btnAddExercise.setOnClickListener {
            val exerciseDialog = ExerciseDialog(-1, Exercise())
            exerciseDialog.show(supportFragmentManager, "New exercise")
        }

        btnSend.setOnClickListener { sendClick() }
    }

    override fun onSuccessAddExercise(exercise: Exercise) {
        exercises.add(exercise)
        exercisesAdapter.notifyDataSetChanged()
    }

    override fun onSuccessEditExercise(position: Int, exercise: Exercise) {
        exercises[position] = exercise
        exercisesAdapter.notifyDataSetChanged()
    }

    private fun sendClick() {
        if (!validateForm()) return

        uploadWorkout()
    }

    private fun validateForm(): Boolean {
        if (exercises.size == 0) return false

        for (exercise in exercises) {
            if (!exercise.validate()) return false
        }

        return true
    }

    private fun uploadWorkout() {
        showProgressDialog()

        val key = FirebaseDatabase.getInstance().reference.child("workouts").push().key ?: return

        val comment =
            if (etComment.validateNonEmpty())
                etComment.text.toString()
            else ""

        val newWorkout = Workout(uid, userName, exercises, comment)

        FirebaseDatabase.getInstance().reference
            .child("workouts")
            .child(key)
            .setValue(newWorkout)
            .addOnCompleteListener {
                hideProgressDialog()
                toast("Workout saved")
                finish()
            }
    }

}