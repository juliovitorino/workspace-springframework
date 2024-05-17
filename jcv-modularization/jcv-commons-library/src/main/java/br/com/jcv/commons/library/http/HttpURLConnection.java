package br.com.jcv.commons.library.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpURLConnection {

	public String sendGET(String url) throws IOException {
		URL obj = new URL(url);
		log.info("GET URL :: {}", url);

		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		log.info("GET Response Code :: {}", responseCode);

		if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return response.toString();
		} else {
			return "Algo deu errado na request direta";
		}

	}

}