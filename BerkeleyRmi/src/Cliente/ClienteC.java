//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Prático 2 - Parte 01: Algoritmo de Berkeley													//
//																										//
// UFOP - CSI302 Sistemas Distribuídos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
// * Código adaptado:																					//
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
 * Implementação da classe cliente para acessar serviços do servidor.
 * @author Gildo Tiago
 *
 */
public class ClienteC implements ServicoListener {
	
	/*
	 * Os comentários realizados na classe Cliente C são análogos nesta.
	 */
	
	private static final int horas = 3;
	private static final int minutos = 25;
	private static final int valorRelogioLocal = (horas * 60 + minutos);
	private static DecimalFormat df = new DecimalFormat("00");

	public static void main(String[] args) {
		try {
			String nomeServico = "BerkeleyRMI";
			int porta = 8242;
			
			ServicoListener clienteC = new ClienteC();
			ServicoListener clienteCdistribuido = (ServicoListener) UnicastRemoteObject.exportObject(clienteC, 0);

			Registry registry = LocateRegistry.getRegistry(porta);
			Servico servicoRemoto = (Servico) registry.lookup(nomeServico);
			
			servicoRemoto.addListener(clienteCdistribuido, valorRelogioLocal);

			System.out.println("Relogio do Cliente C: " + df.format(horas) + ":" + df.format(minutos));
			servicoRemoto.setRelogio3();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void atualizarRelogio(int resultado) throws RemoteException {
		System.out.println("Servidor retornou para o Cliente C: " + resultado + " minutos");
		int ajusteRelogioLocal = valorRelogioLocal+resultado;
		System.out.println("Ajuste do Cliente C: " + df.format(ajusteRelogioLocal/60) + ":" 
						+ df.format(ajusteRelogioLocal%60));
	}
}