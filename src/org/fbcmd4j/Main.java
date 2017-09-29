package org.fbcmd4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fbcmd4j.utils.Utils;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.auth.AccessToken;

public class Main {
	static final Logger logger = LogManager.getLogger(Main.class);
	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";

	public static void main(String[] args) throws FacebookException, IOException {
		logger.info("Iniciando app");
		
		String appId ="135891643706241";
		String appSecret = "ac82008cfae0ad7790483a3665f6a56a";
		String userToken = null;
		String appToken ="135891643706241|jnaRdyr98M8vR6G4eNM4EwCqmgc";
		String redU= "https://www.facebook.com/connect/login_success.html.";
		Facebook mfb = null;
		
		
		
		int option = 1;
		
			Scanner scan = new Scanner(System.in);
			while(true) {
			
				System.out.println("Cliente de Facebook en lÃ­nea de comando \n\n"
								+  "Opciones: \n"
								+  "(0) Configurar usuario \n"
								+  "(1) Mostrar Usuario \n"
								+  "(2) Mostrar Newsfeed \n"
								+  "(3) Mostrar Wall \n"
								+  "(4) Publicar Estado \n"
								+  "(5) Publicar Link \n"
								+  "(6) Salir \n"
								+  "\nPor favor ingrese una opciÃ³n:");
		
					option = scan.nextInt();
					scan.nextLine();
					switch (option) {
					case 0:
						mfb = new FacebookFactory().getInstance();
						mfb.setOAuthAppId(appId, appSecret);
						
						
						String urlVal ="https://www.facebook.com/v2.10/dialog/oauth?client_id="+appId+"&redirect_uri="+redU;
						System.out.println("Ingresa al link y loggeate con tus credenciales de Facebook: \n"+urlVal);
						System.out.println(" ");
						System.out.println("Despues:");
						System.out.println("Copia y pega el link de la página a la que te redirigió después de ingresar a Facebook");
						System.out.println("No importa que la pagina diga que no esta disponible");
						String link0 = scan.nextLine();
						String code = link0.substring(58);
						
						
						URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id="
						        + appId + "&redirect_uri=" + redU
						        + "&client_secret=" + appSecret
						        + "&code=" + code);
						
						
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						String line, outputString = "";
						BufferedReader reader = new BufferedReader(
						        new InputStreamReader(conn.getInputStream()));
						while ((line = reader.readLine()) != null) {
						    outputString += line;
						}
						System.out.println(outputString);
						if(outputString.indexOf("access_token")!=-1) {
						    int k=outputString.length();
						    
						    String userPrev = outputString.substring(17);
						    userToken =	userPrev.substring(0,userPrev.indexOf("\""));
						}
						
						
						System.out.println(userToken +"\n");
						mfb.setOAuthAccessToken(new AccessToken(userToken));
						System.out.println("Usuario configurado");
						break;
					case 1:
						try {
							System.out.println(mfb.getMe().getName());
						} catch (FacebookException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case 2:
						System.out.println("Mostrando NewsFeed...");
						ResponseList<Post> newsFeed = mfb.getFeed();
						for (Post p : newsFeed) {
							Utils.printPost(p);
						}
						askToSaveFile("NewsFeed", newsFeed, scan);
						break;
					case 3:
						System.out.println("Mostrando Wall...");
						ResponseList<Post> wall = mfb.getPosts();
						for (Post p : wall) {
							Utils.printPost(p);
						}		
						askToSaveFile("Wall", wall, scan);
						break;
					case 4:
						System.out.println("Escribe tu estado: ");
						String estado = scan.nextLine();
						try {
							mfb.postStatusMessage(estado);
						} catch (FacebookException e) {
							logger.error(e);
						}
						System.out.println("\nEstado correctamente publicado");
						break;
					case 5:
						
					case 6:
						System.out.println("Gracias por usar el cliente!");
						System.exit(0);
						break;
					default:
						break;
					}
			}
		}
				
	
	public static void askToSaveFile(String fileName, ResponseList<Post> posts, Scanner scan) {
		System.out.println("Guardar resultados en un archivo de texto? Si/No");
		String option = scan.nextLine();
		
		if (option.contains("Si") || option.contains("si")) {
			List<Post> ps = new ArrayList<>();
			int n = 0;

			while(n <= 0) {
				try {
					System.out.println("CuÃ¡ntos posts deseas guardar?");
					n = Integer.parseInt(scan.nextLine());					
			
					if(n <= 0) {
						System.out.println("Favor de ingresar un nÃºmero vÃ¡lido");
					} else {
						for(int i = 0; i<n; i++) {
							if(i>posts.size()-1) break;
							ps.add(posts.get(i));
						}
					}
				} catch(NumberFormatException e) {
					logger.error(e);
				}
			}

			
		}
	}
}
