package sorcer.shelter.provider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import sorcer.core.SorcerConstants;
import sorcer.core.provider.ServiceTasker;
import sorcer.service.Context;
import sorcer.util.Log;
import java.util.*;

import com.sun.jini.start.LifeCycle;

@SuppressWarnings("rawtypes")
public class ShelterProvider extends ServiceTasker implements Shelter,
		ServiceShelter, SorcerConstants {

	private static Logger logger = Log.getTestLog();

	private LinkedList<Animal> animalsInShelter;

	/**
	 * Constructs an instance of the SORCER shelter provider implementing
	 * SorcerShelter and Shelter. This constructor is required by Jini 2 life
	 * cycle management.
	 * 
	 * @param args
	 * @param lifeCycle
	 * @throws Exception
	 */
	public ShelterProvider(String[] args, LifeCycle lifeCycle) throws Exception {
		super(args, lifeCycle);
		String animal = getProperty("provider.animalsInShelter");
		animalsInShelter = new LinkedList<Animal>();
		animalsInShelter.add(new Animal(animal));
	}

	public Context getAnimalsInShelter(Context shelter) throws RemoteException,
			ShelterException{
			return process(shelter, ServiceShelter.ANIMALS);
			}

	public Context addAnimal(Context shelter) throws RemoteException,
			ShelterException{
			return process(shelter, ServiceShelter.ADD);}

	public Context deleteAnimal(Context shelter) throws RemoteException,
			ShelterException {
			return process(shelter, ServiceShelter.REMOVE);
			}
	

	private Context process(Context context, String selector)
			throws RemoteException, ShelterException {
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
			// set return value
			if (context.getReturnPath() != null) {
				context.setReturnValue(result);
			}
			logger.info(selector + " result: \n" + result);
			String outputMessage = "processed by " + getHostname();
			context.putValue(selector + CPS +
					ServiceShelter.ANIMALS + CPS + ServiceShelter.AMOUNT, result);
			context.putValue(ServiceShelter.COMMENT, outputMessage);

		} catch (Exception ex) {
			throw new ShelterException(ex);
		}
		return context;
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
