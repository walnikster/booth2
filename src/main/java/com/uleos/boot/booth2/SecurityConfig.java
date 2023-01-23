package com.uleos.boot.booth2;

import org.opensaml.security.x509.X509Support;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2AuthenticationTokenConverter;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    SecurityFilterChain app(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .saml2Login(Customizer.withDefaults())
                .saml2Logout(Customizer.withDefaults());
        // @formatter:on

        return http.build();
    }

    @Bean
    RelyingPartyRegistrationResolver relyingPartyRegistrationResolver(
            RelyingPartyRegistrationRepository registrations) {
        return new DefaultRelyingPartyRegistrationResolver((id) -> registrations.findByRegistrationId("saml2test"));
    }

    @Bean
    Saml2AuthenticationTokenConverter authentication(RelyingPartyRegistrationResolver registrations) {
        return new Saml2AuthenticationTokenConverter(registrations);
    }

    @Bean
    FilterRegistrationBean<Saml2MetadataFilter> metadata(RelyingPartyRegistrationResolver registrations) {
        Saml2MetadataFilter metadata = new Saml2MetadataFilter(registrations, new OpenSamlMetadataResolver());
        FilterRegistrationBean<Saml2MetadataFilter> filter = new FilterRegistrationBean<>(metadata);
        filter.setOrder(-101);
        return filter;
    }

    @Bean
    RelyingPartyRegistrationRepository repository() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new ClassPathResource("saml-certificate/keystore.jks").getInputStream(), "changeit".toCharArray());

        Certificate certificate = keyStore.getCertificate("saml2test");
        X509Certificate x509Certificate = X509Support.decodeCertificate(certificate.getEncoded());

        PrivateKey key = (PrivateKey) keyStore.getKey("saml2test", "changeit".toCharArray());


        RelyingPartyRegistration two = RelyingPartyRegistrations
                .fromMetadataLocation("http://localhost:8580/realms/saml2test/protocol/saml/descriptor")
                .entityId("saml2test")
                .registrationId("saml2test").assertingPartyDetails(
                        c -> c.wantAuthnRequestsSigned(false)
                )
                .signingX509Credentials(
                        (c) -> c.add(Saml2X509Credential.signing(key, x509Certificate)))
                .build();
        return new InMemoryRelyingPartyRegistrationRepository(two);
    }

    X509Certificate relyingPartyCertificate() {
        Resource resource = new ClassPathResource("saml-certificate/publiccert.crt");
        try (InputStream is = resource.getInputStream()) {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
}
