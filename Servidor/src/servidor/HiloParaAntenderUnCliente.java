package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ibraime
 */
public class HiloParaAntenderUnCliente extends Thread{
    Socket sk;
    public HiloParaAntenderUnCliente(Socket sk){
        this.sk = sk;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        int seconds = 0;
        int cuenta = 0;
        Boolean first = true;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            is = sk.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            
            os = sk.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            
            Inet4Address ip = (Inet4Address) sk.getInetAddress();
            String laIP = ip.getHostAddress();
            while(true){
            	calendar = Calendar.getInstance();
            	if(first) {
            		System.out.println(laIP + "(" +formatter.format(calendar.getTime()) + ")" + ": The user is preparing");
            	}
                String linea = br.readLine();
                String calculo = "";
                System.out.println(laIP + "(" +formatter.format(calendar.getTime()) + ")" + "(message):  " + linea);
                if(first){
                	seconds= new Random().nextInt(20 - 10 + 1) + 10;
                	bw.write("I will count the 3 first seconds, wait until the end");
                    bw.newLine();
                    bw.flush();
                    System.out.println(laIP + "(" +formatter.format(calendar.getTime()) + ")" + ": The count started");
                    for (int i = 0; i <= seconds; i++) {
                    	if(i==1) {
                    		bw.write("It has passed " + i + " second");
                            bw.newLine();
                            bw.flush();
                    	}else if(i == 2 || i == 3){
                    		bw.write("It has passed " + i + " seconds");
                            bw.newLine();
                            bw.flush();
                    	}
                    	try {
							TimeUnit.SECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}                    	
                    }
                    calendar = Calendar.getInstance();
                    System.out.println(laIP + "(" +formatter.format(calendar.getTime()) + ")" + ": The count ended with " 
                    + seconds + " seconds");
                    bw.write("The count ended, write how many seconds is has passed (In numbers please)");
                    bw.newLine();
                    bw.flush();
                    first=false;
                    }else {
                    	cuenta=Integer.valueOf(linea);
                    	System.out.println(laIP + "(" +formatter.format(calendar.getTime()) + ")" + 
                    	": The user chose " + cuenta + " seconds of the " + seconds);
                    	if(cuenta==seconds) {
                    		bw.write("Congratulations you won, it was " + seconds + " seconds and you said " + cuenta + "!");
                    		bw.newLine();
                    		bw.flush();
                    	}else if((cuenta-seconds==1)||(seconds-cuenta==1)) {
                    		bw.write("So close.. you failed for one, " + cuenta + " seconds of the " + seconds + ", try again..");
                    		bw.newLine();
                    		bw.flush();
                    	}else{
                    		bw.write("You guessed " + cuenta + " seconds of the " + seconds + ", try again..");
                    		bw.newLine();
                    		bw.flush();
                    	}
                    	first=true;
                }
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(HiloParaAntenderUnCliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(is != null) is.close();
            } catch (IOException ex) {
                Logger.getLogger(HiloParaAntenderUnCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
}
