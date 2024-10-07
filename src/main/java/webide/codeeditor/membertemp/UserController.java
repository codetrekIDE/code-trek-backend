package webide.codeeditor.membertemp;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    public void createMember(
            @RequestBody User member
    ) {
        User savedMember = userRepository.save(member);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody LoginRequest loginRequest, HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("userId", String.valueOf(loginRequest.getLoginId()));
        cookie.setMaxAge(60 * 60);  // 쿠키 유효 시간 1시간
        response.addCookie(cookie);
        System.out.println("쿠키 생성");
        return ResponseEntity.ok("Login successful.");
    }
}
