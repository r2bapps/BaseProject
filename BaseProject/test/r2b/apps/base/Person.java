package r2b.apps.base;

import java.util.ArrayList;
import java.util.List;

public class Person {
	
	private static final String[] names = {"Jenson", "Fernando", "Kimi", "Sebastian", "Nico", "Lewis"};

	private String name;
	private long id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public static List<Person> getPeople() {
		 List<Person> list = new ArrayList<Person>(names.length);
		 Person p;
		for(int i = 0; i < names.length; i++) {
			p = new Person();
			p.setId(i);
			p.setName(names[i]);
			list.add(p);
		}
		return list;
	}
	
}
