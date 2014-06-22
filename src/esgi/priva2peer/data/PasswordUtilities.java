package esgi.priva2peer.data;

import java.util.HashMap;
import java.util.regex.Pattern;

public class PasswordUtilities
{

	/**
	 * Retourne <code>Vrai</code> si le mot de passe contient un caractère
	 * spécial, <code>Faux</code> sinon
	 * 
	 * @param pw {String}: le mot de passe à vérifier
	 * @return {boolean} <code>true</code> si
	 *         pw.contains(["-!\"§$%&/()=?+*~#'_:.,;@\^<>£¤µ"]) == true,
	 *         <code>false</code> sinon
	 **/
	private static boolean contientCaracSpe(String pw)
	{
		boolean res = false;
		int i = 0;
		CharSequence c;
		do
		{
			c = Constants.SPEC_CHARS[i];
			if (pw.contains(c))
				res = true;
			i++ ;
		}while (true != res && Constants.SPEC_CHARS.length > i); // Tant que res
																	// est faux
																	// ET que i
																	// <
																	// longueur
																	// de
																	// SPEC_CHAR
		return res;
	}

	/**
	 * Vérifie que le pw contient au moins une majuscule
	 * 
	 * @param pw {String}: le mot de passe à vérifier
	 * @return {boolean} <code>true</code> si le pw contient au moins une
	 *         majuscule, <code>false</code> sinon
	 **/
	private static boolean contientMaj(String pw)
	{
		return Pattern.compile("(?=.*[A-Z])").matcher(pw).find();
	}

	/**
	 * Vérifie que le pw contient au moins une minuscule
	 * 
	 * @param pw {String}: le mot de passe à vérifier
	 * @return {boolean} <code>true</code> si le pw contient au moins une
	 *         minuscule, <code>false</code> sinon
	 **/
	private static boolean contientMin(String pw)
	{
		return Pattern.compile("(?=.*[a-z])").matcher(pw).find();
	}

	/**
	 * Vérifie que le pw contient au moins un chiffre
	 * 
	 * @param pw {String}: le mot de passe à vérifier
	 * @return {boolean} <code>true</code> si le pw contient au moins un
	 *         chiffre, <code>false</code> sinon
	 **/
	private static boolean contientNombre(String pw)
	{
		return Pattern.compile(".*\\d+.*").matcher(pw).find();
	}

	/**
	 * Retourne <code>Vrai</code> si la longueur du mot de passe est supérieure
	 * à 8, <code>Faux</code> sinon
	 * 
	 * @param pw {String}: le mot de passe à vérifier
	 * @return {boolean} <code>true</code> si pw.length >= 8, <code>false</code>
	 *         sinon
	 **/
	private static boolean estSuffisammentLong(String pw)
	{
		return pw.length() >= 8;
	}

	/**
	 * Vérifie que le mot de passe ne contient que les caractères acceptés
	 * 
	 * @param pw {String]: Le mot de passe à vérifier
	 * @return {boolean}: <code>true</code> si le mot de passe valide les
	 *         attentes, <code>false</code> sinon
	 **/
	private static boolean seulementAcceptes(String pw)
	{
		return Pattern.compile("^[-!\"§$%&/()=?+*~#'_:\\.,@^<>£¤µa-zA-Z0-9]+$").matcher(pw).find();
	}

	/**
	 * Return a HashMap containing the tests name and a boolean indicating if it
	 * has been validated, for the given password
	 * 
	 * @param pw {String}: the password to check
	 * @return {HashMap&lt;String, Boolean&gt;} The <code>HashMap</code> with
	 *         the tests results. Ex: "Length":false
	 **/
	public static HashMap<String, Boolean> isStrongEnough(String pw)
	{
		HashMap<String, Boolean> res = new HashMap<String, Boolean>();
		res.put(Constants.PW_LENGTH, estSuffisammentLong(pw));
		res.put(Constants.PW_SPE_CHAR, contientCaracSpe(pw));
		res.put(Constants.PW_NUMBER, contientNombre(pw));
		res.put(Constants.PW_MIN, contientMin(pw));
		res.put(Constants.PW_MAJ, contientMaj(pw));
		res.put(Constants.PW_GOOD_FMT, seulementAcceptes(pw));
		return res;
	}
}
