package metrik.project.domain.service.githubactions

import metrik.project.domain.model.PipelineConfiguration
import metrik.project.infrastructure.github.feign.GithubFeignClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URI
import java.net.URL

@Service
class BranchService(
    private val githubFeignClient: GithubFeignClient,
) {
    private var logger = LoggerFactory.getLogger(javaClass.name)
    private val githubBaseUrl = "https://api.github.com/repos"

    fun retrieveBranches(
        pipeline: PipelineConfiguration
    ): List<String> {

        logger.info(
            "Get Github Branches - " +
                "Sending request to Github Feign Client with owner: ${pipeline.url}, " +
                "branch: ${pipeline.name}"
        )

        val branches = with(githubFeignClient) {
            getOwnerRepoFromUrl(pipeline.url).let { (owner, repo) ->
                retrieveBranches(
                    URI(pipeline.baseUrl ?: githubBaseUrl),
                    pipeline.credential,
                    owner,
                    repo,
                )
            }
        }
        return branches?.map { it.name } ?: listOf()
    }

    private fun getOwnerRepoFromUrl(url: String): Pair<String, String> {
        val components = URL(url).path.split("/")
        val owner = components[components.size - ownerIndex]
        val repo = components.last()
        return Pair(owner, repo)
    }

    private companion object {
        const val ownerIndex = 2
    }
}
