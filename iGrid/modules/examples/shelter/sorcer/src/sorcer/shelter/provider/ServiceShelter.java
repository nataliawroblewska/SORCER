package sorcer.shelter.provider;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sorcer.service.Context;

@SuppressWarnings("rawtypes")
public interface ServiceShelter extends Remote {

	public Context getAnimalsInShelter(Context shelter) throws RemoteException,
			ShelterException;

	public Context addAnimal(Context shelter) throws RemoteException,
			ShelterException;

	public Context deleteAnimal(Context shelter) throws RemoteException,
			ShelterException;

	public final static String SHELTER = "shelter";

	public final static String ADD = "add";

	public final static String REMOVE = "remove";

	public final static String AMOUNT = "amount";

	public final static String ANIMALS = "animals";

	public final static String COMMENT = "comment";
}
