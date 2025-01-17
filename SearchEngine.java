import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> stringList = new ArrayList<String>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return String.format("Welcome, %s", System.getProperty("user.name"));
        } 
        else if (url.getPath().contains("/add")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                stringList.add(parameters[1]);
                return String.format("%s was added to the list. The list now contains: \n", parameters[1]) 
                + stringList.toString();
            }
        }
        else if (url.getPath().contains("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                String hasSubstring = "";
                for (String s : stringList) {
                    if (s.contains(parameters[1])) {
                        if (hasSubstring.equals("")) {
                            hasSubstring += s;
                        }
                        else {
                            hasSubstring += ", " + s;
                        }
                    } 
                }
                return String.format("The following words contain %s: \n", parameters[1]) + hasSubstring;
            }
        }
        return "404 Not Found!";
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
