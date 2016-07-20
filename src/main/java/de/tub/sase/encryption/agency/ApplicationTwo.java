package de.tub.sase.encryption.agency;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;

public class ApplicationTwo extends SecretAgentImpl {
    public ApplicationTwo() throws NoSuchAlgorithmException, NoSuchProviderException, RemoteException {
    }

    public static void main(String[] args) {
        try {
            // Get a input scanner
            Scanner input = new Scanner(System.in);
            // Get current ip address of machine
            String ip = InetAddress.getLocalHost().getHostAddress();

            System.out.println(".:: Please provide ipv4 address of rmiregistry, default will be local ip (" + ip + "):");

            String userIp = input.nextLine();
            // Rudimentary check of userIp
            if (!userIp.isEmpty() && userIp.length() > 1) {
                ip = userIp;
            }

            // Create ApplicationTwo instance
            SecretAgentImpl ApplicationTwo = new ApplicationTwo();

            // Get registry
            Registry r = LocateRegistry.getRegistry(ip, Registry.REGISTRY_PORT);

            // Get "ApplicationOne" from the registry
            SecretAgent ApplicationOne = (SecretAgent) r.lookup("ApplicationOne");

            System.out.println(".:: Requesting message...");
            // Get the message from the "ApplicationOne", send the public key of "ApplicationTwo" to encrypt the message
            CipherAndSignature cipherAndSignature = ApplicationOne.getMessage(ApplicationTwo.getPublicKey());

            System.out.println(".:: CIPHER TEXT");
            System.out.println(new String(cipherAndSignature.getCipher(), StandardCharsets.UTF_8));

            System.out.println(".:: VERIFIES");
            // Verify the message with the public key of "ApplicationOne"
            System.out.println(ApplicationTwo.verify(cipherAndSignature, ApplicationOne.getPublicKey()));

            System.out.println(".:: DECIPHER TEXT");
            // Decrypt the message with private key of "ApplicationTwo" and print it
            System.out.println(new String(ApplicationTwo.decrypt(cipherAndSignature), StandardCharsets.UTF_8));
            System.exit(0);
        } catch (NotBoundException e) {
            // If "ApplicationOne" is not bound output error message
            System.out.println(".:: It seems that 'ApplicationOne' is not bound to the registry!");
            System.exit(1);
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
