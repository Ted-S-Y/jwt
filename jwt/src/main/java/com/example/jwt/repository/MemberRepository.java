package com.example.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwt.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Member findMemberByEmail(String email);

}
