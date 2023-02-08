import java.net.*;
import java.io.*;
import java.util.*;

public class asynchronousServer {

    public static void main(String args[])
        throws IOException, InterruptedException
    {

        DatagramSocket ss = new DatagramSocket(1234);
        InetAddress ip = InetAddress.getLocalHost();

        System.out.println("\nSERVER SIDE");

        System.out.println("\nServer is Running !\n");
        Thread ssend;
        ssend = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Scanner sc = new Scanner(System.in);
                    
                    while (true) {
                        synchronized (this)
                        {
                            byte[] sd = new byte[1000];


                            sd = sc.nextLine().getBytes();
                            DatagramPacket sp
                                = new DatagramPacket(
                                    sd,
                                    sd.length,
                                    ip,
                                    5334);

                            ss.send(sp);
                            
                            String msg = new String(sd);
                           

                            if ((msg).equals("Bye")) {
                                System.out.println("Server exiting... ");
                                break;
                            }
                            System.out.println("\nWaiting for client response...\n");
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Exception occurred");
                }
            }
        });

        Thread sreceive;
        sreceive = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    while (true) {
                        synchronized (this)
                        {

                            byte[] rd = new byte[1000];

                            DatagramPacket sp1
                                = new DatagramPacket(
                                    rd,
                                    rd.length);
                            ss.receive(sp1);

                            String msg
                                = (new String(rd)).trim();
                            System.out.println("\nClient ("+ sp1.getPort()+ ") : "+ msg +"\n");
                            

                            if (msg.equals("Bye")) {
                                System.out.println("\nClient connection closed.");
                                break;
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Exception occurred");
                }
            }
        });

        
        ssend.start();
        
        sreceive.start();

        ssend.join();
        sreceive.join();
    }
}