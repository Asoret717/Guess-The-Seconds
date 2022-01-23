package cliente;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ibraime
 */
public class Cliente {
    final static int PORT = 40080;
    final static String HOST = "localhost";
    public static void main(String[] args) {
        try {
            Socket sk = new Socket(HOST, PORT);
            
            enviarMensajesAlServidor(sk);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean isNumeric(String str) { 
    	 try {  
    	    Integer.valueOf(str);  
    	    return true;
    	 } catch(NumberFormatException e){  
    	    return false;  
    	 }  
    }

    private static void enviarMensajesAlServidor(Socket sk) {
        OutputStream os = null;
        InputStream is = null;
        Boolean first = true;
        try {
            os = sk.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            
            is = sk.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            
            Scanner sc = new Scanner(System.in);
            String linea;
            int numero;
            while(true){
            	if(first) {
            		System.out.println("Guess the seconds by mind ,(it will be from 10 to 20), "
            				+ "press return when you are ready");
            		System.out.println("User: ");
                    linea = sc.nextLine();
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                    for (int i = 0; i < 5; i++) {
                    	linea = br.readLine();
                    	System.out.println("Server: " + linea);
                    }
                    first=false;
            	}else {
            		
            		System.out.println("User: ");
                    linea = sc.nextLine();
                    while(!isNumeric(linea)) {
                    	System.out.println("You didn't write a number");
                    	System.out.println("User: ");
                        linea = sc.nextLine();
                    }
            		bw.write(linea);
                    bw.newLine();
                    bw.flush();
                    linea = br.readLine();
                	System.out.println("Server: " + linea);
                	System.out.println(" ");
                	first=true;
            	}
                
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(os != null) os.close();
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
