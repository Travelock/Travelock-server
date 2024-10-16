package com.travelock.server.repository;


import com.travelock.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByProvider(String provider);
    //    UserEntity findByEmail(String email);
    List<Member> findAllByEmail(String email);
}
