package br.com.jcv.commons.library.integration;

import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component(value = "httpUrlConnectionHelper")
public class HttpUrlConnectionHelper implements IRestConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Gson gson = new Gson();

    @Override
    public <T> T executeGet(String targetURL, Type type) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            //connection.setRequestProperty("x-api-key","6v3v1QxhkV79mpTEqKpPu3h8ixYUdhxi8WdKFNOr");

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {

                response.append(line);
            }
            rd.close();

            if (connection.getResponseCode() != HttpStatus.SC_OK &&
               connection.getResponseCode() != HttpStatus.SC_ACCEPTED &&
                connection.getResponseCode() != HttpStatus.SC_CREATED) {

                logger.error("error making request for {} | responde code = {}", targetURL, connection.getResponseCode());
                throw new IntegrationErrorException(targetURL);
            }
            return gson.fromJson(response.toString(), type);
        } catch (Exception e) {

            logger.error("Erro ao executar http get (url = " + targetURL + "): " + e.getMessage(), e);
            throw new IntegrationErrorException(targetURL);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }


    @Override
    public <T> T executeGet(String targetURL, Type type, String token, boolean isBearer) {
        return executeGet(targetURL, type, isBearer ? "Bearer " + token : token);
    }

    @Override
    public <T> T executeGet(String targetURL, Type type, String token) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("x-api-key","6v3v1QxhkV79mpTEqKpPu3h8ixYUdhxi8WdKFNOr");
