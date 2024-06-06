package com.server.InvestiMate.common.jwt;

import com.server.InvestiMate.api.member.domain.RoleType;
import lombok.Builder;

public record JwtAuthenticationDTO(
        String oAuth2Id,
        String name,
        RoleType roleType
) {
    @Builder
    public static JwtAuthenticationDTO fromResponse(String oAuth2Id, String name, RoleType roleType) {
        return new JwtAuthenticationDTO(oAuth2Id,name,roleType);
    }
}
