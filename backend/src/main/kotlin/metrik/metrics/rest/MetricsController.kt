package metrik.metrics.rest

import metrik.metrics.domain.model.CalculationPeriod
import metrik.metrics.rest.vo.FourKeyMetricsResponse
import metrik.metrics.rest.vo.MetricsQueryRequest
import metrik.metrics.rest.vo.PipelineStageRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MetricsController {
    @Autowired
    private lateinit var metricsApplicationService: MetricsApplicationService

    @PostMapping("/api/pipeline/metrics")
    fun getFourKeyMetrics(@RequestBody metricsQueryRequest: MetricsQueryRequest): FourKeyMetricsResponse {
        return metricsApplicationService.calculateFourKeyMetrics(
            metricsQueryRequest.pipelineStages,
            metricsQueryRequest.startTime,
            metricsQueryRequest.endTime,
            metricsQueryRequest.unit,
            metricsQueryRequest.branch,
        )
    }

    @GetMapping("/api/pipeline/metrics")
    fun getFourKeyMetrics(
        @RequestParam pipelineId: String,
        @RequestParam targetStage: String,
        @RequestParam startTime: Long,
        @RequestParam endTime: Long,
        @RequestParam unit: CalculationPeriod,
        @RequestParam(required = false) branch: String?
    ): FourKeyMetricsResponse {
        return metricsApplicationService.calculateFourKeyMetrics(
            listOf(PipelineStageRequest(pipelineId, targetStage)),
            startTime,
            endTime,
            unit,
            branch
        )
    }
}
