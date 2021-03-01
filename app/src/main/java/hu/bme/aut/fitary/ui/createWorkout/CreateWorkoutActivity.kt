package hu.bme.aut.fitary.ui.createWorkout

import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.fitary.BaseActivity
import hu.bme.aut.fitary.R
import hu.bme.aut.fitary.adapter.ExerciseAdapter
import hu.bme.aut.fitary.data.DomainExercise
import hu.bme.aut.fitary.dataSource.model.Workout
import hu.bme.aut.fitary.extensions.validateNonEmpty
import hu.bme.aut.fitary.ui.exerciseDialog.ExerciseDialog
import kotlinx.android.synthetic.main.activity_create_workout.*

class CreateWorkoutActivity : BaseActivity(), ExerciseDialog.ExerciseResultHandler {
    private val domainExercises: MutableList<DomainExercise> = mutableListOf()
    private lateinit var exercisesAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_workout)

        exercisesAdapter = ExerciseAdapter(this, domainExercises)
        listViewExercises.adapter = exercisesAdapter
        listViewExercises.setOnItemClickListener { parent, view, position, id ->
            val exercise = parent.getItemAtPosition(position) as DomainExercise

            val exerciseDialog = ExerciseDialog(position, exercise)
            exerciseDialog.show(supportFragmentManager, "Edit exercise")
        }

        btnAddExercise.setOnClickListener {
            val exerciseDialog = ExerciseDialog(-1, DomainExercise())
            exerciseDialog.show(supportFragmentManager, "New exercise")
        }

        btnSend.setOnClickListener { sendClick() }
    }

    override fun onSuccessAddExercise(domainExercise: DomainExercise) {
        domainExercises.add(domainExercise)
        exercisesAdapter.notifyDataSetChanged()
    }

    override fun onSuccessEditExercise(position: Int, domainExercise: DomainExercise) {
        domainExercises[position] = domainExercise
        exercisesAdapter.notifyDataSetChanged()
    }

    private fun sendClick() {
        if (!validateForm()) return

        uploadWorkout()
    }

    private fun validateForm(): Boolean {
        if (domainExercises.size == 0) return false

        for (exercise in domainExercises) {
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

        val newWorkout = Workout(uid, userName, domainExercises, comment)

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