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

package Servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Definição do servidor, aberto para conexão de clientes.
 * @author Gildo Tiago
 *
 */
public class Servidor {

	public static void main(String args[]) {
		try {
			String bind = "BerkeleyRMI"; // Nome do serviço a ser conectado pelo cliente.
			int porta = 8242;

			Servico servico = new ImplementacaoServico();
			Servico servicoDistribuido = (Servico) UnicastRemoteObject.exportObject(servico, 0);

			Registry registry = LocateRegistry.createRegistry(porta); // Serviço de registro de diretório.
			registry.bind(bind, servicoDistribuido); // Nomeação para os clientes encontrarem os serviços.
			System.out.printf("Servico disponivel: %s%n", bind);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}