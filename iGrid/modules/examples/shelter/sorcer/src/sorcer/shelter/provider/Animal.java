package sorcer.shelter.provider;

import java.io.Serializable;

public class Animal implements Serializable {
	protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
		
    public boolean equals(Object object) {
        if (object instanceof Animal) {
            Animal otherAnimal = (Animal) object;
            return (this.getName() == otherAnimal.getName());
        }
        return false;
    }

    public String toString() {
        return name;
    }
	
	public String value() {
		return "" + name;
	}
}
