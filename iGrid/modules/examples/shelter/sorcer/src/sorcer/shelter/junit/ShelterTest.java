package sorcer.shelter.junit;

import static sorcer.eo.operator.args;
import static sorcer.eo.operator.context;
import static sorcer.eo.operator.exert;
import static sorcer.eo.operator.get;
import static sorcer.eo.operator.in;
import static sorcer.eo.operator.job;
import static sorcer.eo.operator.jobContext;
import static sorcer.eo.operator.parameterTypes;
import static sorcer.eo.operator.result;
import static sorcer.eo.operator.sig;
import static sorcer.eo.operator.task;
import static sorcer.eo.operator.value;

import java.rmi.RMISecurityManager;
import java.util.logging.Logger;
import java.util.*;

import org.junit.Test;

import sorcer.shelter.provider.Shelter;
import sorcer.shelter.provider.Animal;
import sorcer.shelter.provider.ServiceShelter;
import sorcer.core.SorcerConstants;
import sorcer.service.Job;
import sorcer.service.ServiceExertion;
import sorcer.service.Task;
import sorcer.util.Sorcer;

@SuppressWarnings("unchecked")
public class ShelterTest implements SorcerConstants {

 	private final static Logger logger = Logger
 			.getLogger(ShelterTest.class.getName());
 
 	static {
 		ServiceExertion.debug = true;
 		System.setProperty("java.security.policy", Sorcer.getHome()
 				+ "/configs/policy.all");
 		System.setSecurityManager(new RMISecurityManager());
 		Sorcer.setCodeBase(new String[] { "jeri-shelter-dl.jar" });
 		System.out.println("CLASSPATH :" + System.getProperty("java.class.path"));
 		System.setProperty("java.protocol.handler.pkgs", "sorcer.util.url|org.rioproject.url");
 	}
 	
 	@Test
 	public void shelter1AnimalsTest() throws Exception {
 		Task t1 = task("t1",
 				sig("getAnimalsInShelter", ServiceShelter.class, "Shelter1"),
 				context("animals1", result("animals/amount")));
 
 		logger.info("t1 value: " + value(t1));
 	}
 	
 	@Test
 	public void shelter1AddTest() throws Exception {
 		Task t2 = task("t2",
 				sig("addAnimal", ServiceShelter.class, "Shelter1"),
 				context("add", in("add/amount", new Animal("kazio"))));
 		t2 = exert(t2);
 		logger.info("t1 context: " + context(t2));
 		logger.info("t1 value: " + get(t2, "add/animals/amount"));
 	}
 	
 	@Test
 	public void shelter1RemoveTest() throws Exception {
 		Task t4 = task("t4",
 				sig("deleteAnimal", ServiceShelter.class, "Shelter1"),
 				context("remove", in("remove/amount", new Animal("kazio"))));
 		
 		t4 = exert(t4);
 		logger.info("t3 context: " + context(t4));
 		logger.info("t3 value: " + get(t4, "remove/animals/amount"));
 	}
 
}
