/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dto.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import security.AES;
import security.MD5;

/**
 *
 * @author ANGHELO
 */
@WebServlet(name = "Clave", urlPatterns = {"/clave"})
public class Clave extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            // Obtener los valores del formulario
            String passActual = request.getParameter("passActual");
            String nuevaClave = request.getParameter("nuevaClave");
            String confirClave = request.getParameter("confirClave");

            String claveMD5 = MD5.getMd5Hash(passActual);
            String claveMD5N = MD5.getMd5Hash(nuevaClave);

            // Obtener el usuario logueado desde la sesión
            HttpSession session = request.getSession();
            Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

            // Validar que la contraseña actual sea correcta
            if (!usuarioLogueado.getPassUsua().equals(claveMD5)) {
                out.print("{\"resultado\":\"error\", \"mensaje\":\"La contraseña actual es incorrecta\"}");
                return;
            }

            // Validar que la nueva contraseña y la confirmación de la nueva contraseña coincidan
            if (!nuevaClave.equals(confirClave)) {
                out.print("{\"resultado\":\"error\", \"mensaje\":\"La nueva contraseña y la confirmación de la nueva contraseña no coinciden\"}");
                return;
            }

            // Cambiar la contraseña del usuario
            usuarioLogueado.setPassUsua(claveMD5N);

            // Guardar los cambios
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.mycompany_SistemaNightclub_war_1.0-SNAPSHOTPU");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(usuarioLogueado);
            em.getTransaction().commit();

            // Devolver el resultado
            out.print("{\"resultado\":\"ok\"}");
        }
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
