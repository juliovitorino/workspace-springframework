package br.com.jcv.commons.library.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
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
			return "Something went wrong with request";
		}

	}

	public <T,C> C sendPOST(String url, T body, Class<C> typeToConvert) throws IOException, CommoditieBaseException {
		URL obj = new URL(url);
		log.info("POST URL :: {}", url);

		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/json");

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(new ObjectMapper().writeValueAsString(body).getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		log.info("POST Response Code :: {}", responseCode);

		if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			C returnTo;
			try {
				returnTo = new ObjectMapper().readValue(response.toString(), typeToConvert);
			} catch (JsonProcessingException e) {
				returnTo = (C) response.toString();
			}
			return returnTo;
		} else {
			throw new CommoditieBaseException("Something went wrong with request", HttpStatus.BAD_GATEWAY);
		}

	}

	public <T,C> C sendPOST(String url, Map<String,String> header, T body, Class<C> typeToConvert) throws IOException, CommoditieBaseException {
		URL obj = new URL(url);
		log.info("POST URL :: {}", url);

		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/json");

		header.forEach(con::setRequestProperty);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(new ObjectMapper().writeValueAsString(body).getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		log.info("POST Response Code :: {}", responseCode);

		if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			C returnTo;
			try {
				returnTo = new ObjectMapper().readValue(response.toString(), typeToConvert);
			} catch (JsonProcessingException e) {
				returnTo = (C) response.toString();
			}
			return returnTo;
		} else {
			throw new CommoditieBaseException("Something went wrong with request", HttpStatus.BAD_GATEWAY);
		}

	}

}