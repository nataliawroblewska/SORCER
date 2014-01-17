package sorcer.shelter.provider;

public class ShelterException extends Exception {
	public boolean removeSucceeded;

	public ShelterException(Exception cause) {
		super(cause);
	}
}
