package co.wordbe.querydsl.repository;

import co.wordbe.querydsl.dto.MemberSearchCondition;
import co.wordbe.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
