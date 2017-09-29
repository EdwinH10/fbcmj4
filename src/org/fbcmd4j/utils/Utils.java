package org.fbcmd4j.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.Post;
import facebook4j.User;
import facebook4j.internal.org.json.JSONObject;

public class Utils {

	private static final Logger logger = LogManager.getLogger(Utils.class);
	
	static String appId ="135891643706241";
	static String appSecret = "ac82008cfae0ad7790483a3665f6a56a";
	static String userToken = "EAACEdEose0cBAHb5NodxdH8RxjyrH5NbPowshckuXfiZBHSWTqO4erljeILJgCQH9f0UUxyx2JAg4dF3SOh7R4SylIn5hk1t5k3vaDj4fr1MDYwZC0dmVAZBAZAPzHbUhGfVgOT5qfkZAPR6YFBhd2AEGK26UhpyArV4AYcXV13kRlRHry0JDX442zFAmtncYDmtVzyuHXAZDZD";
	static String appToken ="135891643706241|jnaRdyr98M8vR6G4eNM4EwCqmgc";
	static Facebook mfb;
	
	

	public Utils() {
		mfb = new FacebookFactory().getInstance();
		mfb.setOAuthAppId(appId, appSecret);
		mfb.setOAuthAccessToken(new AccessToken(userToken));
	}
	
	public static void printPost(Post p) {
		if(p.getStory() != null)
			System.out.println("Story: " + p.getStory());
		if(p.getMessage() != null)
			System.out.println("Mensaje: " + p.getMessage());
		System.out.println("--------------------------------");
	}
	public static String savePostsToFile(String fileName, List<Post> posts) {
		File file = new File(fileName + ".txt");

		try {
    		if(!file.exists()) {
    			file.createNewFile();
            }

    		FileOutputStream fos = new FileOutputStream(file);
			for (Post p : posts) {
				String msg = "";
				if(p.getStory() != null)
					msg += "Story: " + p.getStory() + "\n";
				if(p.getMessage() != null)
					msg += "Mensaje: " + p.getMessage() + "\n";
				msg += "--------------------------------\n";
				fos.write(msg.getBytes());
			}
			fos.close();

			logger.info("Posts guardados en el archivo '" + file.getName() + "'.");
			System.out.println("Posts guardados exitosamente en '" + file.getName() + "'.");
		} catch (IOException e) {
			logger.error(e);
		}
        
        return file.getName();
	}	
	}
	