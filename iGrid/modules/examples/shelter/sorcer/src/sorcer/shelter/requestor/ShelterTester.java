package sorcer.shelter.requestor;

import java.rmi.RMISecurityManager;
import java.util.logging.Logger;

import sorcer.shelter.provider.Animal;
import sorcer.shelter.provider.ServiceShelter;
import sorcer.core.context.ServiceContext;
import sorcer.core.exertion.NetJob;
import sorcer.core.exertion.NetTask;
import sorcer.core.signature.NetSignature;
import sorcer.service.Context;
import sorcer.service.Job;
import sorcer.util.Log;
import sorcer.util.Sorcer;

@SuppressWarnings("rawtypes")
public class ShelterTester {

	private static Logger logger = Log.getTestLog();

	String CPS = "/";
	
	public static void main(String[] args) throws Exception {
		System.setSecurityManager(new RMISecurityManager());
		Job result = new ShelterTester().test();
		logger.info("job context: \n" + result.getJobContext());
	}

	private Job test() throws Exception {
		Job result = (Job)getJob().exert();
		return result;
	}

	private Job getJob() throws Exception {
		NetTask task1 = getAddTask();
		NetTask task2 = getRemoveTask();
		NetJob job = new NetJob("shelter");
		job.addExertion(task1);
		job.addExertion(task2);
		return job;
	}

	private NetTask getAddTask() throws Exception {
		ServiceContext context = new ServiceContext(ServiceShelter.SHELTER);
		context.putValue(ServiceShelter.ADD + CPS + ServiceShelter.AMOUNT,
				new Animal("zuzio"));
		context.putValue(ServiceShelter.ANIMALS + CPS + ServiceShelter.AMOUNT,
				Context.none);
		NetSignature signature = new NetSignature("addAnimal",
				ServiceShelter.class, Sorcer.getActualName("Shelter1"));
		NetTask task = new NetTask("shelter-add", signature);
		task.setContext(context);
		return task;
	}

	private NetTask getRemoveTask() throws Exception {
		ServiceContext context = new ServiceContext(ServiceShelter.SHELTER);
		context.putValue(ServiceShelter.REMOVE + CPS + ServiceShelter.AMOUNT,
				new Animal("zuzio"));
		context.putValue(ServiceShelter.ANIMALS + CPS + ServiceShelter.AMOUNT,
				Context.none);
		NetSignature signature = new NetSignature("deleteAnimal",
				ServiceShelter.class, Sorcer.getActualName("Shelter1"));
		NetTask task = new NetTask("shelter-remove", signature);
		task.setContext(context);
		return task;
	}
	
}
