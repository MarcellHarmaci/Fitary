package hu.bme.aut.fitary.extensions

import com.github.mikephil.charting.data.PieEntry

fun MutableList<PieEntry>.addEntry(newEntry: PieEntry) {
    var entryOfExercise: PieEntry? = null

    for (entry in this) {
        if (entry.label == newEntry.label)
            entryOfExercise = entry
    }

    if (entryOfExercise == null) {
        this.add(
            PieEntry(
                newEntry.value,
                newEntry.label
            )
        )
    }
    else {
        this.remove(entryOfExercise)
        this.add(
            PieEntry(
                newEntry.value + entryOfExercise.value,
                newEntry.label
            )
        )
    }
}

