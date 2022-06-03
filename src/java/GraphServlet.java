
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GraphServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                PrintWriter out = response.getWriter();
       try{
            response.setContentType("text/html;charset=UTF-8");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/serpapi", "root", "");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM india ORDER BY Date DESC LIMIT 1");
            ResultSet rs = statement.executeQuery();
            rs.next();
            GraphData object1 = processGraphData(new GraphData(),rs.getString("Domain1"),out,connection);
            GraphData object2 = processGraphData(new GraphData(),rs.getString("Domain2"),out,connection);
            GraphData object3 = processGraphData(new GraphData(),rs.getString("Domain3"),out,connection);
            out.println("<!DOCTYPE html>"
                    + "<html>\n" +
                    "<head>\n" +
                    "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                    "<script type=\"text/javascript\">\n" +
                    "google.charts.load('current', {'packages':['corechart']});\n" +
                    "google.charts.setOnLoadCallback(drawChart);\n" +
                    "\n"+
                    "function drawChart() {\n" +
                    "var data = google.visualization.arrayToDataTable([\n" +
                    "['Date', '"+object1.Domain+"', '"+object2.Domain+"','"+object3.Domain+"'],\n");
                    for(int i=6;i>=0;i--)
                    out.println("['"+object1.Date[i]+"',  "+object1.Position[i]+",      "+object2.Position[i]+","+object3.Position[i]+"],\n");
                    out.println(
                    "]);\n" +
                    "  \n" +
                    "var options = {\n" +
                    "title: 'Websites Ranking',\n" +
                    "curveType: 'function',\n" +
                    "legend: { position: 'bottom' }\n" +
                    "};\n" +
                    "\n" +
                    "var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));\n" +
                    "\n" +
                    "chart.draw(data, options);\n" +
                    "}\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div id=\"curve_chart\" style=\"width: 1500px; height: 650px\"></div>\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n");
        } catch (ClassNotFoundException ex) {
            out.print(ex);
            Logger.getLogger(GraphServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
                        out.print(ex);
            Logger.getLogger(GraphServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private GraphData processGraphData(GraphData object,String Domain,PrintWriter out,Connection connection) throws SQLException {
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM india ORDER BY Date DESC");
            ResultSet set = statement.executeQuery();
            int i=0;
            object.Domain = Domain;
        while(set.next()&&(i<7)){
            object.Position[i] = 0;
 
            if(Domain.contains(set.getString("Domain1"))){
                object.Position[i] = 3;
            }
            if (Domain.contains(set.getString("Domain2"))){
                object.Position[i] = 2;
            }
            if (Domain.contains(set.getString("Domain3"))){
                object.Position[i] = 1;
            }
            object.Date[i] =set.getString("Date");
            i++;
        }}
        catch(Exception e){
            out.println(e);
        }
        return object;
    }


}
