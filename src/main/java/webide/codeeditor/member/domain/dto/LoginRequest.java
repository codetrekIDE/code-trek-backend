package webide.codeeditor.member.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @Pattern(regexp = "^[a-z]+$", message = "로그인 아이디는 소문자 알파벳만 가능합니다.")
    private String loginId;
    private String password;

}