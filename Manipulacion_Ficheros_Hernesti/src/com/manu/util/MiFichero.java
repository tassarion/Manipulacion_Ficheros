package com.manu.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MiFichero {
	static final int LEC_BINARIO = 0;
	static final int ESC_BINARIO = 1;
	static final int LEC_TEXTO = 2;
	static final int ESC_TEXTO = 3;
	
	private String ruta_nombre;
	private BufferedInputStream bufferedInput;
	private BufferedOutputStream bufferedOutput;
	private BufferedReader bufferReader;
	private BufferedWriter bufferWriter;
	private Boolean finFichero;
	
	private Integer lineaActual = 0;
	
	private int modo;
	
	public MiFichero(String ruta_nombre, int modo) throws IOException{
		this.modo = modo;
		this.ruta_nombre = ruta_nombre;
		this.finFichero = false;
		if(modo == 0){
			FileInputStream fileImput = new FileInputStream(this.ruta_nombre);
			this.bufferedInput = new BufferedInputStream(fileImput);
		}else if(modo == 1){
	        FileOutputStream fileOutput = new FileOutputStream (this.ruta_nombre);
	        this.bufferedOutput = new BufferedOutputStream(fileOutput);				
		}else if(modo == 2){
			FileReader fileReader = new FileReader (new File (this.ruta_nombre));
			this.bufferReader = new BufferedReader(fileReader);			
		}else if(modo == 3){
			FileWriter fileWriter = new FileWriter (new File (this.ruta_nombre));
			this.bufferWriter = new BufferedWriter (fileWriter);
		}else{
			//error
		}
	}

	/**
	 * Este método lee de una sola vez todo el fichero binario Byte a Byte
	 * @throws IOException
	 */
	public void leer_fichero_b() throws IOException{
		int leidos;
		int contador = 0;
		byte [] bytes = new byte[1];
		leidos = this.bufferedInput.read(bytes);
		contador = contador + 1;
		while (leidos > 0){
			leidos = this.bufferedInput.read(bytes);
			contador = contador + 1;		
		}
	}
	
	/**
	 * Este método devuelve un array de bytes del fichero que se está leyendo.
	 * @param bytes
	 * @return Número de bytes leidos
	 * @throws IOException
	 */
	public int dameBytes (byte [] miByte) throws IOException{
		int nLeidos = this.bufferedInput.read(miByte);
		if(nLeidos == -1) this.finFichero = true;
		return nLeidos;
	}
	
	/**
	 * Este método se emplea para comprobar si se ha alcanzado el final del fichero
	 * @return
	 */
	public Boolean esFinalArchivo(){
		return this.finFichero;
	}

	/**
	 * Este método se emplea para cerrar el archivo
	 */
	public void cerrarFichero(){
		try {
			if(this.modo==0){
				this.bufferedInput.close();
			}else if(this.modo==1){
				this.bufferedOutput.close();
			}else if(this.modo==2){
				this.bufferReader.close();
			}else if (this.modo==3){
				this.bufferWriter.close();
			}
		} catch (IOException e) {
			System.out.println("Error al cerrar el fichero->" + e.getMessage());
		}
	}
	
	/**
	 * Este método se emplea para escribir un fichero binario
	 * @param miByte
	 * @return
	 * @throws IOException
	 */
	public void escribir_fichero_b(byte [] miByte)   {
		try{
			this.bufferedOutput.write(miByte, 0, miByte.length );
		}catch(IOException e){
			System.out.println("Error escritura->" + e.getMessage());
		}
	}
	
	/**
	 * Este método devuelve una línea de texto de un archivo.
	 * En caso de que no haya más líneas devolverá null.
	 * @return
	 * @throws IOException
	 */
	public String dameLineaTxt() {
		String linea = null;
		try {
			linea = this.bufferReader.readLine();
			lineaActual = lineaActual + 1; 
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Error al leer una línea de texto:"+ e.getMessage());
		}
		return linea;
	}
	
	public void escribeLineaTxt(String linea){
		try {
			this.bufferWriter.write(linea);

			//this.bufferWriter.newLine();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Erro al escribir una linea de texto:" + e.getMessage());
		} 
	
	}
		
}
