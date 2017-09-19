package fileserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable {
    
    private String caminho;
    private final int porta;
    
    public Servidor(int porta) {
        this.porta = porta;
    }

    /**
     * 
     * @param c Caminho do diretório de arquivos disponiveis
     */
    public void iniciar() {
        try {
            //Criar a conex�o/ouvinte servidor
            ServerSocket escutarPorta = new ServerSocket(porta);
            //Aguardar por novas conex�es
            while (true) {
                Socket cliente = escutarPorta.accept();
                new Conexao(cliente, getCaminho());
             //   escutarPorta.close();
            }
        } catch (IOException ex) {
            System.out.println("Servidor ServerSocket IO: " + ex.getMessage());
        }
    }

    /**
     * @return the porta
     */
    public int getPorta() {
        return porta;
    }

    @Override
    public void run() {
        iniciar();
    }

    /**
     * @return the caminho
     */
    public String getCaminho() {
        return caminho;
    }

    /**
     * @param caminho the caminho to set
     */
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

}
