package ServidorChat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class Grupo extends javax.swing.JPanel {
    DefaultListModel<String> modelo;
    Collection<Socket> coleccion;
    public Grupo(Collection<Socket> coleccion,DefaultListModel<String> modelo) {
        initComponents();
        this.coleccion=coleccion;
        this.modelo=modelo;
    }
    
    public void ponerMensaje(String mensaje){

            String enviar=mensaje+"\n";
            jTextArea1.append(enviar);
            envio(enviar,null);

    }
    
    public void ponerUsuario(String usuario){

            String enviar="< ["+usuario+"] se ha unido al chat>\n";
            jTextArea1.append(enviar);
            modelo.addElement(usuario);
            jList1.setModel(modelo);
            envio(enviar,usuario);

    }
    
    public void borrarUsuario(String usuario){
        modelo.removeElement(usuario);
        jList1.setModel(modelo);
        envio(null,usuario);
    }
    
    private synchronized void envio(String envio,String usuario){
        //enviar un metodo X que elija si es mensaje o usuario
        //true mensaje, false usuario (true añadir,false borrar)
        Iterator itr=coleccion.iterator();
        Socket c;
        DataOutputStream dataout;
        if (envio==null){
            while(itr.hasNext()) {
                try {
                    c = (Socket)itr.next();
                    dataout = new DataOutputStream(c.getOutputStream());
                    dataout.writeBoolean(false);
                    dataout.writeBoolean(false);
                    dataout.writeUTF(usuario);
                } catch (IOException ex) {
                    Logger.getLogger(Grupo.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en borrar usuario");
                }
            }
        }else{
            if (usuario==null){
                while(itr.hasNext()) {
                    try {
                        c = (Socket)itr.next();
                        dataout = new DataOutputStream(c.getOutputStream());
                        dataout.writeBoolean(true);
                        dataout.writeUTF(envio);
                    } catch (IOException ex) {
                        Logger.getLogger(Grupo.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Error en poner mensaje");
                    }
                }
            }else{
                while(itr.hasNext()) {
                    try {
                        c = (Socket)itr.next();
                        dataout = new DataOutputStream(c.getOutputStream());
                        dataout.writeBoolean(true);
                        dataout.writeUTF(envio);
                        dataout.writeBoolean(false);
                        dataout.writeBoolean(true);
                        dataout.writeUTF(usuario);
                    } catch (IOException ex) {
                        Logger.getLogger(Grupo.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Error en poner usuario");
                    }
                }
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(400, 400));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
        jScrollPane1.setViewportView(jTextArea1);

        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
