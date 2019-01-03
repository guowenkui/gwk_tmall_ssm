package com.how2java.tmall.interceptor;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.ICategoryService;
import com.how2java.tmall.service.IOrderItemService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IOrderItemService orderItemService;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /*这里是获取分类集合信息,用于放在搜索栏下面*/
        List<Category> cs = this.categoryService.list();
        request.getSession().setAttribute("cs",cs);

        /*这里是获取当前的contextPath:tmall_ssm,用于放在左上角的变形金刚,点击之后才能跳转到首页,否则点击之后也仅仅停留在当前页面*/
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        session.setAttribute("contextPath",contextPath);

        /*这里是获取购物车中一共有多少数量*/
        User user = (User) session.getAttribute("user");
        int itemNum = 0;
        if (user!=null){
            List<OrderItem> list = this.orderItemService.listByUser(user.getId());
            for (OrderItem item:list){
                itemNum+=item.getNumber();
            }
        }
        session.setAttribute("cartTotalItemNumber",itemNum);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion(),在访问视图之后被调用");
    }
}
