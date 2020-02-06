package ClienteChat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class Thread_cliente implements Runnable{
    DefaultListModel<String> modelo;
    DataInputStream in;
    javax.swing.JTextArea area;
    javax.swing.JList<String> lista;
    Socket servidor;
    Thread_cliente(DataInputStream i,javax.swing.JTextArea a,javax.swing.JList<String> l,Socket servidor) {
        in=i;
        area=a;
        lista=l;
        this.servidor=servidor;
    }

    @Override
    public void run() {
        //read modelo OBJECT
        ObjectInputStream entrada;
        try {
            entrada = new ObjectInputStream(servidor.getInputStream());
            DefaultListModel<String> dato = (DefaultListModel<String>) entrada.readObject();
            modelo=dato;
            lista.setModel(modelo);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Thread_cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        while(true){
            try {
                if(in.readBoolean())
                    area.append(in.readUTF());
                else{
                    if(in.readBoolean()){
                        modelo.addElement(in.readUTF());
                        lista.setModel(modelo);
                    }else{
                        modelo.removeElement(in.readUTF());
                        lista.setModel(modelo);
                    }
                }
                    
            } catch (IOException ex) {
                Logger.getLogger(Thread_cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
