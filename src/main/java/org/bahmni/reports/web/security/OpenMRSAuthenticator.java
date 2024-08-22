package org.bahmni.reports.web.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.reports.BahmniReportsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Component
public class OpenMRSAuthenticator {

    private static final Logger logger = LogManager.getLogger(OpenMRSAuthenticator.class);
    private static final String WHOAMI_URL = "/bahmnicore/whoami";
    public static final String OPENMRS_SESSION_ID_COOKIE_NAME = "JSESSIONID";

    @Autowired
    private BahmniReportsProperties properties;

    public AuthenticationResponse authenticate(String sessionId) {
        ResponseEntity<Privileges> response = callOpenMRS(sessionId);
        HttpStatusCode status = response.getStatusCode();

        if (status.is2xxSuccessful()) {
            return response.getBody().hasReportingPrivilege()?
                    AuthenticationResponse.AUTHORIZED:
                    AuthenticationResponse.UNAUTHORIZED;
        }

        return AuthenticationResponse.NOT_AUTHENTICATED;
    }

    public ResponseEntity<Privileges> callOpenMRS(String sessionId) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", OPENMRS_SESSION_ID_COOKIE_NAME + "=" + sessionId);
        try {
            return new RestTemplate()
                    .exchange(properties.getOpenmrsRootUrl() + WHOAMI_URL,
                            HttpMethod.GET,
                            new HttpEntity<>(null, requestHeaders),
                            Privileges.class
                    );
        } catch (HttpClientErrorException exception) {
            logger.warn("Could not authenticate with OpenMRS", exception);
            return new ResponseEntity<>(exception.getStatusCode());
        }
    }
}
