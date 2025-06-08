package com.MMD.code.Controller;

import com.MMD.code.DTO.MetricAverageResponse;
import com.MMD.code.Entity.AverageType;
import com.MMD.code.Entity.ComponentType;
import com.MMD.code.Entity.TimeRange;
import com.MMD.code.Service.MetricQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/metrics")
@Tag(name = "Metric Query API", description = "Endpoints for retrieving metric averages and core time-series data")
public class MetricQueryController {

    private final MetricQueryService metricQueryService;

    public MetricQueryController(MetricQueryService metricQueryService) {
        this.metricQueryService = metricQueryService;
    }

    @Operation(
            summary = "Get metric averages",
            description = "Returns average metrics for CPU (overall or per core), memory, or disk based on the selected average type and time range.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved metric averages"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content)
            }
    )
    @GetMapping("/average")
    public ResponseEntity<List<MetricAverageResponse>> getAverageMetric(
            @Parameter(description = "Type of average: HOUR, DAY, WEEK") @RequestParam("averageType") AverageType averageType,
            @Parameter(description = "Time range for the average: TODAY, YESTERDAY, LAST_WEEK") @RequestParam("timeRange") TimeRange timeRange,
            @Parameter(description = "Component type: OVERALL_CPU_CORE, INDIVIDUAL_CPU_CORES, MEMORY_USED, MEMORY_UNUSED, DISK") @RequestParam("component") ComponentType componentType
    ) {
        List<MetricAverageResponse> response = metricQueryService.getAverage(componentType, averageType, timeRange);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get individual CPU core time series",
            description = "Returns time-series data for individual CPU cores used in line charts. Only applicable for INDIVIDUAL_CPU_CORES.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved core time series"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content)
            }
    )
    @GetMapping("/core-timeseries")
    public ResponseEntity<List<MetricAverageResponse>> getCoreTimeSeries(
            @Parameter(description = "Type of average: HOUR, DAY, WEEK") @RequestParam("averageType") AverageType averageType,
            @Parameter(description = "Time range for the average: TODAY, YESTERDAY, LAST_WEEK") @RequestParam("timeRange") TimeRange timeRange
    ) {
        List<MetricAverageResponse> response = metricQueryService.getCoreTimeSeries(averageType, timeRange);
        return ResponseEntity.ok(response);
    }
}

