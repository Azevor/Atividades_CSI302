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

package Servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Defini��o do servidor, aberto para conex�o de clientes.
 * @author Gildo Tiago
 *
 */
public class Servidor {

	public static void main(String args[]) {
		try {
			String bind = "BerkeleyRMI"; // Nome do servi�o a ser conectado pelo cliente.
			int porta = 8242;

			Servico servico = new ImplementacaoServico();
			Servico servicoDistribuido = (Servico) UnicastRemoteObject.exportObject(servico, 0);

			Registry registry = LocateRegistry.createRegistry(porta); // Servi�o de registro de diret�rio.
			registry.bind(bind, servicoDistribuido); // Nomea��o para os clientes encontrarem os servi�os.
			System.out.printf("Servico disponivel: %s%n", bind);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}