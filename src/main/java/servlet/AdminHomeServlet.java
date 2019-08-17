package servlet;

import manager.CategoryManager;
import model.Category;
import model.User;
import model.UserType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/admin/home")
public class AdminHomeServlet extends HttpServlet {

    private CategoryManager categoryManager = new CategoryManager();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getUserType() != UserType.ADMIN) {
            resp.sendRedirect("/login.jsp");
        } else {
            List<Category> allCategories = categoryManager.getAllCategories();
            req.setAttribute("categories", allCategories);
            req.getRequestDispatcher("/WEB-INF/admin/admin.jsp").forward(req, resp);

        }
    }
}
