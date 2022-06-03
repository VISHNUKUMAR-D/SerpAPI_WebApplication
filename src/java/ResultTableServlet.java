
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResultTableServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean History = (boolean) request.getAttribute("History");
        try(PrintWriter out = response.getWriter()){
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/serpapi", "root", "");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM india ORDER BY Date DESC");
            ResultSet set = statement.executeQuery();
            response.setContentType("text/html");
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Serp API</title><style>table,th,td{border:1px solid black}</style>");
            out.println("<link rel= 'stylesheet' href='Bootstrap/css/bootstrap.css'>");
            out.println("<link rel= 'stylesheet' href='Bootstrap/css/bootstrap.min.css'>");
            out.println("<link rel= 'stylesheet' href='Bootstrap/js/bootstrap.min.js'>");
            out.println("</head>");
            out.println("<body>");
            while(set.next()){
                out.println("<br><div class='container'><div class='card'><div class='card-header'><h3 align='center'>"+set.getString("Date")+"</h3></div></div></div><br>");
                out.println("<div class='container'><br><table><thead class='thead-dark'><tr><th width='50px' height='35px'>S.no</th><td height='35px' width='200px'>Search</td><th width='700px'>Title</th><th width='200px'>Domain</th></tr></thead><tbody>");
                out.println("<tr class='success'><td height='35px'>1</td><td>"+set.getString("Search")+"<td><a href='"+set.getString("Link1")+"' class="+"link-primary"+">"+set.getString("Title1")+"</a></td><td>"+set.getString("Domain1")+"</td></tr>");
                out.println("<tr class='success'><td height='35px'>2</td><td>"+set.getString("Search")+"<td><a href='"+set.getString("Link2")+"' class="+"link-primary"+">"+set.getString("Title2")+"</a></td><td>"+set.getString("Domain2")+"</td></tr>");
                out.println("<tr class='success'><td height='35px'>3</td><td>"+set.getString("Search")+"<td><a href='"+set.getString("Link3")+"' class="+"link-primary"+">"+set.getString("Title3")+"</a></td><td>"+set.getString("Domain3")+"</td></tr>");
                out.println("</tbody></table></div><br><br>");
                if(!History)break;
            }
            out.println("</body>");
            out.println("</html>");
        
        } catch (Exception ex) {
            Logger.getLogger(ResultTableServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
    }

    
}
