/**
 * 
 */
package JDBC;

import java.net.*;
/**
 * 
 */
public class udp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(9876);
			byte[]receiveData = new byte[1024];
			
			while(true) {
				// Attendr la récup
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				socket.receive(receivePacket);
				
				String message= new String(receivePacket.getData(), 0, receivePacket.getLength());
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			if(socket !=null && !socket.isClosed()) {
				socket.close();
			}
		}

	}

}
