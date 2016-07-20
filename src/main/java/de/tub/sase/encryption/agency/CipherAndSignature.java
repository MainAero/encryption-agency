package de.tub.sase.encryption.agency;

import java.io.Serializable;

public class CipherAndSignature implements Serializable {

    private byte[] cipher;
    private byte[] signature;

    public byte[] getCipher() {
        return cipher;
    }

    public void setCipher(byte[] cipher) {
        this.cipher = cipher;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public CipherAndSignature(byte[] cipher, byte[] signature) {
        super();
        this.cipher = cipher;
        this.signature = signature;
    }
}
