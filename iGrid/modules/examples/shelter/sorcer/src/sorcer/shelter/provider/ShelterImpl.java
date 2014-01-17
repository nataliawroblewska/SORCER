package sorcer.shelter.provider;

import java.rmi.RemoteException;
import java.util.*;

public class ShelterImpl implements Shelter {
	private LinkedList<Animal> animalsInShelter;

    public ShelterImpl(LinkedList<Animal> startingAnimalsInShelter)
        throws RemoteException {
        animalsInShelter = startingAnimalsInShelter;
    }
	
	public LinkedList<Animal> getAnimalsInShelter()
        throws RemoteException {
        return animalsInShelter;
    }

    public void addAnimal(Animal newAnimal) 
        throws RemoteException, DoubledAnimalException {
        checkForDoubledAnimal(newAnimal);
        animalsInShelter.add(newAnimal);
        return;
    }

   public void deleteAnimal(Animal animalToDelete)
        throws RemoteException, NoSuchAnimalException {
        checkIsThereSuchAnimalInShelter(animalToDelete);
        animalsInShelter.remove(animalToDelete);
        return;
    }


	private void checkIsThereSuchAnimalInShelter(Animal animalToDelete)
        throws NoSuchAnimalException {
        if (!animalsInShelter.contains(animalToDelete)) {
            throw new NoSuchAnimalException();
        }
    }
	
	private void checkForDoubledAnimal(Animal newAnimal)
		throws DoubledAnimalException{
		if(animalsInShelter.contains(newAnimal)){
			throw new DoubledAnimalException(); 
		}
	}
}
