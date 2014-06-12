/*
 * AESCipher
 * 
 * 0.1
 * 
 * 2014/05/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;


/**
 * AES 128 bits cipher utility class. 
 * Encrypt/Decrypt strings.
 * Previously of the using of this class you should see:
 * http://android-developers.blogspot.com.es/2013/08/some-securerandom-thoughts.html
 */
public class AESCipher {

    private static final String defaultKey = "e8ffc7e56311679f12b6fc91aa77a5eb";
    private static String key = defaultKey;
    private static final byte[] ivBytes =
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    public static void init(final Context context) {
        // 64 bit hex string
        final String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        key = id.concat(id);
    }

    /**
     * Encrypt plain text and returns as base64 string.
     * @param plainText
     * @return The plain text encrypted if no error, the 
     * plain text no encrypted otherwise or null if the plain text is null.
     */
    public static String encrypt(String plainText) {

        if(plainText == null) {
           return null;
        }

        String exit = plainText;
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] textBytes = plainText.getBytes("UTF-8");

            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            byte[] cipherData = cipher.doFinal(textBytes);

            exit = Base64.encodeToString(cipherData, Base64.DEFAULT);
            
        } catch (UnsupportedEncodingException | 
        		NoSuchPaddingException | 
        		InvalidAlgorithmParameterException | 
        		NoSuchAlgorithmException | 
        		IllegalBlockSizeException | 
        		BadPaddingException | 
        		InvalidKeyException e) {
            Logger.e(AESCipher.class.getSimpleName(), e.toString());
        }

        return exit;
    }

    /**
     * Decrypt base64 text and returns as a plain string.
     * @param base64Text
     * @return The plain text decrypted if no error, the 
     * base64 text no decrypted otherwise or null if the base64 text is null.
     */
    public static String decrypt(String base64Text) {

        if(base64Text == null) {
            return null;
        }

        String exit = base64Text;

        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            byte[] textBytes = Base64.decode(base64Text, Base64.DEFAULT);

            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            byte [] cipherData = cipher.doFinal(textBytes);

            exit = new String(cipherData, "UTF-8");

        } catch (UnsupportedEncodingException | 
        		NoSuchPaddingException | 
        		InvalidAlgorithmParameterException | 
        		NoSuchAlgorithmException | 
        		IllegalBlockSizeException | 
        		BadPaddingException | 
        		InvalidKeyException e) {
            Logger.e(AESCipher.class.getSimpleName(), e.toString());
        }

        return exit;

    }
    
}
