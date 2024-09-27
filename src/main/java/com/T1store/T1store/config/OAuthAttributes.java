package com.T1store.T1store.config;

import com.T1store.T1store.constant.Provider;
import com.T1store.T1store.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String picture;
    private Provider provider;
    private String providerId;
    private String tel;
    private Member.Address address;


    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String nickname, String email, String picture, Provider provider, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
        this.providerId = providerId;
        this.tel = "입력하지 않은 정보";
        this.address = null;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        } else if (registrationId.equals("naver")) {
            return ofNaver(userNameAttributeName, attributes);
        } else {
            return ofGoogle(userNameAttributeName, attributes);
        }
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        String providerId = String.valueOf(attributes.get("id"));
        return new OAuthAttributes(attributes,
                userNameAttributeName,
                (String) profile.get("nickname"),
                (String) kakao_account.get("email"),
                (String) profile.get("profile_image_url"),
                Provider.KAKAO,
                providerId
                );
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        String providerId = (String) attributes.get("sub");
        return new OAuthAttributes(attributes,
                userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture"),
                Provider.GOOGLE,
                providerId
                );
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String providerId = (String) response.get("id");
        return new OAuthAttributes(
                attributes,
                userNameAttributeName,
                (String) response.get("name"),
                (String) response.get("email"),
                (String) response.get("profile_image"),
                Provider.NAVER,
                providerId
                );
    }

    public Member toEntity() {
        return new Member(nickname, email, picture, provider, providerId,tel,address);
    }
}
