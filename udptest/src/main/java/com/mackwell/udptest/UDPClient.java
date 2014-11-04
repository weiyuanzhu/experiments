package com.mackwell.udptest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPClient{
	
	private final String FIND = "FIND";
	private final String SETT = "SETT";
	private final String SETC = "SETC";

	private List<byte[]> panelList;
	private String ip;
	
	private static final int SERVER_PORT = 1460;
	private static final int LISTEN_PORT = 5001;
	
	private DatagramSocket udpSocket = null;
	private DatagramPacket udpPacket = null; 
	private boolean isListen = true;
	
	private String msg;
	
	public UDPClient(String msg, String ip)
	{
		super();
		panelList = new ArrayList<byte[]>();
		this.msg = msg;
		this.ip = ip;
	}
	
	public void search(){
		
		ExecutorService exec = Executors.newCachedThreadPool();
		Thread t = new Thread(test);
		exec.execute(t);
		
	}
	
	public void reset(){
		ExecutorService exec = Executors.newCachedThreadPool();
		Thread t = new Thread(new Reset("192.168.1.17"));
		exec.execute(t);
		
	}

    public void singleIpSearch(String ip){
        ExecutorService exec = Executors.newCachedThreadPool();
        Thread t = new Thread(new SingleIpSearch(ip));
        exec.execute(t);
    }
	
	Runnable test = new Runnable(){

		public void run()
		{
			try {
				InetAddress address = InetAddress.getByName(ip);

				udpSocket = new DatagramSocket(LISTEN_PORT);
				
				int msg_len = FIND.length();
				
				udpPacket = new DatagramPacket(FIND.getBytes(),msg_len,address,SERVER_PORT);
				
				
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				udpSocket.send(udpPacket);
				Runnable receive= new Runnable()
				{

					@Override
					public void run() {

						System.out.println("---------------receiving udp packages------------");
						byte[] buf = new byte[1024];
						udpPacket = new DatagramPacket(buf, buf.length);
						while(isListen)
						{
							try {
								udpSocket.receive(udpPacket);
								byte[] buffer = new byte[buf.length];
								int i = 0;
								for(byte b : udpPacket.getData()) {
									//int a = b & 0xFF;
									buffer[i] = b;
									
									i++;
								}
								
								/*for(int j =0; j<buffer.length;j++)
								{
									System.out.print(buffer[j] + " ");
									
								}*/
								
								panelList.add(buffer);
								
								
						
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}



				};
				Thread t = new Thread(receive);
				t.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
	};
	
	private class Reset implements Runnable{
		
		private InetAddress ipAddres;
		
		public Reset(String ipAddress){
			try {
				this.ipAddres = InetAddress.getByName(ipAddress);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		public void run()
		{
			try {
				InetAddress address = InetAddress.getByName("192.168.1.17");
				
				byte[] test = new byte[] {83,69,84,84,0, 8, -36, 21, -87, 30,1,-64, -88, 1, 17, -1, -1, -1, 0, -64, -88, 1, 17, 1, -12, 0, 0, 0, 0, 0, 0, -1, 8, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 10};
				
				
				
				if(udpSocket==null )udpSocket = new DatagramSocket(LISTEN_PORT);
				
				int msg_len = test.length;
				
				udpPacket = new DatagramPacket(test,msg_len,address,SERVER_PORT);
				
				
				
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				udpSocket.send(udpPacket);
				

						System.out.println("---------------receiving udp packages------------");
						byte[] buf = new byte[128+64];
						udpPacket = new DatagramPacket(buf, buf.length);
						while(isListen)
						{
							try {
								udpSocket.receive(udpPacket);
								byte[] buffer = new byte[buf.length];
								int i = 0;
								for(byte b : udpPacket.getData()) {
									//int a = b & 0xFF;
									buffer[i] = b;
									
									i++;
								}
								
								/*for(int j =0; j<buffer.length;j++)
								{
									System.out.print(buffer[j] + " ");
									
								}*/
								
								panelList.add(buffer);
								
								System.out.println();
								for(byte j:buffer)
								{
									System.out.print(j+" ");
								}
								
								
								
						
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
	};

    private class SingleIpSearch implements Runnable{

        private InetAddress ipAddres;

        public SingleIpSearch(String ipAddress){
            try {
                this.ipAddres = InetAddress.getByName(ipAddress);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        public void run(){
            try {
                //InetAddress address = InetAddress.getByName(ipAddres);

                //byte[] test = new byte[] {83,69,84,84,0, 8, -36, 21, -87, 30,1,-64, -88, 1, 17, -1, -1, -1, 0, -64, -88, 1, 17, 1, -12, 0, 0, 0, 0, 0, 0, -1, 8, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 10};

                if(udpSocket==null )udpSocket = new DatagramSocket(LISTEN_PORT);

                //int msg_len = FIND.length();

                udpPacket = new DatagramPacket(FIND.getBytes(),FIND.length(),ipAddres,SERVER_PORT);




            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                udpSocket.send(udpPacket);


                System.out.println("---------------receiving udp packages------------");
                byte[] buf = new byte[128+64];
                udpPacket = new DatagramPacket(buf, buf.length);
                while(isListen)
                {
                    try {
                        udpSocket.receive(udpPacket);
                        byte[] buffer = new byte[buf.length];
                        int i = 0;
                        for(byte b : udpPacket.getData()) {
                            //int a = b & 0xFF;
                            buffer[i] = b;

                            i++;
                        }

								/*for(int j =0; j<buffer.length;j++)
								{
									System.out.print(buffer[j] + " ");

								}*/


                        panelList.add(buffer);

                        System.out.println();
                        for(byte j:buffer)
                        {
                            System.out.print(j+" ");
                        }
                        System.out.println();





                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };
	

	public boolean isListen() {
		return isListen;
	}

	public void setListen(boolean isListen) {
		this.isListen = isListen;
	}
	
	public List<byte[]> getPanelList() {
		return panelList;
	}
	
	public void newfeature(){
		
	}
	
	public void newfeaturei2(){
		
	}
	
}
