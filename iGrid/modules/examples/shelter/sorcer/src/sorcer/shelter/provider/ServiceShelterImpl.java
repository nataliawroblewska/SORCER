package sorcer.shelter.provider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.jini.admin.Administrable;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import sorcer.core.SorcerConstants;
import sorcer.core.provider.Provider;
import sorcer.core.proxy.Partnership;
import sorcer.core.proxy.RemotePartner;
import sorcer.service.Context;
import sorcer.service.Exertion;
import sorcer.service.ExertionException;
import sorcer.util.Log;
import java.util.*;

public class ServiceShelterImpl implements Shelter, ServiceShelter, SorcerConstants {

	private static Logger logger = Log.getTestLog();

	private LinkedList<Animal> animalsInShelter;

	
	public Context getAnimalsInShelter(Context shelter) throws RemoteException{
			return process(shelter, ServiceShelter.ANIMALS);
			}

	public Context addAnimal(Context shelter) throws RemoteException{
			return process(shelter, ServiceShelter.ADD);}

	public Context deleteAnimal(Context shelter) throws RemoteException {
			return process(shelter, ServiceShelter.REMOVE);
			}
			
			

	private Context process(Context context, String selector)
			throws RemoteException {
		try {
			logger.info("input context: \n" + context);

			LinkedList<Animal> result = null;
			Animal animal = null;
			if (selector.equals(ServiceShelter.ANIMALS)) {
				result = getAnimalsInShelter();
			} else if (selector.equals(ServiceShelter.ADD)) {
				animal = (Animal) context.getValue(ServiceShelter.ADD + CPS
						+ ServiceShelter.AMOUNT);
				addAnimal(animal);
				result = getAnimalsInShelter();
			} else if (selector.equals(ServiceShelter.REMOVE)) {
				animal = (Animal) context.getValue(ServiceShelter.REMOVE
						+ CPS + ServiceShelter.AMOUNT);
				deleteAnimal(animal);
				result = getAnimalsInShelter();
			}
			
			if (context.getReturnPath() != null) {
				context.setReturnValue(result);
			}
			logger.info(selector + " result: \n" + result);
			String outputMessage = "processed by " + getHostname();
			context.putValue(selector + CPS +
					ServiceShelter.ANIMALS + CPS + ServiceShelter.AMOUNT, result);
			context.putValue(ServiceShelter.COMMENT, outputMessage);

		} catch (Exception ex) {
			// ContextException, UnknownHostException
			throw new RemoteException(selector + " process execption", ex);
		}
		return context;
	}

	public ServiceShelterImpl(LinkedList<Animal> startingAnimalsInShelter) throws RemoteException {
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
	  
	   	Animal a = checkIsThereSuchAnimalInShelter(animalToDelete);
        animalsInShelter.remove(a);
        return;
    }
	
	private Animal checkIsThereSuchAnimalInShelter(Animal animalToDelete)
        throws NoSuchAnimalException {
		
		for (Animal a : animalsInShelter){
			if(a.getName().matches(animalToDelete.getName())){
				return a;
			}
		}       
           throw new NoSuchAnimalException();
       
    }
	
	private void checkForDoubledAnimal(Animal newAnimal)
		throws DoubledAnimalException{
		for (Animal a : animalsInShelter){
			if(a.getName().matches(newAnimal.getName())){
				throw new DoubledAnimalException(); 
			}
		}       
		
	}

	private Provider partner;

	private Administrable admin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see sorcer.core.provider.proxy.Partnership#getPartner()
	 */
	public Remote getInner() throws RemoteException {
		return (Remote) partner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sorcer.base.Service#service(sorcer.base.Exertion)
	 */
	public Exertion service(Exertion exertion, Transaction transaction)
			throws RemoteException, ExertionException, TransactionException {
		return partner.service(exertion, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jini.admin.Administrable#getAdmin()
	 */
	public Object getAdmin() throws RemoteException {
		return admin;
	}

	public void setInner(Object provider) {
		partner = (Provider) provider;
	}

	public void setAdmin(Object admin) {
		this.admin = (Administrable) admin;
	}

	/**
	 * Returns name of the local host.
	 * 
	 * @return local host name
	 * @throws UnknownHostException
	 */
	private String getHostname() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
}
