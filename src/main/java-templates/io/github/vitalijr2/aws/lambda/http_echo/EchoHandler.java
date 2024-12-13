/*-
 * ---------------LICENSE_START-----------------
 * Mock Loggers
 * ---------------------------------------------
 * Copyright (C) 2024 Vitalij Berdinskih
 * ---------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---------------LICENSE_END-------------------
 */
package io.github.vitalijr2.aws.lambda.http_echo;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class EchoHandler implements
    RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

  public static final String LOGGER_NAME = "echo";

  private static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);

  private final String index;

  public EchoHandler() {
    try (var indexStream = getClass().getResourceAsStream("/index.html")) {
      requireNonNull(indexStream, "Resource index.html is not found");
      index = new BufferedReader(new InputStreamReader(indexStream, StandardCharsets.UTF_8)).lines()
          .collect(Collectors.joining());
    } catch (IOException exception) {
      LOGGER.error("Could not initialise: {}", exception.getMessage());
      throw new RuntimeException(exception);
    }
  }

  @Override
  public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent requestEvent,
      Context context) {
    MDC.put("@aws-request-id@", context.getAwsRequestId());
    LOGGER.debug("Request event: {}", requestEvent);

    Optional<APIGatewayV2HTTPResponse> responseEvent = empty();
    var request = requestEvent.getRequestContext().getHttp();

    if ("GET".equals(request.getMethod()) && "/".equals(request.getPath())) {
      responseEvent = Optional.of(LambdaUtils.responseOK(index));
    } else {
      var requestBody = requestEvent.getBody();
      var requestHeaders = requestEvent.getHeaders();
      var responseBody = new JSONObject();

      if (nonNull(requestHeaders) && !requestHeaders.isEmpty()) {
        var headers = new JSONArray();
        requestHeaders.forEach((key, value) -> headers.put(key + ": " + value));
        responseBody.put("headers", headers);
        LOGGER.info("Request headers:\n{}", headers);
      }
      if (nonNull(requestBody) && !requestBody.isBlank()) {
        if (requestEvent.getIsBase64Encoded()) {
          requestBody = new String(Base64.getDecoder().decode(requestBody));
        }
        responseBody.put("body", requestBody);
        LOGGER.info("Request body:\n{}", requestBody);
      }
      if (!responseBody.isEmpty()) {
        responseEvent = Optional.of(LambdaUtils.getResponseEvent(responseBody.toString()));
      }
    }

    return responseEvent.orElseGet(LambdaUtils::responseOK);
  }

}
