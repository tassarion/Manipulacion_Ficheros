package com.manu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;





/**
 * Esta clase sirve para cargar, crear y manipular un documento XML.
 * @author Manuel Arroyo Salcedo
 *
 */
public class XMLDom4j {
	private Document documento;
	private String mensajeError = null;
	private Boolean hayMensajeError = false;
	
	private ArrayList<String> lista_etiquetas;
	private ArrayList<Integer> ocurrencias_etiquetas;
	
	public XMLDom4j(){
		//this.documento = DocumentFactory.getInstance().createDocument();
	}
	
	/**
	 * Este método se emplea para leer y almacenar un fichero XML
	 * @param nombre_fichero
	 */
	public void cargarFicheroXML(String nombre_fichero){
		SAXReader reader = new SAXReader();
		try {
			this.documento = reader.read(nombre_fichero);
//			Element rootElement = this.documento.getRootElement();
//			System.out.println("Root Element: "+rootElement.getName());		
//			
//			  for (Iterator<Element> i = rootElement.elementIterator(); i.hasNext();) {
//				    Element e = i.next();
//				    System.out.println("Root Element: "+e.getName());
//			  }
			
		} catch (DocumentException e) {
			System.out.println("Error de lectura de documento:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Este método se emplea para cargar y recorrer en profundidad un fichero XML. 
	 * @param nombre_fichero
	 */
	public void recorrerXMLSaxProf(String nombre_fichero){
		SAXReader reader = new SAXReader();
		lista_etiquetas = new ArrayList<String>();
		ocurrencias_etiquetas = new ArrayList<Integer>();
		try {
			this.documento = reader.read(nombre_fichero);
			Element rootElement = this.documento.getRootElement();
			System.out.println("Root Element: "+rootElement.getName());
			
			recorrerXMLSax(rootElement);
			
			System.out.println("Total etiquetas diferentes encontradas: " + lista_etiquetas.size());
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Recorrido en profundidad de un arbol XML
	 */
	public void recorrerXMLSaxProf(Document miDocumento){
		lista_etiquetas = new ArrayList<String>();
		ocurrencias_etiquetas = new ArrayList<Integer>();

		Element rootElement = miDocumento.getRootElement();
		
		System.out.println("Root Element: "+rootElement.getName());
		
		recorrerXMLSax(rootElement);
		
		System.out.println("Total etiquetas diferentes encontradas: " + lista_etiquetas.size());
		
	}	
	
	
	/**
	 * Recorrido en profundidad de un arbol XML
	 */
	public void recorrerXMLSaxProf(){
		lista_etiquetas = new ArrayList<String>();
		ocurrencias_etiquetas = new ArrayList<Integer>();

		Element rootElement = this.documento.getRootElement();
		
		System.out.println("Root Element: "+rootElement.getName());
		
		recorrerXMLSax(rootElement);
		
		System.out.println("Total etiquetas diferentes encontradas: " + lista_etiquetas.size());
		
	}
	
	/**
	 * Método privado recursivo empleado por el recorrido en profundidad
	 * @param elemento
	 */
	private void recorrerXMLSax(Element elemento){
		//SAXReader reader = new SAXReader();
		int indice;
		  for (Iterator<Element> i = elemento.elementIterator(); i.hasNext();) {
			    Element elemHijo = i.next();
			    //System.out.println("Root Element: "+elemHijo.getName());
			    if(this.lista_etiquetas.contains(elemHijo.getName())){
			    	indice = this.lista_etiquetas.indexOf(elemHijo.getName());
			    	this.ocurrencias_etiquetas.add(indice, this.ocurrencias_etiquetas.get(indice)+1);
			    }else{
			    	this.lista_etiquetas.add(elemHijo.getName());
			    	this.ocurrencias_etiquetas.add(1);
			    }
			    recorrerXMLSax(elemHijo);
		  }		
	}
	
	/**
	 * Muestra las etiqutas que existen en el XML y el número de veces que aparecen almacenadas en las listas correspondientes.
	 * Antes de ejecutar este método es necesario haber recorrido en profundidad el XML.
	 */
	public void mostrarEtiquetas(){
		for(int i = 0; i<this.lista_etiquetas.size(); i++){
			System.out.println("Etiqueta:" + this.lista_etiquetas.get(i) + " - Ocurrencias: " + this.ocurrencias_etiquetas.get(i));
		}
	}
	
	/**
	 * Este método crea un documento XML y su nodo raiz.
	 * @param nodoRaiz
	 */
	public void crearXML(String nodoRaiz){
		this.documento = DocumentHelper.createDocument();
		this.documento.addElement(nodoRaiz);
	}
	
	/**
	 * Crea un nodo etiqueta. Este nodo está vacio, no contiene ni atributos ni texto
	 * @param nombreEtiqueta
	 * @return
	 */
	public Element crearEtiqueta(String nombreEtiqueta){
		Element etiqueta = null;
		etiqueta = this.documento.addElement(nombreEtiqueta);
		return etiqueta;
	} 
	
	/**
	 * Añade un atributo a un elemento nodo
	 * @param etiqueta
	 * @param nomAtributo
	 * @param valor
	 * @return
	 */
	public Boolean creaAtributo(Element nodo, String nomAtributo, String valor){
		Boolean resultado = true;
		if(nodo==null){
			resultado = false;
			this.hayMensajeError = true;
			this.mensajeError = "Error al crear un atributo. El elemento está a null.";
		}if(nodo.attribute(nomAtributo)!=null){
			resultado = false;
			this.hayMensajeError = true;
			this.mensajeError = "Error al crear un atributo. El atributo ya existe.";
		}else{
			nodo.addAttribute(nomAtributo, valor);
		}
		return resultado;
	}
	
	/**
	 * Añade texto a una etiqueta
	 * @param nodo
	 * @param texto
	 */
	public void crearTexto(Element nodo, String texto) {
		nodo.addText(texto);
	}

	/**
	 * Añade un nodo hijo a un nodo padre.
	 * @param nodoPadre
	 * @param nodoHijo
	 */
	public void añadirNodo(Element nodoPadre, Element nodoHijo){
		nodoPadre.add(nodoHijo);
	}
	
	/**
	 * Devuelve los nodos (elementos) con un nombre de etiqueta dado
	 * @param nombreEtiqueta nodo (elementos) a buscar
	 * @return lista de nodos (elementos)
	 */
	public List<Element> buscaEtiqueta(String nombreEtiqueta){
		HashMap map = new HashMap();
		map.put( "pre", "urn:iso:std:iso:20022:tech:xsd:pain.001.001.03");		
		XPath xpath = new DefaultXPath( "//pre:"+nombreEtiqueta);
		xpath.setNamespaceContext( new SimpleNamespaceContext(map));	
		return xpath.selectNodes(this.documento);      
	}

	//Accesores	
	
	public Document getDocumento() {
		return documento;
	}

	public void setDocumento(Document documento) {
		this.documento = documento;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public Boolean getHayMensajeError() {
		return hayMensajeError;
	}

	public void setHayMensajeError(Boolean hayMensajeError) {
		this.hayMensajeError = hayMensajeError;
	}
}
