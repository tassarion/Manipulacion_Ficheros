package com.manu.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Esta clase sirve para crear de forma est�tica un XML y sus componentes, nodo raiz, nodos, texto de nodos y atributos de nodos.
 * @author Manuel Arroyo
 *
 */

public class XMLElementos {

	/**
	 * Crea un documento XML. Para poder a�adir un nodo antes debe existir el documento y el nodo raiz.
	 * @return
	 */
	public static Document creaDocumento(){
		return DocumentHelper.createDocument();
	}
	
	/**
	 * Crea el nodo raiz del documento. El nodo raiz debe existir y tiene que ser �nico. A partir de �l cuelga el resto de los nodos por lo que ha de crearse antes de poder a�adir los nodos.
	 * @param documento
	 * @param raiz
	 * @return
	 */
	public static Element creaRaiz(Document documento, String raiz){
		return documento.addElement(raiz);
	}	
	
	/**
	 * Crea un nodo
	 * @param nombreNodo
	 * @return
	 */
	public static Element crearNodo(String nombreNodo){
		Document documento = DocumentHelper.createDocument();
		return documento.addElement(nombreNodo);
	}
	
	/**
	 * Crea un atributo en un nodo. 
	 * @param nodo
	 * @param clave
	 * @param valor
	 */
	public static void creaAtributo(Element nodo, String clave, String valor){
		nodo.addAttribute(clave, valor);
	}
	
	/**
	 * Crea el texto en un nodo.
	 * @param nodo
	 * @param texto
	 */
	public static void creaTexto(Element nodo, String texto){
		nodo.addText(texto);
	}
	
	/**
	 * Enlaza dos nodos, uno ser� el padre y el otro, el que cuelga de �l ser� el hijo.
	 * @param nodoPadre
	 * @param nodoHijo
	 */
	public static void aniadeHijoAPadre(Element nodoPadre, Element nodoHijo){
		nodoPadre.add(nodoHijo);
	}
	
	
	

}
