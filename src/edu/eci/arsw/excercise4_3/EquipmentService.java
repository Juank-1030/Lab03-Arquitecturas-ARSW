package edu.eci.arsw.excercise4_3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface EquipmentService extends Remote {
    List<String> consultarEquipos() throws RemoteException;
    String consultarEquipo(String codigo) throws RemoteException;
    boolean reservarEquipo(String codigo) throws RemoteException;
    boolean liberarEquipo(String codigo) throws RemoteException;
}
