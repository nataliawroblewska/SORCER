package sorcer.shelter.provider;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface Shelter extends Remote {
	public LinkedList<Animal> getAnimalsInShelter()
        throws RemoteException;
    public void addAnimal(Animal newAnimal) 
        throws RemoteException, DoubledAnimalException;
    public void deleteAnimal(Animal animalToDelete)
        throws RemoteException, NoSuchAnimalException;		
}