//            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Authorization", token);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            return gson.fromJson(response.toString(), type);
        } catch (Exception e) {
            logger.error("Erro ao executar http get (url = " + targetURL + "): " + e.getMessage(), e);
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    public <T> T executeGet(String targetURL, Type type, String token,Map<String,String> params) {
        HttpURLConnection connection = null;
        try {
            StringBuilder targetURLBuilder = new StringBuilder(targetURL);
            Set<String> keys = params.keySet();
            for(String key : keys){
                targetURLBuilder.append("?").append(key).append("=").append(params.get(key));
            }
            targetURL = targetURLBuilder.toString();
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("x-api-key","6v3v1QxhkV79mpTEqKpPu3h8ixYUdhxi8WdKFNOr");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            return gson.fromJson(response.toString(), type);
        } catch (Exception e) {
            logger.error("Erro ao executar http get (url = " + targetURL + "): " + e.getMessage(), e);
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    public void executePut(String targetURL) {
       HttpURLConnection connection = null;
        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getInputStream();

            if (connection.getResponseCode() != HttpStatus.SC_OK &&
                connection.getResponseCode() != HttpStatus.SC_ACCEPTED &&
                connection.getResponseCode() != HttpStatus.SC_CREATED) {

                logger.error("error making request for {} | responde code = {}", targetURL, connection.getResponseCode());
                throw new IntegrationErrorException(targetURL);
            }
        } catch (Exception e) {
            logger.error("Erro ao executar http get (url = " + targetURL + "): " + e.getMessage(), e);
            throw new IntegrationErrorException(targetURL);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    public <T> T executePostWithSec(String targetURL, Class<T> type, String json) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("seckey", "AAAADCNFqNgR6QU3DxGrSBkC7a4Ptdx/TWPN1AepUtUzxJhPcj6VhL9ntVuT/nefznSWgEzzH8diBxyZ108Zx4Ct07rYw91BAsME2KhGSPOVviCZDqcdC9HAFbRXXHh4siDE7VRTvoYdS24XcPNstt1K32qH5eCUva4M11DGFwlZkk4TxCr+PR7KzpDWzUalj0l2m2UIR7ytgUXTRemF31Da+2S2iZehs3l4b2ebuMVy7PybGPAvfh0xY2iVSdAyDPzbtLwQ9xoA+n6/HBtEqQ8Cwem320y/AzLT61AMkSlFO9wGUNJCUcF825Iycp+yCPL8gXaviHcvauNss7sYJ2knNSUT0NrTxncIn9CYgpzpp0+U09968+sv7yOZlZiMPFqxFhTmlJzzEZsfmHnugdCvJu5MEAO0z+hzoHq0t78zusHJvGoLI17eotZefvqM1Q==");

            // connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("content-length", String.valueOf(json.length()));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //connection.setRequestProperty("x-api-key","6v3v1QxhkV79mpTEqKpPu3h8ixYUdhxi8WdKFNOr");
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == 307)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);

            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = connection.getHeaderField("Location");
                // open the new connnection again
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.setRequestProperty("Accept", "*/*");
                System.out.println("Redirect to URL : " + newUrl);
            }

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            if(connection.getResponseCode()!= HttpStatus.SC_OK &&
                    connection.getResponseCode()!= HttpStatus.SC_ACCEPTED &&
                    connection.getResponseCode()!= HttpStatus.SC_CREATED){
                throw new IntegrationErrorException(targetURL);
            }
            return gson.fromJson(response.toString(), type);
        } catch (Exception e) {
            logger.error("Erro ao executar http post (url = " + targetURL + "): " + e.getMessage(), e);
            throw new IntegrationErrorException(targetURL);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    public <T> T executePost(String targetURL, Class<T> type, String json) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");
           // connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("content-length", String.valueOf(json.length()));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //connection.setRequestProperty("x-api-key","6v3v1QxhkV79mpTEqKpPu3h8ixYUdhxi8WdKFNOr");
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == 307)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);

            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = connection.getHeaderField("Location");
                // open the new connnection again
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.setRequestProperty("Accept", "*/*");
                System.out.println("Redirect to URL : " + newUrl);
            }

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            if(connection.getResponseCode()!= HttpStatus.SC_OK &&
                    connection.getResponseCode()!= HttpStatus.SC_ACCEPTED &&
                    connection.getResponseCode()!= HttpStatus.SC_CREATED){
                throw new IntegrationErrorException(targetURL);
            }
            return gson.fromJson(response.toString(), type);
        } catch (Exception e) {
            logger.error("Erro ao executar http post (url = " + targetURL + "): " + e.getMessage(), e);
            throw new IntegrationErrorException(targetURL);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    public <T> T executePostWithBody(String targetURL, Class<T> type, String json) {
        RestTemplate restTemplate = new RestTemplate();
        try{
            Map<String,String> variables = new HashMap<>();
            variables.put("Content-Type","application/json");
            return restTemplate.postForObject(targetURL,json,type,variables);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public <T> T executePostWithHeaderParameter(String targetURL, Class<T> type, String json, Map<String,String> parameterHeader) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "*/*");

            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("content-length", String.valueOf(json.length()));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (parameterHeader != null && !parameterHeader.isEmpty()) {
                for (Map.Entry<String, String> parameter : parameterHeader.entrySet()) {
                    connection.setRequestProperty(parameter.getKey(), parameter.getValue());
                }
            }

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == 307)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);


            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = connection.getHeaderField("Location");
                // open the new connnection again
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.setRequestProperty("Accept", "*/*");
                System.out.println("Redirect to URL : " + newUrl);
            }

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            if(connection.getResponseCode()!= HttpStatus.SC_OK &&
                    connection.getResponseCode()!= HttpStatus.SC_ACCEPTED &&
                    connection.getResponseCode()!= HttpStatus.SC_CREATED){
                throw new IntegrationErrorException(targetURL);
            }
            return gson.fromJson(response.toString(), type);
        } catch (Exception e) {
            logger.error("Erro ao executar http post (url = " + targetURL + "): " + e.getMessage(), e);
            throw new IntegrationErrorException(targetURL);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    public void  executeGetIntegration(String targetURL) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetURL);
            System.out.println("Called URL : " + targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            if(connection.getResponseCode()!= HttpStatus.SC_OK &&
                    connection.getResponseCode()!= HttpStatus.SC_ACCEPTED &&
                    connection.getResponseCode()!= HttpStatus.SC_CREATED){
                throw new IntegrationErrorException(targetURL);
            }

        } catch (Exception e) {
            logger.error("Erro ao executar http get (url = " + targetURL + "): " + e.getMessage(), e);
            throw new IntegrationErrorException(targetURL);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    @Override
    public String getFile(String targetUrl){
        try {

            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            byte[] encodedBytes = Base64.encodeBase64(response.toString().getBytes(StandardCharsets.UTF_8));
            return new String(encodedBytes);

        }catch (Exception e){
            logger.info("Error executing get to url {}, caused by {}",targetUrl,e.getMessage());
            return null;
        }
    }

}