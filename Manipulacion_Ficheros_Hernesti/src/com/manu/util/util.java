package com.manu.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class util {
	/**
	 * Este método se emplea para rellenar de blancos un vector
	 * @param vector
	 * @param pos_actual
	 * @param pos_limite
	 */
	public static void ponBytesBlancos(byte [] vector, int pos_actual,int pos_limite){
		for(int i = pos_actual; i < pos_limite; i++){
			vector[i] = (byte) (32 & 0xff);
		}
	}

	
	public static void ajustarFichero(String fich_entrada, String fich_salida, int tamanio_ajuste, String fich_properties){
		
		MiProperties arch_prop = new MiProperties(fich_properties);
		
		//SE COMPRUEBA SI SE HA INSTANCIADO CORRECTAMENTE EL ARCHIVO PROPERTIES
		if(!arch_prop.hayError()){
			try {
				MiFichero fichero_entrada = new MiFichero(fich_entrada, MiFichero.LEC_BINARIO);
				MiFichero fichero_salida = new MiFichero(fich_salida, MiFichero.ESC_BINARIO);
				byte [] bytes = new byte[1];
				byte [] buffer = new byte[tamanio_ajuste + 2];
				String caracter_conv;
				int contador = 0;
				int cuenta_blancos = 0;
				while(fichero_entrada.dameBytes(bytes) >= 0){

					//MEDIANTE & 0XFF CONVERTIMOS A DECIMAL
					caracter_conv = arch_prop.dameValor(String.valueOf(bytes[0] & 0xff));
					//SE COMPRUEBA SI EL BYTE EXISTE EN EL PROPERTIES
					if(caracter_conv!=null){
						//SE COMPRUEBA SI SE HA LLENADO EL BUFFER ANTES DE INTENTAR ESCRIBIR EL NUEVO CARACTER
						if(contador==tamanio_ajuste){
							//se ha llenado el buffer. Hay que comprobar si se puede grabar en el archivo
							//SE COMPRUEBA QUE NO SEA UNA LINEA EN BLANCO
							if(cuenta_blancos == contador){
								//es una linea toda de blancos
								//en este caso no se graba nada en el archivo, solamente se resetea los contadores
								contador = 0;
								cuenta_blancos = 0;
							}else{
								buffer[tamanio_ajuste] = (byte) (13 & 0xff);
								buffer[tamanio_ajuste + 1] = (byte) (10 & 0xff);
								fichero_salida.escribir_fichero_b(buffer);
								contador = 0;
								cuenta_blancos = 0;
								//System.out.println(new String(buffer, "UTF-8"));
							}
						}

						//SE ANALIZA EL BYTE LEIDO
						if((caracter_conv.equals("13"))||(caracter_conv.equals("10"))){
							//System.out.println("Salto de linea->" + caracter_conv);
							if(cuenta_blancos != contador){
								util.ponBytesBlancos(buffer, contador, tamanio_ajuste);
								buffer[tamanio_ajuste] = (byte) (13 & 0xff);
								buffer[tamanio_ajuste + 1] = (byte) (10 & 0xff);
								fichero_salida.escribir_fichero_b(buffer);
							}
							contador = 0;
							cuenta_blancos = 0;
						
							//BUCLE PARA DESECHAR LOS SIGUIENTES 0D Y 0A. ESTO SE HACE HASTA QUE FINALICE EL ARCHIVO O SE ENCUENTRE UN ARCHIVO VALIDO
							while((fichero_entrada.dameBytes(bytes)>=0)&&((arch_prop.dameValor(String.valueOf(bytes[0] & 0xff))==null)||(arch_prop.dameValor(String.valueOf(bytes[0] & 0xff)).equals("10"))||(arch_prop.dameValor(String.valueOf(bytes[0] & 0xff)).equals("13")))){
								//No es necesario hacer nada
							}
							
							caracter_conv = arch_prop.dameValor(String.valueOf(bytes[0] & 0xff));
							//SE COMPRUEBA SI EL BYTE LEIDO ES DISTINTO DE 0D Y 0A PARA INFERTARLOS EN EL BUFFER -> ESTO SIGNIFICA QUE NO SE HA ALCANZADO EL FINAL DEL FICHERO
							if((!String.valueOf(bytes[0] & 0xff).equals("10"))&&(!String.valueOf(bytes[0] & 0xff).equals("13"))){
								buffer[contador] = (byte) (Integer.parseInt(caracter_conv) & 0xff);
								contador = contador + 1;
								if(String.valueOf(bytes[0] & 0xff).equals("32")){
									cuenta_blancos = cuenta_blancos + 1;
								}

							}else{
								//System.out.println("No entra");
							}
						}else{ 
							//SI NO ES NINGUNO DE LOS CARACTERES ANTERIORES SE METE EN EL BUFFER
							buffer[contador] = (byte) (Integer.parseInt(caracter_conv) & 0xff);
							contador = contador + 1;
							if(String.valueOf(bytes[0] & 0xff).equals("32")){//si es un espacio en blanco se incrementa su contador
								cuenta_blancos = cuenta_blancos + 1;
							}
						}
					}
				}
				
				//SE HA ALCANZADO EL FINAL DE FICHERO Y HAY BYTES EN EL BUFFER QUE TIENEN QUE SER COPIADOS AL ARCHIVO
				if((contador > 0)&&(cuenta_blancos != contador)){
					buffer[tamanio_ajuste] = (byte) (13 & 0xff);
					buffer[tamanio_ajuste + 1] = (byte) (10 & 0xff);
					util.ponBytesBlancos(buffer, contador, tamanio_ajuste);
					fichero_salida.escribir_fichero_b(buffer);
				}				
				
				fichero_entrada.cerrarFichero();
				fichero_salida.cerrarFichero();
			} catch (FileNotFoundException e) {
				System.out.print("Error al abrir un fichero->" + e.getMessage());
				//e.printStackTrace();
			} catch (IOException e) {
				System.out.print("Error al abrir un fichero->" + e.getMessage());
				//e.printStackTrace();
			}
			
		}else{
			System.out.println(arch_prop.dameMensaError());
		}		
	}
	

	/**
	 * Quita todos los saltos de linea y retorno de carro para que esté en una sola linea
	 * @param ficheroXML
	 */
	public static void aplanarXML(String ficheroXML){
		try {
			File tempFile = File.createTempFile("mificherotemporal",null);
			tempFile.deleteOnExit();
			
			BufferedOutputStream bufferedOutputTemp = new BufferedOutputStream(new FileOutputStream (tempFile));
			
			MiFichero fichero_entrada = new MiFichero(ficheroXML, MiFichero.LEC_BINARIO);
			byte [] bytes = new byte[1];
			String caracter_conv;
			while(fichero_entrada.dameBytes(bytes) >= 0){
				caracter_conv = String.valueOf(bytes[0] & 0xff);
				if((caracter_conv!=null)&&(!caracter_conv.equals("10"))&&(!caracter_conv.equals("13"))){
					bufferedOutputTemp.write(bytes);
				}
			}
			
			bufferedOutputTemp.close();
			fichero_entrada.cerrarFichero();
			
			BufferedInputStream bufferInput = new BufferedInputStream(new FileInputStream(tempFile));
			MiFichero fichero_salida = new MiFichero(ficheroXML, MiFichero.ESC_BINARIO);
			
			while(bufferInput.read(bytes) >= 0){
				fichero_salida.escribir_fichero_b(bytes);
			}
			
			bufferInput.close();
			fichero_entrada.cerrarFichero();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Este método 
	 * @param ficheroXML
	 */
	public static void enColumnarXML(String ficheroXML){
		try {
			File tempFile = File.createTempFile("mificherotemporal",null);
			tempFile.deleteOnExit();
			
			BufferedOutputStream bufferedOutputTemp = new BufferedOutputStream(new FileOutputStream (tempFile));
			
			MiFichero fichero_entrada = new MiFichero(ficheroXML, MiFichero.LEC_BINARIO);
			byte [] bytes = new byte[1];
			byte [] byteSig = new byte[1];
			byte [] salto = new byte[2];
			salto[0] = (byte) (13 & 0xff);
			salto[1] = (byte) (10 & 0xff);
			
			Boolean flg_tagApert = false;
			Boolean prim_linea = true;
			String caracter_conv;
			while(fichero_entrada.dameBytes(bytes) >= 0){
				caracter_conv = String.valueOf(bytes[0] & 0xff);
				if((caracter_conv!=null)&&(!caracter_conv.equals("10"))&&(!caracter_conv.equals("13"))){
					//APERTURA DE ETIQUETA
					if(caracter_conv.equals("60")){
						//SE LEE EL SIGUIENTE BYTE
						fichero_entrada.dameBytes(byteSig);
						
						if(!String.valueOf(byteSig[0] & 0xff).equals("47")){
							//SI EL SEGUNDO BYTE NO ES UN SLASH SE HACE EL SALTO DE LINEA
							if(!prim_linea){
							bufferedOutputTemp.write(salto);
							}else{
								prim_linea = false;
							}
							flg_tagApert = true;//SE INDICA QUE SE HA PUESTO UNA ETIQUETA DE APERTURA
						}else{
							if(flg_tagApert){
								flg_tagApert = false;//LA SIGUIENTE ETIQUETA DE APERTURA TENDRÁ SALTO
							}else{
								bufferedOutputTemp.write(salto);
							}
						}
					}
					bufferedOutputTemp.write(bytes);
					if((String.valueOf(byteSig[0] & 0xff)!=null)&&(caracter_conv.equals("60"))){
						bufferedOutputTemp.write(byteSig);
					}
				}
			}
			
			bufferedOutputTemp.close();
			fichero_entrada.cerrarFichero();
			
			BufferedInputStream bufferInput = new BufferedInputStream(new FileInputStream(tempFile));
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(ficheroXML));
			
			while(bufferInput.read(bytes) >= 0){
				bufferedOutput.write(bytes);
			}
			
			bufferInput.close();
			bufferedOutput.close();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void repararEtiquetas(String ficheroXML){
		StringTokenizer tokens = null; 
		String cadenaToken;		
		String etiqueta;		
		Integer posTag = null;	
		byte [] bytes = new byte[1];

		try {
			File tempFile = File.createTempFile("mificherotemporal",null);
			//tempFile.deleteOnExit();
			
			BufferedWriter ficheroTemp = new BufferedWriter(new FileWriter (tempFile));
			
			MiFichero fichero_entrada = new MiFichero(ficheroXML, MiFichero.LEC_TEXTO);
			String linea = null;
			String linea_resultado = null;
			Integer contador = 0;
			
			while((linea = fichero_entrada.dameLineaTxt())!=null){
				linea_resultado = "";
				tokens = new StringTokenizer(linea, ">",true);
				while(tokens.hasMoreTokens()){
					cadenaToken = tokens.nextToken().trim()+">";
					//BUSCAR LA POSICION DONDE EMPIEZA LA POSIBLE ETIQUETA.
					//PUEDE HABER SOLO UNA ETIQUETA O UNA ETIQUETA Y TEXTO
					posTag = cadenaToken.indexOf("<");
					
					if(posTag!=-1){
					etiqueta = cadenaToken.substring(posTag);//CONTIENE UNA ETIQUETA
						if(posTag != 0){//PRIEMRO VIENE EL TEXTO DE LA ETIQUETA
							//DESPUES DE LA ETIQUETA HAY TEXTO
							//NO ES UNA ETIQUETA Y LA DEVOLVEMOS QUITANDO EL EXCESO DE ESPACIOS EN BLANCO
							linea_resultado = linea_resultado + cadenaToken.substring(0, posTag).replaceAll(" +", " ").trim();
						}
						linea_resultado = linea_resultado + verificaTag(etiqueta);
					}
				}
				//HAY QUE ASEGURARSE DE QUE NO SE INTENTA ESCRIBIR UN NULL EN EL FICHERO

				if(linea_resultado!=null){
					contador = contador + 1;
					ficheroTemp.write(linea_resultado+"\n");
				}
				
			}
			ficheroTemp.close();
			fichero_entrada.cerrarFichero();

			BufferedInputStream bufferInput = new BufferedInputStream(new FileInputStream(tempFile));

			BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(ficheroXML));
			
			while(bufferInput.read(bytes) >= 0){
				bufferedOutput.write(bytes);
			}
			
			bufferInput.close();
			bufferedOutput.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
	}
	
	public static String verificaTag(String tag){
		String resultado = "";
		if(tag.substring(0, 1).equals("<")){
			if(tag.indexOf("=")==-1){
				//LA ETIQUETA NO TIENE ATRIBUTOS, SOLO HAY QUE ELIMINAR TODOS LOS ESPACIOS
				resultado = tag.replaceAll(" ","");
			}else if((tag.indexOf("?")==-1)&&(!tag.substring(1,9).equals("Document"))){
				//LA ETIQUETA TIENE AL MENOS UN ATRIBUTO
				
				resultado = "<InstdAmt Ccy="+tag.substring(tag.indexOf("\""), tag.lastIndexOf("\"")) +"\">";
			}else{
				resultado = tag;
			}
			
			
			
			
		}else{
			//NO ES UNA ETIQUETA Y LA DEVOLVEMOS QUITANDO EL EXCESO DE ESPACIOS EN BLANCO
			resultado = tag.replaceAll(" +", " ");//SI HAY MÁS DE UN ESPACIO SEGUIDO SE ELIMINA
		}
		
		
		return resultado.trim();//LE QUITAMOS LOS ESPACIOS DE LOS EXTREMOS
	}
	
	public static void copiarFicheTxt(String nombre_entrada, String nombre_salida) throws IOException{
		byte [] bytes = new byte[1];
		BufferedInputStream bufferInput = new BufferedInputStream(new FileInputStream(nombre_entrada));
		MiFichero fichero_salida = new MiFichero(nombre_salida, MiFichero.ESC_BINARIO);
		
		while(bufferInput.read(bytes) >= 0){
			fichero_salida.escribir_fichero_b(bytes);
		}
		
		bufferInput.close();
		fichero_salida.cerrarFichero();		
	}
}
