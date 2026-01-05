package com.ocp.ocp_finalproject.work.service;

import com.ocp.ocp_finalproject.user.domain.UserPrincipal;
import com.ocp.ocp_finalproject.work.dto.response.AdminWorkPageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.datasource.url=jdbc:mysql://localhost:3307/ocpdb?useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true",
        "spring.datasource.username=ocpuser",
        "spring.datasource.password=ocp1234",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.format_sql=true",
        "spring.jpa.properties.hibernate.generate_statistics=true",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect",
        "logging.level.org.hibernate.stat=DEBUG"
})
class AdminWorkServiceImplNPlusOneTest {

    @Autowired
    private AdminWorkServiceImpl adminWorkService;

    @Autowired
    private AdminWorkServiceTestHelper testHelper;

    @PersistenceContext
    private EntityManager entityManager;

    private Statistics statistics;
    private Long testWorkflowId;

    @BeforeEach
    void setUp() {
        Session session = entityManager.unwrap(Session.class);
        statistics = session.getSessionFactory().getStatistics();
        statistics.clear();
        statistics.setStatisticsEnabled(true);

        testWorkflowId = testHelper.createTestWorks(20);
    }

    @AfterEach
    void tearDown() {
        if (statistics != null) {
            statistics.setStatisticsEnabled(false);
        }
    }

    @Test
    @DisplayName("[개선 전] getWorksForAdmin - 쿼리 수 카운팅")
    void testQueryCount_BeforeOptimization() {
        // given
        UserPrincipal adminPrincipal = testHelper.createAdminPrincipal();
        int page=0;

        statistics.clear();
        entityManager.clear();

        // when
        AdminWorkPageResponse response = adminWorkService.getWorksForAdmin(
                adminPrincipal,
                testWorkflowId,
                page
        );

        // then
        long queryCount = statistics.getQueryExecutionCount();
        long entityLoadCount = statistics.getEntityLoadCount();

        System.out.println("\n========== [개선 전] 쿼리 실행 통계 ==========");
        System.out.println("총 쿼리 실행 수: " + queryCount);
        System.out.println("엔티티 로드 수: " + entityLoadCount);
        System.out.println("조회된 Work 개수: " + response.getWorks().size());
        System.out.println("============================================\n");

        assertThat(response.getWorks()).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(20);
    }

    @Test
    @DisplayName("[개선 전] getWorksForAdmin - 응답시간 측정")
    void testResponseTime_BeforOptimization() {
        // given
        UserPrincipal adminPrincipal = testHelper.createAdminPrincipal();
        int page = 0;
        StopWatch stopWatch = new StopWatch("AdminWorkService Performance Test");

        entityManager.clear();

        // when
        int iterations = 10;
        long totalTime = 0;

        for (int i = 0; i < iterations; i++) {
            stopWatch.start("Iteration " + (i + 1));

            AdminWorkPageResponse response = adminWorkService.getWorksForAdmin(
                    adminPrincipal,
                    testWorkflowId,
                    page
            );

            stopWatch.stop();
            totalTime += stopWatch.getLastTaskTimeMillis();

            entityManager.clear();
        }

        // then
        double averageTime = (double) totalTime / iterations;

        System.out.println("\n========== [개선 전] 응답 시간 통계 ==========");
        System.out.println(stopWatch.prettyPrint());
        System.out.println("평균 응답 시간: " + String.format("%.2f", averageTime) + " ms");
        System.out.println("총 실행 시간: " + totalTime + " ms");
        System.out.println("=============================================\n");

        assertThat(averageTime).isLessThan(500);
    }

    @Test
    @DisplayName("[상세] 실행된 쿼리 목록 확인")
    void testDetailedQueryLog() {
        // given
        UserPrincipal adminPrincipal = testHelper.createAdminPrincipal();

        statistics.clear();
        entityManager.clear();

        // when
        System.out.println("\n========== 쿼리 실행 시작 ==========");

        AdminWorkPageResponse response = adminWorkService.getWorksForAdmin(
                adminPrincipal,
                testWorkflowId,
                0
        );

        System.out.println("========== 쿼리 실행 종료 ==========\n");

        // then
        System.out.println("\n========== 상세 쿼리 통계 ==========");
        System.out.println("Query Execution Count: " + statistics.getQueryExecutionCount());
        System.out.println("Query Execution Max Time: " + statistics.getQueryExecutionMaxTime() + "ms");

        String[] queries = statistics.getQueries();
        System.out.println("\n실행된 쿼리 목록:");
        for (int i = 0; i < queries.length; i++) {
            System.out.println((i + 1) + ". " + queries[i]);
            System.out.println("   실행 횟수: " + statistics.getQueryStatistics(queries[i]).getExecutionCount());
        }
        System.out.println("====================================\n");

        assertThat(response.getWorks().size()).isEqualTo(10);
    }
}