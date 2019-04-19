#!/bin/sh

identity_version=7.3.p1
echo "Start adding local maven identity dependencies version:"$identity_version

echo "Adding identity.jar"
mvn install:install-file -DgroupId=com.sailpoint -Dversion=$identity_version -Dpackaging=jar -DartifactId=identityiq -Dfile=identityiq.jar

echo "Adding connector-bundle.jar"
mvn install:install-file -DgroupId=com.sailpoint -Dversion=$identity_version -Dpackaging=jar -DartifactId=connector-bundle -Dfile=connector-bundle.jar