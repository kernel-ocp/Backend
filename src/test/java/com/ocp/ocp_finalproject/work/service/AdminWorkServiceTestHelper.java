package com.ocp.ocp_finalproject.work.service;

import com.ocp.ocp_finalproject.content.domain.AiContent;
import com.ocp.ocp_finalproject.content.enums.ContentStatus;
import com.ocp.ocp_finalproject.content.repository.AiContentRepository;
import com.ocp.ocp_finalproject.trend.domain.TrendCategory;
import com.ocp.ocp_finalproject.trend.repository.TrendCategoryRepository;
import com.ocp.ocp_finalproject.user.domain.User;
import com.ocp.ocp_finalproject.user.domain.UserPrincipal;
import com.ocp.ocp_finalproject.user.repository.UserRepository;
import com.ocp.ocp_finalproject.work.domain.Work;
import com.ocp.ocp_finalproject.work.enums.WorkExecutionStatus;
import com.ocp.ocp_finalproject.work.repository.WorkRepository;
import com.ocp.ocp_finalproject.workflow.domain.Workflow;
import com.ocp.ocp_finalproject.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;import java.util.Collections;

import static com.ocp.ocp_finalproject.user.enums.UserRole.ADMIN;

@Component
@RequiredArgsConstructor
public class AdminWorkServiceTestHelper {
    private final UserRepository userRepository;
    private final WorkflowRepository workflowRepository;
    private final WorkRepository workRepository;
    private final AiContentRepository aiContentRepository;
    private final TrendCategoryRepository trendCategoryRepository;

    @Transactional
    public Long createTestWorks(int workCount){
        User admin = User.createBuilder()
                .email("admin@test.com")
                .name("Test Admin")
                .build();
        admin.updateRole(ADMIN);
        userRepository.save(admin);

        User normalUser = User.createBuilder()
                .email("user@test.com")
                .name("Test User")
                .build();
        userRepository.save(normalUser);

        TrendCategory category = trendCategoryRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("trend_category_id=1 존재하지 않습니다."));

        Workflow workflow = Workflow.create(
                normalUser,
                null,
                category,
                null,
                "https://test-site.com"
        );
        workflowRepository.save(workflow);

        for (int i=0; i < workCount; i++){
            Work work = Work.createBuilder()
                    .workflow(workflow)
                    .status(WorkExecutionStatus.COMPLETED)
                    .startedAt(LocalDateTime.now().minusHours(2))
                    .completedAt(LocalDateTime.now().minusHours(1))
                    .build();
            workRepository.save(work);

            AiContent aiContent = AiContent.createBuilder()
                    .title("테스트 제목 " + (i+1))
                    .content("테스트 콘텐츠 내용 " + (i+1))
                    .choiceProduct("test product" + (i+1))
                    .choiceTrendKeyword("test keyword " + (i+1))
                    .status(ContentStatus.GENERATED)
                    .startedAt(LocalDateTime.now().minusHours(2))
                    .completedAt(LocalDateTime.now().minusHours(1))
                    .work(work)
                    .build();
            aiContentRepository.save(aiContent);
        }

        return workflow.getId();
    }

    public UserPrincipal createAdminPrincipal() {
        User admin = userRepository.findAll().stream()
                .filter(u -> u.getRole() == ADMIN)
                .findFirst()
                .orElseThrow();

        return new UserPrincipal(admin, Collections.emptyMap(),"id");
    }
}
