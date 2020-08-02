package com.hr.service;

import com.hr.pojo.Member;

import java.util.List;

public interface MemberSerivce {

    Member findByTelephone(String telephone);
    void addMember(Member member);
    List<Integer> findMemberCountByMonths(List<String> months);
}
