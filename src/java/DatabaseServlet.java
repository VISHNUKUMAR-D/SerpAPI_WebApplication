
import java.io.BufferedReader;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseServlet extends HttpServlet {
    
    public PreparedStatement statement=null;
    public Connection connection = null;
    public DataClass Data;
    public boolean isHistory = true;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/serpapi", "root", "");
            String SearchString = request.getParameter("SearchString");
            TraceWebsite Trace = new TraceWebsite(SearchString);
            Data = Trace.getJsonText();    
            UpdateDB(Data);
            statement.close();
            request.setAttribute("History",false);
            RequestDispatcher dispatch = request.getRequestDispatcher("ResultTableServlet");
            dispatch.forward(request, response);
        }
        catch(Exception ex){       
            out.print(ex);
        }     
    }
 public void UpdateDB(DataClass Data) throws InterruptedException, IOException, SQLException{
       try{
            statement = connection.prepareStatement("SELECT * FROM india WHERE Date LIKE ? AND Search LIKE ?");
            statement.setString(1, Data.Date);
            statement.setString(2,Data.Search);
            ResultSet search = statement.executeQuery();
            
        if(search.next()){
                statement = connection.prepareStatement("UPDATE india SET Domain1= ?,Domain2= ?,Domain3= ?,Link1=?,Link2=?,Link3=?,Title1=?,Title2=?,Title3=? WHERE Date = ? AND Search=?");
                statement.setString(1, Data.Domain[0]);
                statement.setString(2, Data.Domain[1]);
                statement.setString(3, Data.Domain[2]);
                statement.setString(4, Data.Link[0]);
                statement.setString(5, Data.Link[1]);
                statement.setString(6, Data.Link[2]);
                statement.setString(7, Data.Title[0]);
                statement.setString(8, Data.Title[1]);
                statement.setString(9, Data.Title[2]);
                statement.setString(10,Data.Date);
                statement.setString(11, Data.Search);
                
        }else{
                statement = connection.prepareStatement("INSERT INTO india VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                statement.setString(1,Data.Date);
                statement.setString(2, Data.Search);
                statement.setString(3, Data.Title[0]);
                statement.setString(4, Data.Title[1]);
                statement.setString(5, Data.Title[2]);
                statement.setString(6, Data.Domain[0]);
                statement.setString(7, Data.Domain[1]);
                statement.setString(8, Data.Domain[2]);
                statement.setString(9, Data.Link[0]);
                statement.setString(10, Data.Link[1]);
                statement.setString(11, Data.Link[2]);
        }
        statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
       }
 }
