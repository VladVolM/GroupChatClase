package ServidorChat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class Multiples_Threads implements Runnable{
    DefaultListModel<String> modelo;
    Grupo grupo;
    ServerSocket server;
    Socket cliente;
    String nombre;
    Collection<Socket> coleccion;
    Thread t = null;
    
    Multiples_Threads(Grupo grupo,Socket cliente,ServerSocket server,String nombre,Collection<Socket> coleccion,DefaultListModel<String> modelo) {
        this.grupo=grupo;
        this.cliente=cliente;
        this.server=server;
        this.nombre=nombre;
        this.coleccion=coleccion;
        this.modelo=modelo;
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

            ObjectOutputStream salida;
            
            //guardar un array de los clientes///////////////////////////////////////////////////
            try {
                while(ejecutar){
                    cli= server.accept();
                    coleccion.add(cli);
                    datain = new DataInputStream(cli.getInputStream());
                    lectura=datain.readUTF();
                    salir=lectura.charAt(0);
                    if (salir=='*'){
                        ejecutar=false;
                        //FUNCION DE TERMINAR TODOS LOS THREADS
                    }else{
                        salida = new ObjectOutputStream(cli.getOutputStream());

                        salida.writeObject(modelo);// enviando el objeto
                        representarUsuario(lectura);
                        t=(new Thread(new Multiples_Threads(grupo,cli,null,lectura,coleccion,null)));
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
                    representarMensaje("{"+nombre+"}:"+lectura);
                }
            }catch(IOException e){
                String enviar="< ["+nombre+"] ha abandonado el chat>";
                coleccion.remove(cliente);
                representarMensaje(enviar);
                grupo.borrarUsuario(nombre);
            }
        }
    }
}
