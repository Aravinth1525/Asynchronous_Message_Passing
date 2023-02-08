import java.io.*;
import java.net.*;
import java.util.Scanner;

public class asynchronousClient {

    public static void main(String args[])
        throws IOException, InterruptedException
    {

        DatagramSocket cs
            = new DatagramSocket(5334);
        InetAddress ip
            = InetAddress.getLocalHost();

        System.out.println("\nClient SIDE");

        System.out.println("\nClient 1 Connected !\n");

        Thread csend;
        csend = new Thread(new Runnable() {
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
                                    1234);

                            cs.send(sp);
                            
                            String msg = new String(sd);
                            
                            if (msg.equals("Bye")) {
                                System.out.println("\nClient exiting... ");
                                break;
                            }
                            System.out.println("\nWaiting for server response...\n");
                        }
                    }
                }
                catch (IOException e) {
                    System.out.println("Exception occurred");
                }
            }
        });

        Thread creceive;
        creceive = new Thread(new Runnable() {
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
                            cs.receive(sp1);

                            String msg = (new String(rd)).trim();
                            System.out.println("\nServer : " + msg+"\n");

                            if (msg.equals("Bye")) {
                                System.out.println("\nServer Stopped....");
                                break;
                            }
                        }
                    }
                }
                catch (IOException e) {
                    System.out.println("Exception occurred");
                }
            }
        });

        csend.start();
        creceive.start();

        csend.join();
        creceive.join();
    }
}