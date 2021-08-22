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
public class ClienteD implements ServicoListener {
	
	/*
	 * Os coment�rios realizados na classe Cliente D s�o an�logos nesta.
	 */
	
	private static final int horas = 2;
	private static final int minutos = 50;
	private static final int valorRelogioLocal = (horas * 60 + minutos);
	private static DecimalFormat df = new DecimalFormat("00");

	public static void main(String[] args) {
		try {
			String nomeServico = "BerkeleyRMI";
			int porta = 8242;
			
			ServicoListener clienteD = new ClienteD();
			ServicoListener clienteDdistribuido = (ServicoListener) UnicastRemoteObject.exportObject(clienteD, 0);

			Registry registry = LocateRegistry.getRegistry(porta);
			Servico servicoRemoto = (Servico) registry.lookup(nomeServico);
			
			servicoRemoto.addListener(clienteDdistribuido, valorRelogioLocal);

			System.out.println("Relogio do Cliente D: " + df.format(horas) + ":" + df.format(minutos));
			servicoRemoto.setRelogio4();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void atualizarRelogio(int resultado) throws RemoteException {
		System.out.println("Servidor retornou para o Cliente D: " + resultado + " minutos");
		int ajusteRelogioLocal = valorRelogioLocal+resultado;
		System.out.println("Ajuste do Cliente D: " + df.format(ajusteRelogioLocal/60) + ":" 
						+ df.format(ajusteRelogioLocal%60));
	}
}