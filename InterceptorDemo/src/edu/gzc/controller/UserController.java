package edu.gzc.controller;

import edu.gzc.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller

public class UserController {

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /**
     * 处理登录
     */
    @RequestMapping("/login")
    public String login(User user, HttpSession session, Model model) {
        if ("zhangsan".equals(user.getName()) && "123456".equals(user.getPassword())) {
            session.setAttribute("user", user);
            return "home";
        }
        model.addAttribute("msg", "用户名或密码错误！");
        return "login";
    }

    /**
     * 登出处理
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    /**
     * 跳转主页
     */
    @RequestMapping("/home")
    public String index() {
        return "home";
    }

}

