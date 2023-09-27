/**
 * 
 */
package Rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * 
 */
public class CalculatorServer extends UnicastRemoteObject implements CalculatorInterface {
	public int add(int a, int b) throws RemoteException{
		return a + b;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CalculatorServer server = new CalculatorServer();
			
			LocateRegistry.createRegistry(1099);
			
			Naming.rebind("CalculatorService", server);
			System.out.println("Serveur RMI prêt");
		}catch(Exception e){
			System.err.print("Error du serveur RMI : " + e.toString());
			e.printStackTrace();
			
		}
	}

}
