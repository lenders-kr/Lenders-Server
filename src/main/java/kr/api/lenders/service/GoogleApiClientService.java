package kr.api.lenders.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import kr.api.lenders.error.GoogleAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleApiClientService {
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CLIENT_ID = "537355601906-r0dhlkgi5d8thr475ts5grase9g91dd4.apps.googleusercontent.com";

    public Payload verifyToken(final String token) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(token);
        } catch (Exception e) {
            throw new GoogleAuthenticationException();
        }

        if (idToken == null) {
            throw new GoogleAuthenticationException();
        }

        return idToken.getPayload();
    }
}
