import com.example.watch.domain.model.Movie

data class SearchState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

sealed class SearchIntent {
    data class Search(val query: String, val year: String?) : SearchIntent()
    data class SelectMovie(val movie: Movie) : SearchIntent()
}