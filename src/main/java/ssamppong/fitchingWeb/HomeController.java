package ssamppong.fitchingWeb;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ssamppong.fitchingWeb.config.SessionUser;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HttpSession httpSession;

    // 메인 화면 - 게시판 목록
    @GetMapping("/")
    public String postList(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            log.info(user.getName() + " fewfew");
            model.addAttribute("userName", user.getName());
        }
        return "list";
    }

}