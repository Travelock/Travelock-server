package com.travelock.server.repository;


import com.travelock.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByProvider(String provider);
    //    UserEntity findByEmail(String email);
    List<Member> findAllByEmail(String email);

    // 닉네임 중복 여부를 확인하는 메서드
    boolean existsByNickName(String nickName);

}
