package com.ocp.ocp_finalproject.content.repository;

import com.ocp.ocp_finalproject.content.domain.AiContent;
import com.ocp.ocp_finalproject.content.enums.ContentStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiContentRepository extends JpaRepository<AiContent, Long> {

    Optional<AiContent> findTopByWorkId(Long workId);

    List<AiContent> findByWorkIdIn(Collection<Long> workIds);

    Optional<AiContent> findByWorkId(Long workId);

    @Query("""
        SELECT ac.choiceTrendKeyword
        FROM AiContent ac
        WHERE ac.choiceTrendKeyword IS NOT NULL
          AND ac.work.workflow.id = :workflowId
        ORDER BY ac.completedAt DESC
    """)
    List<String> findRecentTrendKeywordsByWorkflowId(
            @Param("workflowId") Long workflowId,
            Pageable pageable
    );

    @Query("""
        SELECT ac.choiceProduct
        FROM AiContent ac
        WHERE ac.choiceProduct IS NOT NULL
          AND ac.work.workflow.id = :workflowId
        ORDER BY ac.completedAt DESC
    """)
    List<String> findRecentChoiceProductsByWorkflowId(@Param("workflowId") Long workflowId);

    // 포스팅 통계 조회 쿼리

    /*
     * 특정 기간 동안 특정 상태의 콘텐츠 수 조회
     *
     * @param status 조회할 콘텐츠 상태
     * @param startDateTime 시작 날짜-시간 (포함)
     * @param endDateTime 종료 날짜-시간 (미포함)
     * @return 해당 기간의 콘텐츠 수
     */
    Long countByStatusAndCompletedAtBetween(ContentStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime);

    /*
     * 플랫폼별 발행된 포스팅 수 조회
     *
     * @param startDateTime 시작 날짜-시간 (포함)
     * @param endDateTime 종료 날짜-시간 (미포함)
     * @return [String platformName, Long postCount] 형식의 Object[] 리스트
     */
    @Query("""
        SELECT bt.blogTypeName, COUNT(ac)
        FROM AiContent ac
        JOIN ac.work w
        JOIN w.workflow wf
        JOIN wf.userBlog ub
        JOIN ub.blogType bt
        WHERE ac.status = 'PUBLISHED'
            AND ac.completedAt >= :startDateTime
            AND ac.completedAt < :endDateTime
        GROUP BY bt.blogTypeName
        ORDER BY COUNT(ac) DESC
    """)
    List<Object[]> countByBlogPlatform(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}
