package com.manu.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MiProperties {
	
	private Properties archivoProp;
	private Boolean error;
	private String mensa_error; 
	
	/**
	 * Constructor de la clase. instancia el objeto y lo abre. En caso de que se produzca un error en la apertura 
	 * quedará reflejado en el atributo error y se guardará el mensaje en el atributo mensa_error.
	 * @param ruta_nombre
	 * @param error
	 */
	public MiProperties(String ruta_nombre){
		this.error = false;
		this.archivoProp = new Properties();
		try {
			this.archivoProp.load(new FileInputStream(ruta_nombre));
		} catch (FileNotFoundException e) {
			this.error = true;
			mensa_error = "Error archivo Properties->" + e.getMessage();
			//e.printStackTrace();
		} catch (IOException e) {
			this.error = true;
			mensa_error = "Error archivo Properties->" + e.getMessage();
			//e.printStackTrace();
		}
	}
	
	/**
	 * Devuelve el valor de la clave buscada
	 * @param clave
	 * @return
	 */
	public String dameValor(String clave){
		return this.archivoProp.getProperty(clave);
	}
	
	/**
	 * Indica si hay o no error
	 * @return
	 */
	public Boolean hayError(){
		return this.error;
	}
	
	/**
	 * Devuelve el mensaje de error en caso de que se haya producido.
	 * @return
	 */
	public String dameMensaError(){
		return this.mensa_error;
	}
}
