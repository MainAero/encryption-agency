package de.tub.sase.encryption.agency;

import java.net.InetAddress;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;

public class ApplicationOne extends SecretAgentImpl {
    public ApplicationOne() throws NoSuchAlgorithmException, NoSuchProviderException, RemoteException {
    }

    public static void main(String[] args) {
        try {
            // Get current ip address of machine
            String ip = InetAddress.getLocalHost().getHostAddress();
            // Create ApplicationOne instance
            SecretAgentImpl ApplicationOne = new ApplicationOne();
            // Export ApplicationOne object
            SecretAgent StubApplicationOne = (SecretAgent) UnicastRemoteObject.exportObject(ApplicationOne, 0);
            // Get registry
            Registry r = LocateRegistry.getRegistry(ip, Registry.REGISTRY_PORT);
            // Bind the StubApplicationOne with name "ApplicationOne" to the registry
            r.bind("ApplicationOne", StubApplicationOne);

            // Get a input scanner
            Scanner input = new Scanner(System.in);

            // Get plain text message from user
            System.out.println(".:: Please provide a plain text message:");
            String plainText = input.nextLine();
            // Output message
            System.out.println(".:: PLAIN TEXT");
            System.out.println(plainText);

            // Set plain text message to "ApplicationOne"
            ApplicationOne.setPlainText(plainText);

            // "ApplicationOne" waits now until method "getMessage" gets called
            System.out.println(".:: Waiting for communication partner...");

            // Let it run until user types "exit"
            while (true) {
                String in = input.next();

                if (in.toLowerCase().equals("exit")) {
                    System.out.println("Terminating 'ApplicationOne'...");
                    System.exit(0);
                }
            }
        } catch (ConnectException e) {
            // If there are some connection problems to registry
            System.out.println(".:: Connection problem to registry. Is 'rmiregistry' running?");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
