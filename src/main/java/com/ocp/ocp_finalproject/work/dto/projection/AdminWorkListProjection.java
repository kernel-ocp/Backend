package com.ocp.ocp_finalproject.work.dto.projection;

import com.ocp.ocp_finalproject.work.enums.WorkExecutionStatus;

import java.time.LocalDateTime;

/**
 * Admin Work 목록 조회용 Projection DTO
 * */
public record AdminWorkListProjection(
        Long workId,
        Long workflowId,
        Long userId,
        WorkExecutionStatus status,
        String postingUrl,
        LocalDateTime completedAt,
        String title,
        String contentSummary,
        String choiceProduct,
        String choiceTrendKeyword,
        String failureReason
) {
}
