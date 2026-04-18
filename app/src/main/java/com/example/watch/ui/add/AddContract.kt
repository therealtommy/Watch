import com.example.watch.domain.model.Movie

data class AddState(
    val selectedMovie: Movie? = null,
    val isAdding: Boolean = false
)

sealed class AddIntent {
    data class SetSelectedMovie(val movie: Movie?) : AddIntent()
    object AddToWatchlist : AddIntent()
}