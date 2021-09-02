package client_components;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public void connectToTheServer(String domain, int port){
        try{
            socket = new Socket(domain, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void sendRequest(String request){
        output.println(request);
    }

    public String receiveResponse(){
        try {
            return input.readLine();
        } catch (IOException exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public void disconnect(){
        try {
            socket.close();
            input.close();
            output.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
}

