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

public interface HttpURLConnection {

	String sendGET(String url) throws IOException;
	<C> C sendGET(String url,
				   Map<String,String> header,
				   Map<String, String> params,
				   Class<C> typeToConvert) throws IOException, CommoditieBaseException;
	<T,C> C sendPOST(String url, T body, Class<C> typeToConvert) throws IOException, CommoditieBaseException;
	<T,C> C sendPOST(String url, Map<String,String> header, T body, Class<C> typeToConvert) throws IOException, CommoditieBaseException ;
	<C> C sendPOST(String url,
							Map<String,String> header,
							Map<String, String> params,
							Class<C> typeToConvert) throws IOException, CommoditieBaseException;
}