package test;

import java.security.Provider;
import java.security.Security;
import java.util.UUID;

public class TestEncryption {

	public static void main(String[] args) {
		byte[] uuidBytes = UUID.randomUUID().toString().getBytes();
		for (byte b : uuidBytes) {
			System.out.print(b);
		}
		System.out.println();
		System.out.println(new String(uuidBytes));

		Provider[] providers = Security.getProviders();
		for (Provider p : providers) {
			System.out.println(p);
		}
	}

}
