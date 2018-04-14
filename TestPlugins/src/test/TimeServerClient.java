package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TimeServerClient {

	public static void main(String[] args) throws IOException {
		InetAddress ip = InetAddress.getByName("ch.pool.ntp.org");

		DatagramSocket cs = new DatagramSocket();

		byte[] rd = new byte[100];
		byte[] sd = new byte[100];

		DatagramPacket sp = new DatagramPacket(sd, sd.length, ip, 1234);

		DatagramPacket rp = new DatagramPacket(rd, rd.length);

		cs.send(sp);

		cs.receive(rp);

		String time = new String(rp.getData());

		System.out.println(time);

		cs.close();
	}

}
