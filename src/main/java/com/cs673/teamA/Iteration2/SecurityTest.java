package com.cs673.teamA.Iteration2;

import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.*;
import java.util.Base64;
import java.util.Date;

public class SecurityTest{
	public static String encrypt(int uid){
		try{
			KeyGenerator keyGen=KeyGenerator.getInstance("AES");
			keyGen.init(256);
			SecretKey secretKey = keyGen.generateKey();
		
			Date date = new Date();
			String tokenString = Integer.toString(uid)+", " +date.toString();
			byte [] tokenInfo = tokenString.getBytes();
			try{
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, secretKey);
				byte [] transformedBytes = cipher.doFinal(tokenInfo);
				String transformation = new String(transformedBytes);
				return transformation;
			}
			catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) 
    	    {
            	e.printStackTrace();
        	}
        }
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
        return Integer.toString(-1);
	}
	public static int decrypt(String code){
		try{
			KeyGenerator keyGen=KeyGenerator.getInstance("AES");
			keyGen.init(256);
			SecretKey secretKey = keyGen.generateKey();

			Date date = new Date();
			try{
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				byte[] decodedString = cipher.doFinal(code.getBytes());
				String s = new String(decodedString);
				int uid = Integer.parseInt(s.split(",")[0]);
				Date endate = new Date(s.split(",")[1]);
				if(((date.getTime() - endate.getTime()) > 5*60*1000L )|| ((date.getTime() - endate.getTime()) < 0 )){
					return -1;
				}
				else{
					return uid;
				}
			}
			catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) 
     	   {
     	       e.printStackTrace();
     	   }
        }
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
        return -1;
	}
}