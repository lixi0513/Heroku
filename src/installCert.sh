#!/bin/bash
# $1 : the ip of mediasense server
# $2 : the location of your cacerts file
# assumes the installation of openssl and that Java's keytool  is in the PATH.
# this can be modified to install to another keystore by adding "-keystore PATH" to the keytool line.

openssl s_client -showcerts -connect $1:443 > /tmp/MEDIASENSE-PEM.cert < /dev/null
openssl x509 -in /tmp/MEDIASENSE-PEM.cert -out /tmp/MEDIASENSE.cert
keytool -import -trustcacerts -alias mediasensesnp -file /tmp/MEDIASENSE.cert -keystore $2
rm /tmp/MEDIASENSE.cert
rm /tmp/MEDIASENSE-PEM.cert
