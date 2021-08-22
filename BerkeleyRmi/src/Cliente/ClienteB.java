//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Pr�tico 2 - Parte 01: Algoritmo de Berkeley													//
//																										//
// UFOP - CSI302 Sistemas Distribu�dos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
// * C�digo adaptado:																					//
//	(FONTE: https://www.guj.com.br/t/java-rmi-cliente-a-e-b-enviar-info-servidor-dar-resposta/349650)	//
//////////////////////////////////////////////////////////////////////////////////////////////////////////

package Cliente;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;

import Servidor.Servico;
import Servidor.ServicoListener;

/**
 * Implementa��o da classe cliente para acessar servi�os do servidor.
 * @author Gildo Tiago
 *
 */
public class ClienteB implements ServicoListener {
	
	/*
	 * Os coment�rios realizados na classe Cliente B s�o an�logos nesta.
	 */
	
	private static final int horas = 3;
	private static final int minutos = 5;
	private static final int valorRelogioLocal = (horas * 60 + minutos);
	private static DecimalFormat df = new DecimalFormat("00");

	public static void main(String[] args) {
		try {
			String nomeServico = "BerkeleyRMI";
			int porta = 8242;
			
			ServicoListener clienteB = new ClienteB();
			ServicoListener clienteBdistribuido = (ServicoListener) UnicastRemoteObject.exportObject(clienteB, 0);

			Registry registry = LocateRegistry.getRegistry(porta);
			Servico servicoRemoto = (Servico) registry.lookup(nomeServico);
			
			servicoRemoto.addListener(clienteBdistribuido, valorRelogioLocal);

			System.out.println("Relogio do Cliente B: " + df.format(horas) + ":" + df.format(minutos));
			servicoRemoto.setRelogio2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void atualizarRelogio(int resultado) throws RemoteException {
		System.out.println("Servidor retornou para o Cliente B: " + resultado + " minutos");
		int ajusteRelogioLocal = valorRelogioLocal+resultado;
		System.out.println("Ajuste do Cliente B: " + df.format(ajusteRelogioLocal/60) + ":" 
						+ df.format(ajusteRelogioLocal%60));
	}
}