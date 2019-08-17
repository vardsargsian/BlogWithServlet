package servlet;

import manager.CategoryManager;
import manager.PostManager;
import model.Post;
import model.User;
import model.UserType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/admin/addPost")
public class AddPostServlet extends HttpServlet {

    private CategoryManager categoryManager = new CategoryManager();
    private PostManager postManager = new PostManager();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getUserType() != UserType.ADMIN) {
            resp.sendRedirect("/login.jsp");
        } else {

            if (ServletFileUpload.isMultipartContent(req)) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1024 * 1024);
                factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(1024 * 1024 * 50);
                upload.setSizeMax(1024 * 1024 * 5 * 50);
                String uploadPath = "C:\\Users\\Vard\\Desktop\\IdeaProjects\\BlogWithServlet\\uploadImages";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                try {
                    Post post = new Post();
                    List<FileItem> formItems = upload.parseRequest(req);
                    if (formItems != null && formItems.size() > 0) {
                        for (FileItem item : formItems) {
                            if (!item.isFormField()) {
                                String fileName = System.currentTimeMillis() + "_" + new File(item.getName()).getName();
                                String filePath = uploadPath + File.separator + fileName;
                                File storeFile = new File(filePath);
                                item.write(storeFile);
                                post.setPicUrl(fileName);
                            } else {
                                if (item.getFieldName().equals("title")) {
                                    post.setTitle(item.getString());
                                } else if (item.getFieldName().equals("shortText")) {
                                    post.setShortText(item.getString());
                                } else if (item.getFieldName().equals("text")) {
                                    post.setText(item.getString());
                                } else if (item.getFieldName().equals("category_id")) {
                                    post.setCategory(categoryManager.getCategoryById(Integer.parseInt(item.getString())));
                                }
                            }
                        }
                        post.setUser(user);
                        post.setCreatedDate(new Date());
                        postManager.addPost(post);
                        resp.sendRedirect("/admin/home");
                    }
                } catch (FileUploadException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
