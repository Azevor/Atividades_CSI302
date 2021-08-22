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

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Implementação do objeto remoto que recebe a comunicação dos clientes.
 * @author Gildo Tiago
 *
 */
public class ImplementacaoServico implements Servico {
	/*
	 * HashMap que adiciona como chave a interface cliente (proxy) e
	 * armazena sua hora como respectivo valor.
	 */
	private HashMap<ServicoListener, Integer> listeners = new HashMap<>();
	
	/* Definição de alguns valores para teste de sincronização dos relógios. */
	private int horas = 3;
	private int minutos = 15;
	private int valorRelogioLocal = (horas * 60 + minutos);
	private DecimalFormat df = new DecimalFormat("00"); // Exibir dois dígitos da hora.
	private int mediaDiferencas = 0; // Armazenar a média das diferenças de horário servidor-clientes.

	/* Verificar se armazenou a hora dos respectivos clientes. */
	private boolean setouRelogio1;
	private boolean setouRelogio2;
	private boolean setouRelogio3;
	private boolean setouRelogio4;

	/* Método remoto que recebe o proxy cliente conectado e sua respectiva hora. */
	@Override
	public void addListener(ServicoListener listener, int valorRelogio) throws RemoteException {
		listeners.put(listener, valorRelogio);
		// Exibindo a hora local do servidor.
		System.out.println("Hora atual do servidor: " + df.format(valorRelogioLocal/60) + ":" 
						+ df.format(valorRelogioLocal%60));
	}

	/*
	 * Configuração feita para receber 4 clientes, neste trabalho prático.
	 */
	
	/* Método remoto que o cliente utiliza para setar (confirmar informação de hora) */
	@Override
	public void setRelogio1() throws RemoteException { // Cliente A
		setouRelogio1 = true;
		verifica();
	}

	@Override
	public void setRelogio2() throws RemoteException { // Cliente B
		setouRelogio2 = true;
		verifica();
	}
	
	@Override
	public void setRelogio3() throws RemoteException { // Cliente C
		setouRelogio3 = true;
		verifica();
	}
	
	@Override
	public void setRelogio4() throws RemoteException { // Cliente D
		setouRelogio4 = true;
		verifica();
	}
	
	/**
	 * Calcular diferença de horario entre o servidor e os clientes.
	 * @param HashMap<Cliente, Horario> : clientes
	 * @return HashMap<Cliente, Diferença_Horario> : diferencas
	 */
	private HashMap<ServicoListener, Integer> sincronizar(HashMap<ServicoListener, Integer> clientes) {
		HashMap<ServicoListener, Integer> diferencas = new HashMap<>();
		int totalDiferencas = 0;
		/*
		 * De acordo com o algoritmo de Berkeley, para cada horario do cliente,
		 * será calculado a diferença entre o servidor e o cliente.
		 */
		for (ServicoListener keyListener : clientes.keySet()) {
			diferencas.put(keyListener, clientes.get(keyListener)-valorRelogioLocal);
			totalDiferencas += clientes.get(keyListener)-valorRelogioLocal;
		}
		// Cálculo da média das diferenças de horário do cliente.
		mediaDiferencas = totalDiferencas/(1+diferencas.size()); // Soma-se 1 para contar com o servidor na média.
		return diferencas;
	}
	
	/**
	 * Método local que verifica se todos os clientes conectados informaram suas respectivas horas.
	 */
	private void verifica() {
		// Caso o servidor esteja ciente de todos os horários, realiza-se a sincronização dos horários.
		if (setouRelogio1 && setouRelogio2 && setouRelogio3 && setouRelogio4) {
			HashMap<ServicoListener, Integer> diferencas = sincronizar(listeners);
			System.out.println("Média calculada = " + mediaDiferencas + " minutos");
			valorRelogioLocal += mediaDiferencas;
			// Relógio do servidor recebe a média das diferenças entre os relógios e exibe seu novo horário.
			System.out.println("Ajuste do servidor: " + df.format(valorRelogioLocal/60) + ":" 
							+ df.format(valorRelogioLocal%60));
			
			// Todo cliente conectado receberá a diferença, pra mais ou pra menos, de ajuste do relógio.
			for (ServicoListener keyListener : listeners.keySet()) {
				int diferenca = mediaDiferencas-diferencas.get(keyListener);
				try {
					keyListener.atualizarRelogio(diferenca);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			setouRelogio1 = false;
			setouRelogio2 = false;
			setouRelogio3 = false;
		}
	}
}