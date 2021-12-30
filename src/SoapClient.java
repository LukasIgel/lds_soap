
import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.io.StringReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLConnection;
        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;
        import org.w3c.dom.Document;
        import org.w3c.dom.NodeList;
        import org.xml.sax.InputSource;
        import org.xml.sax.SAXException;


/**
 * Client zum Anfragen des Webservices vom lds Übungsblatts. Größtenteils aus Quellcode aus dem Internet zusammengeführt.
 *
 */
public class SoapClient {

    public static void main(String[] args) {
        SoapClient soapClient = new SoapClient();
        soapClient.getAddInteger(14, 33);
    }

    public void getAddInteger(int args1, int args2) {
        String wsURL = "https://crcind.com/csp/samples/SOAP.Demo.CLS";
        URL url = null;
        URLConnection connection = null;
        HttpURLConnection httpConn = null;
        String responseString = null;
        String outputString="";
        OutputStream out = null;
        InputStreamReader isr = null;
        BufferedReader in = null;

        String xmlInput =
        "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:s=\"http://www.w3.org/2001/XMLSchema\"> "
                + "<SOAP-ENV:Body>"
                    +"<AddInteger xmlns=\"http://tempuri.org\">"
                        +"<Arg1>"+args1+"</Arg1>"
                        +"<Arg2>"+args2+"</Arg2>"
                    +"</AddInteger>"
                +"</SOAP-ENV:Body>"
        +"</SOAP-ENV:Envelope>";

        try
        {
            url = new URL(wsURL);
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;

            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();

            String SOAPAction = "";
            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String
                    .valueOf(buffer.length));
            httpConn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");


            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            out = httpConn.getOutputStream();
            out.write(buffer);
            out.close();

            // Read the response and write it to standard out.
            isr = new InputStreamReader(httpConn.getInputStream());
            in = new BufferedReader(isr);

            while ((responseString = in.readLine()) != null)
            {
                outputString = outputString + responseString;
            }
            System.out.println(outputString);
            System.out.println("");

            // Get the response from the web service call
            Document document = parseXmlFile(outputString);

            NodeList nodeLst = document.getElementsByTagName("AddIntegerResult");
            String webServiceResponse = nodeLst.item(0).getTextContent();
            System.out.println("The response from the web service call is : " + webServiceResponse);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Funktion zum Konvertieren eines XML-Objekts im String-Format in ein Dokument-Objekt.
     * @param in Zu konvertierender XML-String
     * @return XML als Document-Objekt
     */
    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}