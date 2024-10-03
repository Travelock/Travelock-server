//13.
package com.travelock.server.dto.oauth2DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private String role;
    private String name;
    private String username;
    private String email;

    private Long memberId;
}
