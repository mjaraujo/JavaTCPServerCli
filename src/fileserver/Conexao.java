package fileserver;

import fileserver.ManipuladorEntradas.RETORNO;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao extends Thread {
    
    ManipuladorEntradas mpEntradas;
    DataInputStream entrada;
    DataOutputStream saida;
    Socket cliente;

    public Conexao(Socket clienteSocket,String caminhoArquivosServer) throws IOException {
         mpEntradas = new ManipuladorEntradas(caminhoArquivosServer);
        cliente = clienteSocket;
        String resp = "";

        entrada = new DataInputStream(cliente.getInputStream());

        RETORNO processar = mpEntradas.processar(entrada.readUTF());
        if (processar == RETORNO.ENVIAR_RESPOSTA_LISTA) {
            for (String s : mpEntradas.getRespostaLista()) {
                resp += s;
                resp += ('\n');
            }
            saida = new DataOutputStream(cliente.getOutputStream());
            saida.writeUTF(resp);

        }
        if (processar == RETORNO.ENVIAR_RESPOSTA_ARQUIVO) {
            saida = new DataOutputStream(cliente.getOutputStream());

            try {
                File file = new File(mpEntradas.getPathArquivo());
                InputStream in = new FileInputStream("/home/marcio/Documentos/" + file);
                long length = file.length();
                byte[] bytes = new byte[16 * 1024];

                int count;
                while ((count = in.read(bytes)) > 0) {
                    saida.write(bytes, 0, count);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.start();
        saida.close();
        entrada.close();
    }

    public void run() {
        try {
            String dado = entrada.readUTF();
            saida.writeUTF("Dados recebidos...");
            entrada.close();
            saida.close();
        } catch (IOException ex) {
            System.out.println("Conexao::run1 IO: " + ex.getMessage());
        } finally {
            try {
                cliente.close();
            } catch (IOException ex) {
                System.out.println("Conexao::run2 IO: " + ex.getMessage());
            }
        }
    }

}
