/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static to.noc.devicefp.server.util.IpUtil.*;
import to.noc.devicefp.server.domain.entity.MaxMindLocation;
import static to.noc.devicefp.server.domain.entity.MaxMindLocation.createMockLocation;
import to.noc.devicefp.server.domain.repository.MaxMindLocationRepository;

/*
 *  Calls the MaxMind Omni GeoIP webservice to retrieve location information
 *  keyed by IP address.
 */
@Service
public class MaxMindServiceImpl implements MaxMindService {
    private static final Logger log = LoggerFactory.getLogger(MaxMindServiceImpl.class);

    @Value("${maxmind.licenseKey}")
    private String maxMindLicenseKey;

    @Autowired
    private MaxMindLocationRepository maxMindLocationRepository;

    private static enum GeoIpField {
        countryCode,
        countryName,
        regionCode,
        regionName,
        city,
        latitude,
        longitude,
        metroCode,
        areaCode,
        timeZone,
        continent,
        postalCode,
        isp,
        organization,
        domain,
        asnum,
        netSpeed,
        userType,
        accuracyRadiusKm,
        countryConf,
        cityConf,
        regionConf,
        postalConf,
        error
    };


    private static void addLocationField(MaxMindLocation location, GeoIpField field, String value) {
        try {
            switch (field) {
                case countryCode:
                    location.setCountryCode(value);
                    break;
                case countryName:
                    location.setCountryName(value);
                    break;
                case regionCode:
                    location.setRegionCode(value);
                    break;
                case regionName:
                    location.setRegionName(value);
                    break;
                case city:
                    location.setCity(value);
                    break;
                case latitude:
                    location.setLatitude(Double.valueOf(value));
                    break;
                case longitude:
                    location.setLongitude(Double.valueOf(value));
                    break;
                case metroCode:
                    location.setMetroCode(value);
                    break;
                case areaCode:
                    location.setAreaCode(value);
                    break;
                case timeZone:
                    location.setTimeZone(value);
                    break;
                case continent:
                    location.setContinent(value);
                    break;
                case postalCode:
                    location.setPostalCode(value);
                    break;
                case isp:
                    location.setIsp(value);
                    break;
                case organization:
                    location.setOrg(value);
                    break;
                case domain:
                    location.setDomain(value);
                    break;
                case asnum:
                    location.setAsnum(value);
                    break;
                case netSpeed:
                    location.setNetSpeed(value);
                    break;
                case userType:
                    location.setUserType(value);
                    break;
                case accuracyRadiusKm:
                    location.setAccuracyRadius(Integer.valueOf(value) * 1000);
                    break;
                case countryConf:
                    location.setCountryConf(value);
                    break;
                case cityConf:
                    location.setCityConf(value);
                    break;
                case regionConf:
                    location.setRegionConf(value);
                    break;
                case postalConf:
                    location.setPostalConf(value);
                    break;
                case error:
                    location.setError(value);
                    log.error("Error received from MaxMind web service ip={} error={}",
                            location.getIpAddress(), value);
                    break;
            }
        } catch (NumberFormatException ex) {
            log.error("Bad value from Maxmind field={} value={}",
                    field.toString(),
                    value);
        }
    }

    @Transactional
    @Override
    public MaxMindLocation loadLocation(String ip) {
        MaxMindLocation location;

        String mutex = ("mm_lookup_" + ip).intern();
        synchronized (mutex) {
            Date tenDaysAgo = new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * 10);
            location = maxMindLocationRepository.getLatestAfterDate(ip, tenDaysAgo);

            if (location == null) { // no cached version
                // if the source ip a private (reserved) ip, we're in a test
                // environment and can use a mock location
                location = isPrivateIp(ip) ?
                        createMockLocation(ip, new Date()) : createFromWebService(ip);
                if (location != null) {
                    // device doesn't cascade to MaxMindLocation, so we need to
                    // flush here (or use a nested transaction
                    location = maxMindLocationRepository.saveAndFlush(location);
                }
            }
        }

        return location;
    }

    private MaxMindLocation createFromWebService(String ip) {
        MaxMindLocation location = null;
        try {
            URL url = new URL("http://geoip.maxmind.com/e?l="+maxMindLicenseKey+"&i="+ip);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10*1000); // 10 seconds

            BufferedReader r = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));

            String resultsLine = r.readLine();
            connection.disconnect();

            if (resultsLine != null) {
                // Split on a comma only if it has zero or an even number of quotes
                // after it. Assumes quotes are never escaped.
                String values[] = resultsLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                location = new MaxMindLocation();
                location.setIpAddress(ip);
                location.setStamp(new Date());

                for (GeoIpField field : GeoIpField.values()) {
                    if (field.ordinal() >= values.length) {
                        break;
                    }
                    String val = values[field.ordinal()];
                    // replace leading and trailing whitespace and quotes
                    val = val.replaceAll("^\"?\\s*|\\s*\"?$", "");
                    if (!val.isEmpty()) {
                        addLocationField(location, field, val);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("", ex);
            location = null;
        }
        return location;
    }

}
