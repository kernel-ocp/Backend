package com.ocp.ocp_finalproject.user.repository;

import com.ocp.ocp_finalproject.user.domain.User;
import com.ocp.ocp_finalproject.user.enums.UserRole;
import com.ocp.ocp_finalproject.user.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
    * 복합 검색 (이름, 이메일, 상태, 권한)
    * 모든 조건은 선택적 (NULL 가능)
    * */
    @Query("SELECT u FROM User u WHERE "+
            "(:name IS NULL OR u.name LIKE %:name%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:role IS NULL OR u.role = :role)")
    Page<User> searchUsers(
            @Param("name") String name,
            @Param("email") String email,
            @Param("status") UserStatus status,
            @Param("role") UserRole role,
            Pageable pageable
    );

    /*
    * 특정 기간에 생성된 사용자 수 조회
    * */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt BETWEEN :startTime AND :endTime")
    Long countByCreatedAtBetween(
            @Param("startTime")LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
            );

    /*
    * 특정 기간에 생성된 사용자 수 조회
    * */
    @Query("SELECT COUNT(DISTINCT u) FROM User u WHERE u.updatedAt BETWEEN :startTime AND :endTime")
    Long countActiveUsersBetween(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
