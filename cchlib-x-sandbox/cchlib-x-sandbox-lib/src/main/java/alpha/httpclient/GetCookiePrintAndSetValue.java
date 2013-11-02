package alpha.httpclient;

import org.apache.commons.httpclient.Cookie;
//import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 */
public class GetCookiePrintAndSetValue 
{
    public static void main( final String[] args )
    {
        HttpClient client = new HttpClient();
        
        client.getParams().setParameter("j_username", "abc");
        client.getParams().setParameter("j_password", "pqr");

        GetMethod method = new GetMethod("http://localhost:8080/");
        
        try{
            client.executeMethod( method );
            
            Cookie[] cookies = client.getState().getCookies();
          
            for( int i = 0; i < cookies.length; i++ ) {
                Cookie cookie = cookies[i];
            
                System.err.println(
                        "Cookie: " + cookie.getName() +
                        ", Value: " + cookie.getValue() +
                        ", IsPersistent?: " + cookie.isPersistent() +
                        ", Expiry Date: " + cookie.getExpiryDate() +
                        ", Comment: " + cookie.getComment()
                        );
                }
            
            client.executeMethod( method );
            } 
        catch(Exception e) {
          System.err.println(e);
            } 
        finally {
          method.releaseConnection();
            }
    }
}