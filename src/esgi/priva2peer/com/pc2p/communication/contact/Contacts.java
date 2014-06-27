package esgi.priva2peer.com.pc2p.communication.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contacts
{
	private static final Map<String, Contact> contactList = new HashMap<String, Contact>();

	synchronized public static Contact getContact(String pseudo)
	{
		return contactList.get(pseudo);
	}

	synchronized public static Contact addContact(Contact contact)
	{
		Contact oldContact = contactList.get(contact.getPseudo());
		contactList.put(contact.getPseudo(), contact);
		return oldContact;
	}

	synchronized public static Contact removeContact(String pseudo)
	{
		return contactList.remove(pseudo);
	}

	synchronized public static List<String> getAllPseudo()
	{
		return new ArrayList<String>(contactList.keySet());
	}

	synchronized public static List<Contact> getAllContact()
	{
		return new ArrayList<Contact>(contactList.values());
	}

}
