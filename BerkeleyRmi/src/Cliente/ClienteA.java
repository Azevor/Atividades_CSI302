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
public class ClienteA implements ServicoListener {
	/*
	 * Defini��o de alguns valores para testar a sincroniza��o dos rel�gios. 
	*/
	private static final int horas = 3;
	private static final int minutos = 20;
	private static final int valorRelogioLocal = (horas * 60 + minutos);
	private static DecimalFormat df = new DecimalFormat("00");

	public static void main(String[] args) {
		try {
			String nomeServico = "BerkeleyRMI"; // Nome do servi�o registrado no servidor.
			int porta = 8242;
			
			ServicoListener clienteA = new ClienteA();
			ServicoListener clienteAdistribuido = (ServicoListener) UnicastRemoteObject.exportObject(clienteA, 0);

			// Requisi��o de registro pelo cliente para acessar o servi�o do servidor.
			Registry registry = LocateRegistry.getRegistry(porta);
			Servico servicoRemoto = (Servico) registry.lookup(nomeServico);
			
			// Acesso ao m�todo remoto que adiciona o proxy cliente no servidor, e a hora local.
			servicoRemoto.addListener(clienteAdistribuido, valorRelogioLocal);

			System.out.println("Relogio do Cliente A: " + df.format(horas) + ":" + df.format(minutos));
			servicoRemoto.setRelogio1();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * M�todo remoto acessado pelo servidor para receber o ajuste do rel�gio local.
	 */
	@Override
	public void atualizarRelogio(int resultado) throws RemoteException {
		// Ao receber a quantidade, em minutos, este valor � adicionado ao rel�gio local, pra mais ou pra menos.
		System.out.println("Servidor retornou para o Cliente A: " + resultado + " minutos");
		int ajusteRelogioLocal = valorRelogioLocal+resultado;
		// Exibe a hora ajustada na tela.
		System.out.println("Ajuste do Cliente A: " + df.format(ajusteRelogioLocal/60) + ":" 
						+ df.format(ajusteRelogioLocal%60));
	}
}