package ServidorChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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

    private void terminarTodo(Collection<Thread> threads) {
        Iterator itr = threads.iterator();
        Thread thred;
        while(itr.hasNext()) {
            thred = (Thread)itr.next();
            thred.stop();
        }
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
            boolean ejecutar=true,nombreRepetido=false;char salir;//variables que se ocupan de terminar el servidor
            Socket cli;//donde se crearan los nuevos clientes
            DataOutputStream dataout;
            ObjectOutputStream salida;
            Collection<Thread> threads_totales= new LinkedList<>();
            try {
                while(ejecutar){
                    cli= server.accept();
                    coleccion.add(cli);
                    datain = new DataInputStream(cli.getInputStream());
                    dataout = new DataOutputStream(cli.getOutputStream());
                    lectura=datain.readUTF();
                    /*do{
                        lectura=datain.readUTF();
                        
                        if(modelo.indexOf(lectura)==-1){
                            nombreRepetido=false;
                            dataout.writeBoolean(true);
                        }else{
                            nombreRepetido=true;
                            dataout.writeBoolean(false);
                        }
                    }while(nombreRepetido);*/
                    salir=lectura.charAt(0);
                    if (salir=='*'){
                        ejecutar=false;
                        representarMensaje("<<<<SERVIDOR APAGODO>>>>");
                        terminarTodo(threads_totales);
                    }else{
                        salida = new ObjectOutputStream(cli.getOutputStream());

                        salida.writeObject(modelo);// enviando el objeto
                        representarUsuario(lectura);
                        t=(new Thread(new Multiples_Threads(grupo,cli,null,lectura,coleccion,null)));
                        t.start();
                        threads_totales.add(t);
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
