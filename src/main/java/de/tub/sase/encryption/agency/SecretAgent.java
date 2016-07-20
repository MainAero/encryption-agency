package de.tub.sase.encryption.agency;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

/**
 * Interface SecretAgent
 *
 * @author Felix Jordan / 26.10.15 21:23
 */
public interface SecretAgent extends java.rmi.Remote {

    /**
     * @param plainText
     */
    void setPlainText(String plainText) throws RemoteException;

    /**
     * @param publicKey
     * @return
     */
    CipherAndSignature getMessage(PublicKey publicKey) throws RemoteException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, SignatureException, InvalidKeyException;

    /**
     * @return PublicKey
     * @throws RemoteException
     */
    PublicKey getPublicKey() throws RemoteException;

    /**
     * @param cipherAndSignature
     * @param publicKey
     * @return boolea
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    boolean verify(CipherAndSignature cipherAndSignature, PublicKey publicKey) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;

    /**
     * @param cipherAndSignature
     * @return byte[]
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    byte[] decrypt(CipherAndSignature cipherAndSignature) throws RemoteException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

    /**
     * @param plainText
     * @param publicKey
     * @return CipherAndSignature
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws SignatureException
     */
    CipherAndSignature encryptAndSign(byte[] plainText, PublicKey publicKey) throws RemoteException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException;
}