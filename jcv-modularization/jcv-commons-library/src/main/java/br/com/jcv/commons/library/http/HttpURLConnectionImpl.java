package br.com.jcv.commons.library.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpURLConnectionImpl implements HttpURLConnection{

	@Override
	public String sendGET(String url) throws IOException {
		URL obj = new URL(url);
		log.info("GET URL :: {}", url);

		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setReadTimeout(10000);
		con.setConnectTimeout(15000);
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

	@Override
	public <T,C> C sendPOST(String url, T body, Class<C> typeToConvert) throws IOException, CommoditieBaseException {
		URL obj = new URL(url);
		log.info("POST URL :: {}", url);

		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setReadTimeout(10000);
		con.setConnectTimeout(15000);
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

	@Override
	public <T,C> C sendPOST(String url, Map<String,String> header, T body, Class<C> typeToConvert) throws IOException, CommoditieBaseException {
		URL obj = new URL(url);
		log.info("POST URL :: {}", url);

		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setReadTimeout(10000);
		con.setConnectTimeout(15000);
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/json");

		if(Objects.nonNull(header)) header.forEach(con::setRequestProperty);

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
			throw new CommoditieBaseException("Something went wrong with request", HttpStatus.valueOf(responseCode));
		}

	}

	@Override
	public <C> C sendPOST(String url,
							Map<String,String> header,
							Map<String, String> params,
							Class<C> typeToConvert) throws IOException, CommoditieBaseException {

		List<NameValuePair> collectParams = null;

		URL obj = new URL(url);
		log.info("GET URL :: {}", url);

		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setReadTimeout(10000);
		con.setConnectTimeout(15000);
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		if(Objects.nonNull(header)) header.forEach(con::setRequestProperty);
		if(Objects.nonNull(params)) {
			collectParams = params.entrySet()
					.stream()
					.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
					.collect(Collectors.toList());
		}


		// For POST only - START
		con.setDoOutput(true);
		con.setDoInput(true);
		OutputStream os = con.getOutputStream();
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(os, "UTF-8"));
		writer.write(getQueryParams(collectParams));
		writer.close();
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
			throw new CommoditieBaseException("Something went wrong with request", HttpStatus.valueOf(responseCode));
		}

	}
	private String getQueryParams(List<NameValuePair> params) throws UnsupportedEncodingException
	{
		if(Objects.isNull(params)) return "";

		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params)
		{
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}