//6.
package com.travelock.server.dto.oauth2DTO;

public interface OAuth2Response {

    //제공자 (Ex. naver, google, ...)
    String getProvider();
    //제공자에서 발급해주는 아이디(번호)
    String getProviderSecret();
    //이메일
    String getEmail();

}
