package ServidorChat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Multiples_Threads implements Runnable{
    Grupo grupo;
    ServerSocket server;
    Socket cliente;
    Thread t = null;
    Multiples_Threads(Grupo grupo,Socket cliente,ServerSocket server) {
        this.grupo=grupo;
        this.cliente=cliente;
        this.server=server;
    }
    private void representarUsuario(String usuario){
        grupo.ponerUsuario(usuario);
        
    }

    private void representarMensaje(String mensaje){
        grupo.ponerMensaje(mensaje);
    }
    @Override
    public void run() {
        DataInputStream datain;
        String lectura;
        if (cliente==null){
            //crear THREADs de lectura
            boolean ejecutar=true;char salir;//variables que se ocupan de terminar el servidor
            Socket cli;//donde se crearan los nuevos clientes

            
            //guardar un array de los clientes///////////////////////////////////////////////////
            try {
                while(ejecutar){
                    cli= server.accept();
                    datain = new DataInputStream(cli.getInputStream());
                    lectura=datain.readUTF();
                    salir=lectura.charAt(0);
                    if (salir=='*'){
                        ejecutar=false;
                        //FUNCION DE TERMINAR TODOS LOS THREADS
                    }else{
                        representarUsuario(lectura);
                        t=(new Thread(new Multiples_Threads(grupo,cli,null)));
                        t.start();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Multiples_Threads.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
            //solo operar sobre un cliente
            try{
                while(true){
                    datain = new DataInputStream(cliente.getInputStream());
                    lectura=datain.readUTF();
                    representarMensaje(lectura);
                }
            }catch(Exception e){
                //SE CIERRA SI EL CLIENTE SE DESACTIVA
            }
        }
    }
}
