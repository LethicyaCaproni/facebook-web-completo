
package controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse resp,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        String contextPath = httpReq.getContextPath();
        String requestURI = httpReq.getRequestURI();
        String servletPath = httpReq.getServletPath();

        System.out.println("Filtro: " + requestURI);

        // Permite acessar o login e o logout sem autenticação.
        boolean isLogin = "/login".equals(servletPath)
                || "/login.jsp".equals(servletPath);

        boolean isLogout = "/logout".equals(servletPath);

        // Permite carregar recursos estáticos.
        boolean isPublicResource =
                servletPath.startsWith("/css/")
                || servletPath.startsWith("/js/")
                || servletPath.startsWith("/images/")
                || servletPath.startsWith("/fonts/")
                || servletPath.equals("/favicon.ico")
                || servletPath.endsWith(".css")
                || servletPath.endsWith(".js")
                || servletPath.endsWith(".png")
                || servletPath.endsWith(".jpg")
                || servletPath.endsWith(".jpeg")
                || servletPath.endsWith(".svg")
                || servletPath.endsWith(".woff")
                || servletPath.endsWith(".woff2");

        if (isLogin || isLogout || isPublicResource) {
            filterChain.doFilter(httpReq, httpResp);
            return;
        }

        // Consulta a sessão sem criar uma nova.
        HttpSession session = httpReq.getSession(false);

        boolean userIsLogged = session != null
                && session.getAttribute("user_logged") != null;

        System.out.println("Usuário autenticado: " + userIsLogged);

        if (userIsLogged) {
            filterChain.doFilter(httpReq, httpResp);
        } else {
            httpResp.sendRedirect(contextPath + "/login.jsp");
        }
    }
}
