package com.manu.controlador;

import com.manu.util.util;

public class Principal_Ajuste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		String ruta_ficheros = "C:\\Users\\Angel\\Workspaces\\MyEclipse Professional\\Manipulacion_Ficheros\\src\\com\\hernesti\\ficheros\\";
//		String ruta_properties = "C:\\Users\\Angel\\Workspaces\\MyEclipse Professional\\Manipulacion_Ficheros\\src\\com\\hernesti\\properties\\";
		String ruta_ficheros = "E:\\workspace_eclipse\\Manipulacion_Ficheros_Hernesti\\src\\com\\manu\\ficheros\\";
		String ruta_properties = "E:\\workspace_eclipse\\Manipulacion_Ficheros_Hernesti\\src\\com\\manu\\properties\\";
		
		Integer ajuste_longitud = 500;
		
		System.out.println("Inicio ajuste");
		util.ajustarFichero(ruta_ficheros + "CCSKT113.TRE_transformar.XML", ruta_ficheros + "CCSKT113.TRE_transformaDO.XML", ajuste_longitud , ruta_properties + "propertiesXML.properties");
		System.out.println("Fin ajuste");
		
//		System.out.println("Inicio aplanar");
//		util.aplanarXML(ruta_ficheros + "CCSKT113.TRE_transformaDO.XML");
//		System.out.println("FIN aplanar");

		System.out.println("Inicio encolumnar");
		util.enColumnarXML(ruta_ficheros + "CCSKT113.TRE_transformaDO.XML");
		System.out.println("FIN encolumnar");
		
		System.out.println("Inicio reparar etiquetas");
		util.repararEtiquetas(ruta_ficheros + "CCSKT113.TRE_transformaDO.XML");
		System.out.println("FIN reparar etiquetas");
//		

	}

}
