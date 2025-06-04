package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class CriptografarSenha {
	public static String criptografarSenha(String senha) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(senha.getBytes("UTF-8"));
			
			StringBuilder sb = new StringBuilder();
			
			for(byte b : hashBytes)
				sb.append(String.format("%02x", b));
			
			return sb.toString();
		}
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
