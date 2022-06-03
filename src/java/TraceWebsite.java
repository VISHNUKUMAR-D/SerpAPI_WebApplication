

// Neccessary packages


import java.io.BufferedReader;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


 class TraceWebsite {
        URL url=null;
        String SearchString;
        TraceWebsite(String SearchString) throws IOException{
         this.SearchString =SearchString;
        this.url = new URL("https://serpapi.com/search.json?q=" +SearchString.replaceAll(" ", "")+"&google_domain=google.com&gl=in&hl=en&api_key=" +
          new ApiResource().getAPI());          
    }
    
    DataClass getJsonText() throws IOException {
        String JsonText = "";
        InputStream is = url.openConnection().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while((line = reader.readLine())!=null)
            JsonText+=line+"\n";
        reader.close();
        System.out.print(JsonText);
        
        return processJsonText(JsonText);         
    }

    private DataClass processJsonText(String JsonText) {
       DataClass Data = new DataClass();
       try{
           JSONParser parser=new JSONParser();
           Object object = parser.parse(JsonText);
           JSONObject jsonObject = (JSONObject) object;
           JSONArray jsonElements = (JSONArray) jsonObject.get("organic_results");
           Data.Date = new SimpleDateFormat("Y-MM-dd").format(new Date());
           Data.Search = SearchString;
           for(int i=0; i<3; i++){
               JSONObject jsonElement = (JSONObject) jsonElements.get(i);
               String string = (String) jsonElement.get("link");
               Data.Domain[i] = string.substring(string.indexOf(".")+1,string.lastIndexOf("."));
               Data.Link[i] = (String) jsonElement.get("link");
               Data.Title[i] = (String) jsonElement.get("title");
           }
           
       }catch(Exception e){
           System.err.println(e);
           e.printStackTrace();
       }
       return Data;
    }
}
