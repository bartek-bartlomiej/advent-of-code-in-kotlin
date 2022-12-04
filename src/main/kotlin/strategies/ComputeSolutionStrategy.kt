package strategies

fun interface ComputeSolutionStrategy<Data, Result> {
    fun compute(data: Data): Result
}