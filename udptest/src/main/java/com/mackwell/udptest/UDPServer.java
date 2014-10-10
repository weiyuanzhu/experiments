package com.mackwell.udptest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author weiyuan zhu
 * date: 10/04/2014
 * android network UDP server class
 */
public class UDPServer implements Runnable{

	private static final int LISTEN_PORT = 6000;
	private static final int CLIENT_PORT = 6001;
	
	private byte[] msg = new byte[1024];
	
	private boolean live = true;
	
	
	public UDPServer(){}


	public boolean isLive() {
		return live;
	}


	public void setLive(boolean live) {
		this.live = live;
	}


	@Override
	public void run() {
		
		DatagramSocket udpSocket = null;
		DatagramPacket udpPacket = new DatagramPacket(msg,msg.length);
		
		try {
			udpSocket = new DatagramSocket(LISTEN_PORT);
			while (live)
			{
				try {
					InetAddress address = InetAddress.getByName("localhost");
					udpSocket.receive(udpPacket);
					String result = new String(udpPacket.getData(),udpPacket.getOffset(),udpPacket.getLength());
					
					System.out.println("Message received------------->" + result );
					udpPacket = new DatagramPacket(result.getBytes(),result.length(),address,CLIENT_PORT);
					
					udpSocket.send(udpPacket);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void serverUpgrade(){
		int test;
		int test2;
		int a;
	}
	

	
	
	
}
