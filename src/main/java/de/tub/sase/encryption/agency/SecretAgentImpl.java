package de.tub.sase.encryption.agency;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SecretAgentImpl implements SecretAgent, Serializable {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String plainText;

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    private void setKeys() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public SecretAgentImpl() throws NoSuchAlgorithmException, NoSuchProviderException, RemoteException {
        this.setKeys();
    }

    public CipherAndSignature encryptAndSign(byte[] plainText, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException {
        // encrypt
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(plainText);

        //sign
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(this.privateKey, new SecureRandom());
        signature.update(cipherText);
        byte[] signatureBytes = signature.sign();

        return new CipherAndSignature(cipherText, signatureBytes);
    }

    public boolean verify(CipherAndSignature cipherAndSignature, PublicKey publicKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(cipherAndSignature.getCipher());
        return signature.verify(cipherAndSignature.getSignature());
    }

    public byte[] decrypt(CipherAndSignature cipherAndSignature) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        return cipher.doFinal(cipherAndSignature.getCipher());
    }

    /**
     * @param plainText
     */
    public void setPlainText(String plainText) throws RemoteException {
        this.plainText = plainText;
    }

    @Override
    public CipherAndSignature getMessage(PublicKey publicKey) throws RemoteException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, SignatureException, InvalidKeyException {
        System.out.println(".:: Requested the message. PublicKey ends with: ..." + publicKey.toString().substring(publicKey.toString().length() - 22, publicKey.toString().length()));
        return encryptAndSign(this.plainText.getBytes(), publicKey);
    }
}
