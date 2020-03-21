package edu.gzc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 登录拦截处理
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if (user != null) {
            return true;
        }
        request.setAttribute("msg","您还没登录，请登录！");
        request.getRequestDispatcher("/toLogin").forward(request,response);
        return false;
    }
}
