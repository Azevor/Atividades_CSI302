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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface remota que permite chamada de método que atualiza os relógios dos clientes.
 * @author Gildo Tiago
 *
 */
public interface ServicoListener extends Remote {
	void atualizarRelogio(int resultado) throws RemoteException;
}